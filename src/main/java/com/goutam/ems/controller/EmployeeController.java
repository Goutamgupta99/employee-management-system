package com.goutam.ems.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.goutam.ems.dto.ApiResponse;
import com.goutam.ems.dto.EmployeeRequestDto;
import com.goutam.ems.dto.EmployeeResponseDto;
import com.goutam.ems.service.EmployeeService;

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
 * 2. REST Controller Pattern -> Exposes RESTful endpoints using HTTP methods.
 *
 * SOLID Principles: ✔ Single Responsibility Principle (SRP) -> Responsible only
 * for handling HTTP requests/responses.
 *
 * ✔ Dependency Inversion Principle (DIP) -> Depends on EmployeeService
 * abstraction instead of implementation.
 *
 * Request Flow:
 *
 * Client │ ▼ EmployeeController │ ▼ EmployeeService │ ▼ EmployeeRepository │ ▼
 * PostgreSQL Database
 *
 * ============================================================================
 */
@RestController
@RequestMapping("/api/employees")
@Validated
public class EmployeeController {

	/**
	 * Constructor Injection
	 *
	 * Spring injects EmployeeService automatically.
	 *
	 * Benefits: - Loose Coupling - Better Testability - Immutable Dependency
	 * (final) - Recommended by Spring
	 */
	private final EmployeeService employeeService;

	public EmployeeController(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	/**
	 * Create Employee API
	 *
	 * HTTP Method: POST /api/employees
	 *
	 * Request: EmployeeRequestDto
	 *
	 * Response: EmployeeResponseDto
	 *
	 * Steps: 1. Accept request body. 2. Validate request using Bean Validation. 3.
	 * Delegate processing to Service layer. 4. Return HTTP 201 (Created).
	 */
	@PostMapping
	public ResponseEntity<EmployeeResponseDto> saveEmployee(@Valid @RequestBody EmployeeRequestDto employee) {

		EmployeeResponseDto savedEmployee = employeeService.saveEmployee(employee);

		return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
	}

	/**
	 * Get All Employees API
	 *
	 * HTTP Method: GET /api/employees
	 *
	 * Response: List<EmployeeResponseDto>
	 *
	 * Steps: 1. Delegate request to Service. 2. Return list of employees. 3.
	 * Respond with HTTP 200 (OK).
	 */
	@GetMapping
	public ResponseEntity<List<EmployeeResponseDto>> getAllEmployees() {

		List<EmployeeResponseDto> employees = employeeService.getAllEmployees();

		return ResponseEntity.ok(employees);
	}

	/**
	 * Get Employee By Id API
	 *
	 * HTTP Method: GET /api/employees/{id}
	 *
	 * Path Variable: id
	 *
	 * Response: EmployeeResponseDto
	 *
	 * Steps: 1. Read employee id. 2. Delegate lookup to Service. 3. Return employee
	 * details.
	 */
	@GetMapping("/{id}")
	public ResponseEntity<EmployeeResponseDto> getEmployeeById(@PathVariable Long id) {

		EmployeeResponseDto employee = employeeService.getEmployeeById(id);

		return ResponseEntity.ok(employee);
	}

	/**
	 * Update Employee API
	 *
	 * HTTP Method: PUT /api/employees/{id}
	 *
	 * Request: EmployeeRequestDto
	 *
	 * Response: EmployeeResponseDto
	 *
	 * Steps: 1. Read employee id. 2. Validate request body. 3. Delegate update to
	 * Service. 4. Return updated employee.
	 */
	@PutMapping("/{id}")
	public ResponseEntity<EmployeeResponseDto> updateEmployee(@PathVariable Long id,
			@Valid @RequestBody EmployeeRequestDto employee) {

		EmployeeResponseDto updatedEmployee = employeeService.updateEmployee(id, employee);

		return ResponseEntity.ok(updatedEmployee);
	}

	/**
	 * Delete Employee API
	 *
	 * HTTP Method: DELETE /api/employees/{id}
	 *
	 * Response: ApiResponse
	 *
	 * Steps: 1. Read employee id. 2. Delegate delete operation to Service. 3.
	 * Return success response.
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse> deleteEmployee(@PathVariable Long id) {

		ApiResponse response = employeeService.deleteEmployee(id);

		return ResponseEntity.ok(response);
	}
}