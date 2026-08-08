package com.goutam.ems.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.goutam.ems.dto.ApiResponseDto;
import com.goutam.ems.dto.EmployeeRequestDto;
import com.goutam.ems.dto.EmployeeResponseDto;
import com.goutam.ems.dto.PageResponseDto;
import com.goutam.ems.service.EmployeeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/employees")
@Validated
@Tag(name = "Employee Management", description = "REST APIs for Employee Management System")
public class EmployeeController {

	private final EmployeeService employeeService;

	public EmployeeController(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	/**
	 * Creates a new employee.
	 */
	@Operation(summary = "Create Employee", description = "Creates a new employee after validating duplicate Employee Code and Email.")
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "Employee created successfully", content = @Content(schema = @Schema(implementation = EmployeeResponseDto.class))),
			@ApiResponse(responseCode = "400", description = "Validation failed"),
			@ApiResponse(responseCode = "409", description = "Employee already exists") })
	@PostMapping
	public ResponseEntity<EmployeeResponseDto> saveEmployee(@Valid @RequestBody EmployeeRequestDto employee) {

		EmployeeResponseDto savedEmployee = employeeService.saveEmployee(employee);

		return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
	}

	/**
	 * Retrieves employees with optional search, pagination and sorting.
	 *
	 * Example: /api/employees?keyword=java&page=0&size=10&sort=firstName,asc
	 */
	@Operation(summary = "Get / Search Employees", description = "Retrieves employees with optional keyword search, pagination and sorting.")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Employees fetched successfully") })
	@GetMapping
	public ResponseEntity<PageResponseDto<EmployeeResponseDto>> getAllEmployees(
			@RequestParam(required = false) String keyword, Pageable pageable) {

		PageResponseDto<EmployeeResponseDto> employees = employeeService.getAllEmployees(keyword, pageable);

		return ResponseEntity.ok(employees);
	}

	/**
	 * Retrieves an employee by ID.
	 */
	@Operation(summary = "Get Employee By Id", description = "Retrieves employee details using employee ID.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Employee found", content = @Content(schema = @Schema(implementation = EmployeeResponseDto.class))),
			@ApiResponse(responseCode = "404", description = "Employee not found") })
	@GetMapping("/{id}")
	public ResponseEntity<EmployeeResponseDto> getEmployeeById(@PathVariable Long id) {

		EmployeeResponseDto employee = employeeService.getEmployeeById(id);

		return ResponseEntity.ok(employee);
	}

	/**
	 * Updates an existing employee.
	 */
	@Operation(summary = "Update Employee", description = "Updates an existing employee.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Employee updated successfully", content = @Content(schema = @Schema(implementation = EmployeeResponseDto.class))),
			@ApiResponse(responseCode = "400", description = "Validation failed"),
			@ApiResponse(responseCode = "404", description = "Employee not found"),
			@ApiResponse(responseCode = "409", description = "Duplicate Employee Code or Email") })
	@PutMapping("/{id}")
	public ResponseEntity<EmployeeResponseDto> updateEmployee(@PathVariable Long id,
			@Valid @RequestBody EmployeeRequestDto employee) {

		EmployeeResponseDto updatedEmployee = employeeService.updateEmployee(id, employee);

		return ResponseEntity.ok(updatedEmployee);
	}

	/**
	 * Deletes an employee by ID.
	 */
	@Operation(summary = "Delete Employee", description = "Deletes an employee using employee ID.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Employee deleted successfully", content = @Content(schema = @Schema(implementation = ApiResponseDto.class))),
			@ApiResponse(responseCode = "404", description = "Employee not found") })
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponseDto> deleteEmployee(@PathVariable Long id) {

		ApiResponseDto response = employeeService.deleteEmployee(id);

		return ResponseEntity.ok(response);
	}
}