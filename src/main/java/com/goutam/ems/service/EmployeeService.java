package com.goutam.ems.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.goutam.ems.dto.ApiResponseDto;
import com.goutam.ems.dto.EmployeeRequestDto;
import com.goutam.ems.dto.EmployeeResponseDto;
import com.goutam.ems.dto.PageResponseDto;

import jakarta.validation.Valid;

/**
 * ============================================================================
 * EmployeeService
 * ============================================================================
 *
 * Responsibility: Defines business operations related to Employee management.
 *
 * The Controller depends on this interface instead of the implementation.
 *
 * This follows: - Service Layer Pattern - Dependency Inversion Principle
 *
 * ============================================================================
 */
public interface EmployeeService {

	EmployeeResponseDto saveEmployee(EmployeeRequestDto employee);

	/**
	 * Retrieves employees with optional keyword search, pagination and sorting.
	 */
	PageResponseDto<EmployeeResponseDto> getAllEmployees(String keyword, Pageable pageable);

	EmployeeResponseDto getEmployeeById(Long id);

	EmployeeResponseDto updateEmployee(Long id, @Valid EmployeeRequestDto employee);

	ApiResponseDto deleteEmployee(Long id);
}