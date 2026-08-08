package com.goutam.ems.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.goutam.ems.dto.ApiResponseDto;
import com.goutam.ems.dto.EmployeeRequestDto;
import com.goutam.ems.dto.EmployeeResponseDto;
import com.goutam.ems.service.EmployeeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.Valid;

/**
 * ============================================================================
 * EmployeeController
 * ============================================================================
 *
 * Responsibility: Exposes REST APIs for Employee operations.
 *
 * The Controller acts as the entry point of the application. It accepts HTTP
 * requests from clients, delegates business logic to the Service layer, and
 * returns HTTP responses.
 *
 * It DOES NOT contain any business logic.
 *
 * Design Patterns Used: 1. MVC (Model-View-Controller) -> Acts as the
 * Controller layer.
 *
 * 2. REST Controller Pattern -> Exposes RESTful endpoints.
 *
 * SOLID Principles: ✔ Single Responsibility Principle (SRP) -> Handles only
 * HTTP requests/responses.
 *
 * ✔ Dependency Inversion Principle (DIP) -> Depends on EmployeeService
 * abstraction.
 * ============================================================================
 */
@RestController
@RequestMapping("/api/employees")
@Validated
@Tag(name = "Employee Management", description = "REST APIs for Employee Management System")
public class EmployeeController {

	/**
	 * Constructor Injection
	 */
	private final EmployeeService employeeService;

	public EmployeeController(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	/**
	 * Create Employee API
	 */
	@Operation(summary = "Create Employee", description = "Creates a new employee after validating duplicate Employee Code and Email.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Employee created successfully", content = @Content(schema = @Schema(implementation = EmployeeResponseDto.class))),
			@ApiResponse(responseCode = "400", description = "Validation failed"),
			@ApiResponse(responseCode = "409", description = "Employee already exists") })
	@PostMapping
	public ResponseEntity<EmployeeResponseDto> saveEmployee(@Valid @RequestBody EmployeeRequestDto employee) {

		EmployeeResponseDto savedEmployee = employeeService.saveEmployee(employee);

		return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
	}

	/**
	 * Get All Employees API
	 */
	@Operation(summary = "Get All Employees", description = "Retrieves all employees from the database.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Employees fetched successfully") })
	@GetMapping
	public ResponseEntity<Page<EmployeeResponseDto>> getAllEmployees(Pageable pageable) {
		Page<EmployeeResponseDto> employees = employeeService.getAllEmployees(pageable);
		return ResponseEntity.ok(employees);
	}

	/**
	 * Get Employee By Id API
	 */
	@Operation(summary = "Get Employee By Id", description = "Retrieves employee details using employee id.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Employee found", content = @Content(schema = @Schema(implementation = EmployeeResponseDto.class))),
			@ApiResponse(responseCode = "404", description = "Employee not found") })
	@GetMapping("/{id}")
	public ResponseEntity<EmployeeResponseDto> getEmployeeById(@PathVariable Long id) {

		EmployeeResponseDto employee = employeeService.getEmployeeById(id);

		return ResponseEntity.ok(employee);
	}

	/**
	 * Update Employee API
	 */
	@Operation(summary = "Update Employee", description = "Updates an existing employee.")
	@ApiResponses(value = {
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
	 * Delete Employee API
	 */
	@Operation(summary = "Delete Employee", description = "Deletes an employee using employee id.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Employee deleted successfully", content = @Content(schema = @Schema(implementation = ApiResponseDto.class))),
			@ApiResponse(responseCode = "404", description = "Employee not found") })
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponseDto> deleteEmployee(@PathVariable Long id) {

		ApiResponseDto response = employeeService.deleteEmployee(id);

		return ResponseEntity.ok(response);
	}
}