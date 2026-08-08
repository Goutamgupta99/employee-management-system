package com.goutam.ems.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goutam.ems.constant.ApiStatus;
import com.goutam.ems.constant.MessageConstants;
import com.goutam.ems.dto.ApiResponseDto;
import com.goutam.ems.dto.EmployeeRequestDto;
import com.goutam.ems.dto.EmployeeResponseDto;
import com.goutam.ems.entity.Employee;
import com.goutam.ems.exception.EmployeeAlreadyExistsException;
import com.goutam.ems.exception.EmployeeNotFoundException;
import com.goutam.ems.mapper.EmployeeMapper;
import com.goutam.ems.repository.EmployeeRepository;
import com.goutam.ems.service.EmployeeService;

/**
 * ============================================================================
 * EmployeeServiceImpl
 * ============================================================================
 *
 * PURPOSE ------- Contains all business logic related to Employee operations.
 *
 * Responsibilities: - Validate business rules - Perform CRUD operations -
 * Convert DTO <-> Entity using Mapper - Interact with Repository - Throw
 * business exceptions when required
 *
 * DESIGN PATTERNS ---------------- ✔ Service Layer Pattern Business logic stays
 * separate from Controller.
 *
 * ✔ Repository Pattern Repository communicates with the database.
 *
 * ✔ Mapper Pattern Converts DTOs to Entity and Entity to DTO.
 *
 * SOLID PRINCIPLES ---------------- ✔ SRP Service only contains business logic.
 *
 * ✔ DIP Depends on Repository abstraction rather than DB implementation.
 *
 * ✔ DRY Common methods like findEmployeeById() are reused.
 *
 * APPLICATION FLOW ---------------- Client ↓ Controller ↓ Service ↓ Repository
 * ↓ PostgreSQL
 *
 * ============================================================================
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {

	/**
	 * Constructor Injection
	 *
	 * Spring automatically injects these dependencies.
	 *
	 * Why Constructor Injection? ✔ Immutable dependencies ✔ Easier Unit Testing ✔
	 * Recommended by Spring
	 *
	 * Principle: Dependency Injection (DI) Dependency Inversion Principle (DIP)
	 */
	private final EmployeeRepository employeeRepository;
	private final EmployeeMapper employeeMapper;

	public EmployeeServiceImpl(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper) {

		this.employeeRepository = employeeRepository;
		this.employeeMapper = employeeMapper;
	}

	/**
	 * =========================================================================
	 * CREATE EMPLOYEE
	 * =========================================================================
	 *
	 * Steps ----- 1. Convert Request DTO -> Entity 2. Validate duplicate employee
	 * 3. Save into database 4. Convert Entity -> Response DTO
	 *
	 * Why @Transactional? ------------------- All database operations execute as
	 * one transaction.
	 *
	 * If anything fails, Spring automatically rolls back the transaction.
	 *
	 * Pattern: Unit Of Work
	 *
	 * Principle: ACID Transaction
	 */
	@Transactional
	@Override
	public EmployeeResponseDto saveEmployee(EmployeeRequestDto requestDto) {

		Employee employee = employeeMapper.toEntity(requestDto);

		validateDuplicateOnCreate(employee);

		Employee savedEmployee = employeeRepository.save(employee);

		return employeeMapper.toResponseDto(savedEmployee);
	}

	/**
	 * Business Validation for CREATE.
	 *
	 * Checks: - Employee Code must be unique. - Email must be unique.
	 *
	 * Why? ---- Prevent duplicate records before database insertion.
	 *
	 * Principle: Fail Fast
	 */
	private void validateDuplicateOnCreate(Employee employee) {

		if (employeeRepository.existsByEmployeeCode(employee.getEmployeeCode())) {
			throw new EmployeeAlreadyExistsException(MessageConstants.EMPLOYEE_CODE_EXISTS);
		}

		if (employeeRepository.existsByEmail(employee.getEmail())) {
			throw new EmployeeAlreadyExistsException(MessageConstants.EMAIL_EXISTS);
		}
	}

	/**
	 * ============================================================================
	 * GET ALL / SEARCH EMPLOYEES
	 * ============================================================================
	 *
	 * Supports:
	 *
	 * 1. Normal pagination 2. Sorting 3. Keyword search
	 *
	 * Examples:
	 *
	 * GET /api/employees
	 *
	 * GET /api/employees?page=0&size=5
	 *
	 * GET /api/employees?keyword=java
	 *
	 * GET /api/employees?keyword=java&page=0&size=5&sort=firstName,asc
	 *
	 * Business Flow:
	 *
	 * Controller ↓ Service ↓ Repository ↓ PostgreSQL
	 *
	 * If keyword is empty: → Fetch all employees
	 *
	 * If keyword is provided: → Search employees using keyword
	 *
	 * Pageable handles pagination and sorting.
	 * ============================================================================
	 */
	@Transactional(readOnly = true)
	@Override
	public Page<EmployeeResponseDto> getAllEmployees(String keyword, Pageable pageable) {

		Page<Employee> employees;
		if (keyword == null || keyword.trim().isEmpty()) {
			// No search keyword → fetch all employees
			employees = employeeRepository.findAll(pageable);
		} else {
			// Search using the provided keyword
			employees = employeeRepository.searchEmployees(keyword.trim(), pageable);
		}
		return employees.map(employeeMapper::toResponseDto);
	}

	/**
	 * ========================================================================= GET
	 * EMPLOYEE BY ID
	 * =========================================================================
	 *
	 * Steps ----- 1. Find Employee 2. Convert Entity -> DTO
	 *
	 * Principle: DRY
	 */
	@Transactional(readOnly = true)
	@Override
	public EmployeeResponseDto getEmployeeById(Long id) {

		return employeeMapper.toResponseDto(findEmployeeById(id));
	}

	/**
	 * =========================================================================
	 * UPDATE EMPLOYEE
	 * =========================================================================
	 *
	 * Steps ----- 1. Fetch existing employee. 2. Validate duplicate values only if
	 * changed. 3. Copy request data into managed entity. 4. Save updated entity. 5.
	 * Return Response DTO.
	 *
	 * Principle: Separation of Concerns
	 */
	@Transactional
	@Override
	public EmployeeResponseDto updateEmployee(Long id, EmployeeRequestDto requestDto) {

		Employee existingEmployee = findEmployeeById(id);

		validateDuplicateOnUpdate(existingEmployee, requestDto);

		employeeMapper.updateEntity(existingEmployee, requestDto);

		Employee updatedEmployee = employeeRepository.save(existingEmployee);

		return employeeMapper.toResponseDto(updatedEmployee);
	}

	/**
	 * =========================================================================
	 * DELETE EMPLOYEE
	 * =========================================================================
	 *
	 * Steps ----- 1. Verify employee exists. 2. Delete employee. 3. Return success
	 * response.
	 */
	@Transactional
	@Override
	public ApiResponseDto deleteEmployee(Long id) {

		Employee employee = findEmployeeById(id);

		employeeRepository.delete(employee);

		return new ApiResponseDto(MessageConstants.EMPLOYEE_DELETED, ApiStatus.SUCCESS);
	}

	/**
	 * =========================================================================
	 * COMMON HELPER METHOD
	 * =========================================================================
	 *
	 * Fetch employee by ID.
	 *
	 * Used by: - GET - UPDATE - DELETE
	 *
	 * Why? ---- Prevent duplicate code.
	 *
	 * Principle: DRY
	 */
	private Employee findEmployeeById(Long id) {

		return employeeRepository.findById(id)
				.orElseThrow(() -> new EmployeeNotFoundException(MessageConstants.EMPLOYEE_NOT_FOUND + id));
	}

	/**
	 * =========================================================================
	 * UPDATE VALIDATION
	 * =========================================================================
	 *
	 * Duplicate validation happens ONLY if the user changed Employee Code or Email.
	 *
	 * Example -------
	 *
	 * Existing Email: abc@gmail.com
	 *
	 * Updated Email: abc@gmail.com
	 *
	 * No validation required.
	 *
	 * ---------------------------
	 *
	 * Existing Email: abc@gmail.com
	 *
	 * Updated Email: xyz@gmail.com
	 *
	 * Check whether xyz@gmail.com already exists.
	 *
	 * Principle: Fail Fast
	 */
	private void validateDuplicateOnUpdate(Employee existing, EmployeeRequestDto updated) {

		if (!existing.getEmployeeCode().equals(updated.getEmployeeCode())
				&& employeeRepository.existsByEmployeeCode(updated.getEmployeeCode())) {

			throw new EmployeeAlreadyExistsException(MessageConstants.EMPLOYEE_CODE_EXISTS);
		}

		if (!existing.getEmail().equals(updated.getEmail()) && employeeRepository.existsByEmail(updated.getEmail())) {

			throw new EmployeeAlreadyExistsException(MessageConstants.EMAIL_EXISTS);
		}
	}

}