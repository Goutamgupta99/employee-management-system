package com.goutam.ems.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.goutam.ems.enums.EmployeeStatus;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * ============================================================================
 * EmployeeRequestDto
 * ============================================================================
 *
 * Responsibility: Represents incoming Employee data from client.
 *
 * This class is ONLY used for API requests.
 *
 * Validation annotations belong here instead of Entity.
 *
 * Design Pattern: - DTO Pattern
 *
 * Benefits: - Prevents exposing Entity - Separates API layer from Database
 * layer - Makes API changes independent of Database
 * ============================================================================
 */

@Data
public class EmployeeRequestDto {

	@NotBlank(message = "Employee Code is required")
	@Size(min = 3, max = 10, message = "Employee Code must be between 3 and 10 characters")
	private String employeeCode;

	@NotBlank(message = "First Name is required")
	@Size(min = 2, max = 30, message = "First Name must be between 2 and 30 characters")
	private String firstName;

	@NotBlank(message = "Last Name is required")
	@Size(min = 2, max = 30, message = "Last Name must be between 2 and 30 characters")
	private String lastName;

	@Email(message = "Invalid email format")
	@NotBlank(message = "Email is required")
	private String email;

	@NotBlank(message = "Phone number is required")
	@Pattern(regexp = "^[6-9]\\d{9}$", message = "Phone number must be a valid 10-digit Indian mobile number")
	private String phone;

	@NotBlank(message = "Department is required")
	private String department;

	@NotBlank(message = "Designation is required")
	private String designation;

	@NotNull(message = "Salary is required")
	@DecimalMin(value = "0.01", message = "Salary must be greater than zero")
	private BigDecimal salary;

	@NotNull(message = "Joining Date is required")
	@PastOrPresent(message = "Joining Date cannot be in the future")
	private LocalDate joiningDate;

	@NotNull(message = "Status is required")
	private EmployeeStatus status;
}