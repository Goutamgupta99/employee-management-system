package com.goutam.ems.service;

import org.springframework.data.domain.Pageable;

import com.goutam.ems.dto.ApiResponseDto;
import com.goutam.ems.dto.EmployeeRequestDto;
import com.goutam.ems.dto.EmployeeResponseDto;
import com.goutam.ems.dto.PageResponseDto;

/**
 * Defines business operations for Employee management.
 */
public interface EmployeeService {

	/**
	 * Creates a new employee.
	 */
	EmployeeResponseDto saveEmployee(EmployeeRequestDto employee);

	/**
	 * Retrieves employees with optional search, pagination and sorting.
	 */
	PageResponseDto<EmployeeResponseDto> getAllEmployees(String keyword, Pageable pageable);

	/**
	 * Retrieves an employee by ID.
	 */
	EmployeeResponseDto getEmployeeById(Long id);

	/**
	 * Updates an existing employee.
	 */
	EmployeeResponseDto updateEmployee(Long id, EmployeeRequestDto employee);

	/**
	 * Deletes an employee by ID.
	 */
	ApiResponseDto deleteEmployee(Long id);
}