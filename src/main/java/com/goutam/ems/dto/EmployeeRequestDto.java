package com.goutam.ems.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.goutam.ems.enums.EmployeeStatus;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * ============================================================================
 * EmployeeRequestDto
 * ============================================================================
 *
 * Responsibility:
 * Represents incoming Employee data from client.
 *
 * This class is ONLY used for API requests.
 *
 * Validation annotations belong here instead of Entity.
 *
 * Design Pattern:
 * - DTO Pattern
 *
 * Benefits:
 * - Prevents exposing Entity
 * - Separates API layer from Database layer
 * - Makes API changes independent of Database
 * ============================================================================
 */

@Data
public class EmployeeRequestDto {

    @NotBlank(message = "Employee Code is required")
    private String employeeCode;

    @NotBlank(message = "First Name is required")
    private String firstName;

    @NotBlank(message = "Last Name is required")
    private String lastName;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Phone number is required")
    private String phone;

    @NotBlank(message = "Department is required")
    private String department;

    @NotBlank(message = "Designation is required")
    private String designation;

    @NotNull(message = "Salary is required")
    private BigDecimal salary;

    @NotNull(message = "Joining Date is required")
    private LocalDate joiningDate;

    @NotNull(message = "Status is required")
    private EmployeeStatus status;
}