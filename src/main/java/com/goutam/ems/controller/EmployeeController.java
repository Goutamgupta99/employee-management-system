package com.goutam.ems.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

import com.goutam.ems.config.PaginationConfig;
import com.goutam.ems.dto.ApiResponseDto;
import com.goutam.ems.dto.EmployeeRequestDto;
import com.goutam.ems.dto.EmployeeResponseDto;
import com.goutam.ems.dto.PageResponseDto;
import com.goutam.ems.exception.InvalidPaginationException;
import com.goutam.ems.exception.InvalidSortFieldException;
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

	private static final int DEFAULT_PAGE = 0;
	private static final int DEFAULT_SIZE = 10;
	private static final int MAX_PAGE_SIZE = 50;

	private final EmployeeService employeeService;

	public EmployeeController(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	// ==================== CREATE EMPLOYEE ====================

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

	// ==================== GET / SEARCH EMPLOYEES ====================

	@Operation(summary = "Get / Search Employees", description = """
			Retrieves employees with optional keyword search,
			pagination and sorting.

			Default page = 0
			Default size = 10
			Maximum size = 50

			Examples:
			/api/employees
			/api/employees?page=0&size=10
			/api/employees?page=0&size=10&sort=firstName,asc
			/api/employees?keyword=java&page=0&size=10
			""")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Employees fetched successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid pagination or sorting parameters") })
	@GetMapping
	public ResponseEntity<PageResponseDto<EmployeeResponseDto>> getAllEmployees(
			@RequestParam(required = false) String keyword, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String sort) {

		// Validate page number
		validatePage(page);

		// Validate page size
		validateSize(size);

		// Build sorting
		Sort sortObject = buildSort(sort);

		// Validate allowed sorting fields
		validateSortFields(sortObject);

		// Create Pageable after validation
		Pageable pageable = PageRequest.of(page, size, sortObject);

		PageResponseDto<EmployeeResponseDto> employees = employeeService.getAllEmployees(keyword, pageable);

		return ResponseEntity.ok(employees);
	}

	// ==================== PAGE VALIDATION ====================

	private void validatePage(int page) {

		if (page < DEFAULT_PAGE) {
			throw new InvalidPaginationException("Page number must be greater than or equal to 0");
		}
	}

	// ==================== SIZE VALIDATION ====================

	private void validateSize(int size) {

		if (size <= 0) {
			throw new InvalidPaginationException("Page size must be greater than 0");
		}

		if (size > MAX_PAGE_SIZE) {
			throw new InvalidPaginationException("Page size must not exceed " + MAX_PAGE_SIZE);
		}
	}

	// ==================== SORT BUILDING ====================

	private Sort buildSort(String sort) {

		if (sort == null || sort.isBlank()) {
			return Sort.by(Sort.Order.asc("firstName"), Sort.Order.asc("id"));
		}

		String[] sortParts = sort.split(",");

		String property = sortParts[0].trim();

		if (property.isBlank()) {
			throw new InvalidPaginationException("Sort property cannot be empty");
		}

		Sort.Direction direction = Sort.Direction.ASC;

		if (sortParts.length > 1) {
			try {
				direction = Sort.Direction.fromString(sortParts[1].trim());
			} catch (IllegalArgumentException ex) {
				throw new InvalidPaginationException("Sort direction must be 'asc' or 'desc");
			}
		}

		Sort primarySort = Sort.by(direction, property);

		// Add ID as deterministic tie-breaker
		if (!property.equals("id")) {
			primarySort = primarySort.and(Sort.by(Sort.Direction.ASC, "id"));
		}

		return primarySort;
	}
	// ==================== SORT FIELD VALIDATION ====================

	private void validateSortFields(Sort sort) {

		sort.forEach(order -> {

			String property = order.getProperty();

			if (!PaginationConfig.ALLOWED_SORT_FIELDS.contains(property)) {

				throw new InvalidSortFieldException("Sorting by field '" + property + "' is not allowed");
			}
		});
	}

	// ==================== GET EMPLOYEE BY ID ====================

	@Operation(summary = "Get Employee By Id", description = "Retrieves employee details using employee ID.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Employee found", content = @Content(schema = @Schema(implementation = EmployeeResponseDto.class))),
			@ApiResponse(responseCode = "404", description = "Employee not found") })
	@GetMapping("/{id}")
	public ResponseEntity<EmployeeResponseDto> getEmployeeById(@PathVariable Long id) {

		EmployeeResponseDto employee = employeeService.getEmployeeById(id);

		return ResponseEntity.ok(employee);
	}

	// ==================== UPDATE EMPLOYEE ====================

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

	// ==================== DELETE EMPLOYEE ====================

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