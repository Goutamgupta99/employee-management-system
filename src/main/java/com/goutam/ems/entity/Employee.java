package com.goutam.ems.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.goutam.ems.enums.EmployeeStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

		@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id; 
	    
	    // Long is a wrapper class.
	    // It allows null before the entity is persisted.
	    // The database generates the ID after insertion.
	    
	    @NotBlank(message = "Employee Code is required")
	    @Column(name = "employee_code", nullable = false, unique = true, length = 20)
	    private String employeeCode;
	    
	    @NotBlank(message = "First Name is required")
	    @Column(name = "first_name", nullable = false, length = 50)
	    private String firstName;

	    @NotBlank(message = "Last Name is required")
	    @Column(name = "last_name", nullable = false, length = 50)
	    private String lastName;
	    
	    @Email(message = "Invalid email format")
	    @NotBlank(message = "Email is required")
	    @Column(nullable = false, unique = true, length = 100)
	    private String email;
	    
	    @NotBlank(message = "Phone number is required")
	    @Column(nullable = false, unique = true, length = 10)
	    private String phone;
	    
	    @NotBlank(message = "Department is required")
	    @Column(nullable = false)
	    private String department;
	    
	    @NotBlank(message = "Designation is required")
	    @Column(nullable = false)
	    private String designation;
	    
	    @NotNull(message = "Salary is required")
	    @Column(nullable = false, precision = 10, scale = 2)
	    private BigDecimal salary;
	    
	    @NotNull(message = "Joining Date is required")
	    @Column(name = "joining_date", nullable = false)
	    private LocalDate joiningDate;
	    
	    @Enumerated(EnumType.STRING)
	    @Column(nullable = false)
	    private EmployeeStatus status;
}
