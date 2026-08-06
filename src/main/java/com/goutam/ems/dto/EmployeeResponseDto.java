package com.goutam.ems.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.goutam.ems.enums.EmployeeStatus;

import lombok.Data;

/**
 * ============================================================================
 * EmployeeResponseDto
 * ============================================================================
 *
 * Responsibility:
 * Represents Employee data returned to the client.
 *
 * This DTO is used ONLY for API responses.
 *
 * It hides database implementation details from clients.
 *
 * Design Pattern:
 * - DTO Pattern
 *
 * Future:
 * We can easily add/remove fields here without affecting the database.
 * ============================================================================
 */

@Data
public class EmployeeResponseDto {

    private Long id;

    private String employeeCode;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private String department;

    private String designation;

    private BigDecimal salary;

    private LocalDate joiningDate;

    private EmployeeStatus status;
}