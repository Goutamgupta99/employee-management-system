package com.goutam.ems.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;

import com.goutam.ems.constant.ApiStatus;
import com.goutam.ems.constant.MessageConstants;
import com.goutam.ems.dto.ApiResponse;
import com.goutam.ems.entity.Employee;
import com.goutam.ems.exception.EmployeeAlreadyExistsException;
import com.goutam.ems.exception.EmployeeNotFoundException;
import com.goutam.ems.repository.EmployeeRepository;
import com.goutam.ems.service.EmployeeService;

/**
 * ============================================================================
 * EmployeeServiceImpl
 * ============================================================================
 *
 * Responsibility: Contains all business logic related to Employee operations.
 *
 * Design Patterns Used: 1. Service Layer Pattern - Keeps business logic
 * separate from Controller.
 *
 * 2. Repository Pattern - Uses Spring Data JPA Repository for database
 * operations.
 *
 * SOLID Principles: ✔ Single Responsibility Principle (SRP) -> Service contains
 * only business logic.
 *
 * ✔ Dependency Inversion Principle (DIP) -> Depends on EmployeeRepository
 * abstraction instead of database implementation.
 *
 * ✔ DRY (Don't Repeat Yourself) -> Common methods like findEmployeeById() are
 * reused.
 *
 * Flow: Controller ↓ EmployeeService ↓ EmployeeServiceImpl (Business Logic) ↓
 * EmployeeRepository ↓ PostgreSQL
 *
 * Future Improvements: - DTO Pattern - MapStruct Mapper - Transaction
 * Management - Caching - Logging - Security (JWT)
 * ============================================================================
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {

	/**
	 * Constructor Injection
	 *
	 * Spring injects EmployeeRepository automatically.
	 *
	 * Principle: - Dependency Injection (DI) - Dependency Inversion Principle
	 * (SOLID)
	 *
	 * Benefits: - Better Testability - Immutable Dependency (final) - Recommended
	 * by Spring
	 */
	private final EmployeeRepository employeeRepository;

	public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}

	/**
	 * Save Employee
	 *
	 * Steps: 1. Validate duplicate Employee Code & Email 2. Persist Employee
	 *
	 * Principle: - Fail Fast - Single Responsibility Principle
	 */
	@Override
	public Employee saveEmployee(Employee employee) {

		validateDuplicateEmployee(employee);

		return employeeRepository.save(employee);
	}

	/**
	 * Checks duplicate Employee Code & Email before saving.
	 *
	 * Why? Avoid database constraint violations and provide meaningful business
	 * exceptions.
	 *
	 * Principle: - DRY - Encapsulation - Business Rule Validation
	 */
	private void validateDuplicateEmployee(Employee employee) {

		if (employeeRepository.existsByEmployeeCode(employee.getEmployeeCode())) {
			throw new EmployeeAlreadyExistsException(MessageConstants.EMPLOYEE_CODE_EXISTS);
		}

		if (employeeRepository.existsByEmail(employee.getEmail())) {
			throw new EmployeeAlreadyExistsException(MessageConstants.EMAIL_EXISTS);
		}
	}

	/**
	 * Fetch all Employees.
	 *
	 * Repository Pattern
	 */
	@Override
	public List<Employee> getAllEmployees() {
		return employeeRepository.findAll();
	}

	/**
	 * Fetch Employee by Id.
	 *
	 * Reuses common helper method instead of repeating logic.
	 *
	 * Principle: DRY
	 */
	@Override
	public Employee getEmployeeById(Long id) {
		return findEmployeeById(id);
	}

	/**
	 * Update Employee.
	 *
	 * Steps: 1. Verify Employee exists 2. Validate duplicate fields if changed 3.
	 * Update required fields 4. Save updated entity
	 *
	 * Principle: - DRY - Separation of Concerns - Business Rule Validation
	 */
	@Override
	public Employee updateEmployee(Long id, Employee employee) {

		Employee existingEmployee = findEmployeeById(id);

		validateDuplicateOnUpdate(existingEmployee, employee);

		updateEmployeeFields(existingEmployee, employee);

		return employeeRepository.save(existingEmployee);
	}

	/**
	 * Delete Employee.
	 *
	 * Steps: 1. Verify Employee exists 2. Delete Employee 3. Return Success
	 * Response
	 */
	@Override
	public ApiResponse deleteEmployee(Long id) {

		Employee existingEmployee = findEmployeeById(id);

		employeeRepository.delete(existingEmployee);

		return new ApiResponse(MessageConstants.EMPLOYEE_DELETED, ApiStatus.SUCCESS);
	}

	/**
	 * Common helper method.
	 *
	 * Fetch Employee by Id.
	 *
	 * Why? Used by Get, Update and Delete APIs.
	 *
	 * Principle: DRY (Don't Repeat Yourself)
	 */
	private Employee findEmployeeById(Long id) {

		return employeeRepository.findById(id)
				.orElseThrow(() -> new EmployeeNotFoundException(MessageConstants.EMPLOYEE_NOT_FOUND + id));
	}

	/**
	 * Copies request data into managed entity.
	 *
	 * Why? Prevents replacing managed JPA entity.
	 *
	 * Future: This method will be replaced with MapStruct Mapper.
	 */
	private void updateEmployeeFields(Employee existing, Employee updated) {

		existing.setEmployeeCode(updated.getEmployeeCode());
		existing.setFirstName(updated.getFirstName());
		existing.setLastName(updated.getLastName());
		existing.setEmail(updated.getEmail());
		existing.setPhone(updated.getPhone());
		existing.setDepartment(updated.getDepartment());
		existing.setDesignation(updated.getDesignation());
		existing.setSalary(updated.getSalary());
		existing.setJoiningDate(updated.getJoiningDate());
		existing.setStatus(updated.getStatus());
	}

	/**
	 * Validates duplicate fields during Update.
	 *
	 * Why? Duplicate validation should only happen when Employee Code or Email is
	 * modified.
	 *
	 * Example: Existing Email = abc@gmail.com Updated Email = abc@gmail.com
	 *
	 * No validation required.
	 *
	 * Existing Email = abc@gmail.com Updated Email = xyz@gmail.com
	 *
	 * Validate uniqueness.
	 *
	 * Principle: Fail Fast
	 */
	private void validateDuplicateOnUpdate(Employee existing, Employee updated) {

		if (!existing.getEmployeeCode().equals(updated.getEmployeeCode())
				&& employeeRepository.existsByEmployeeCode(updated.getEmployeeCode())) {

			throw new EmployeeAlreadyExistsException(MessageConstants.EMPLOYEE_CODE_EXISTS);
		}

		if (!existing.getEmail().equals(updated.getEmail()) && employeeRepository.existsByEmail(updated.getEmail())) {

			throw new EmployeeAlreadyExistsException(MessageConstants.EMAIL_EXISTS);
		}
	}
}