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
import com.goutam.ems.dto.PageResponseDto;
import com.goutam.ems.entity.Employee;
import com.goutam.ems.exception.EmployeeAlreadyExistsException;
import com.goutam.ems.exception.EmployeeNotFoundException;
import com.goutam.ems.mapper.EmployeeMapper;
import com.goutam.ems.repository.EmployeeRepository;
import com.goutam.ems.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	private final EmployeeRepository employeeRepository;
	private final EmployeeMapper employeeMapper;

	public EmployeeServiceImpl(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper) {

		this.employeeRepository = employeeRepository;
		this.employeeMapper = employeeMapper;
	}

	/**
	 * Creates a new employee after validating unique fields.
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
	 * Checks whether employee code or email already exists.
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
	 * Retrieves employees using optional keyword search, pagination and sorting.
	 */
	@Transactional(readOnly = true)
	@Override
	public PageResponseDto<EmployeeResponseDto> getAllEmployees(String keyword, Pageable pageable) {

		Page<Employee> employeePage;

		if (keyword == null || keyword.isBlank()) {

			employeePage = employeeRepository.findAll(pageable);

		} else {

			employeePage = employeeRepository.searchEmployees(keyword, pageable);
		}

		List<EmployeeResponseDto> employees = employeePage.getContent().stream().map(employeeMapper::toResponseDto)
				.toList();

		return new PageResponseDto<>(employees, employeePage.getNumber(), employeePage.getSize(),
				employeePage.getTotalElements(), employeePage.getTotalPages(), employeePage.isFirst(),
				employeePage.isLast());
	}

	/**
	 * Retrieves an employee by ID.
	 */
	@Transactional(readOnly = true)
	@Override
	public EmployeeResponseDto getEmployeeById(Long id) {

		return employeeMapper.toResponseDto(findEmployeeById(id));
	}

	/**
	 * Updates an existing employee after validating duplicate employee code and
	 * email.
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
	 * Deletes an employee by ID.
	 */
	@Transactional
	@Override
	public ApiResponseDto deleteEmployee(Long id) {

		Employee employee = findEmployeeById(id);

		employeeRepository.delete(employee);

		return new ApiResponseDto(MessageConstants.EMPLOYEE_DELETED, ApiStatus.SUCCESS);
	}

	/**
	 * Finds an employee by ID or throws an exception when the employee does not
	 * exist.
	 */
	private Employee findEmployeeById(Long id) {

		return employeeRepository.findById(id)
				.orElseThrow(() -> new EmployeeNotFoundException(MessageConstants.EMPLOYEE_NOT_FOUND + id));
	}

	/**
	 * Validates duplicate fields during employee update.
	 *
	 * Validation is performed only when the value has changed.
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