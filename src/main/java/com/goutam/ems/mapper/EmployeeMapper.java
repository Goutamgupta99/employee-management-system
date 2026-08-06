package com.goutam.ems.mapper;

import org.springframework.stereotype.Component;

import com.goutam.ems.dto.EmployeeRequestDto;
import com.goutam.ems.dto.EmployeeResponseDto;
import com.goutam.ems.entity.Employee;

/**
 * ============================================================================
 * EmployeeMapper
 * ============================================================================
 *
 * Responsibility: Converts between DTOs and Entity.
 *
 * Why? Keeps mapping logic separate from business logic.
 *
 * Design Pattern: - Mapper Pattern
 *
 * SOLID Principles: ✔ Single Responsibility Principle -> Only performs object
 * conversion.
 *
 * Benefits: - Cleaner Service Layer - Reusable mapping logic - Easy migration
 * to MapStruct later
 * ============================================================================
 */
@Component
public class EmployeeMapper {

	/**
	 * Converts EmployeeRequestDto into Employee Entity.
	 *
	 * Used before saving data into the database.
	 */
	public Employee toEntity(EmployeeRequestDto dto) {

		Employee employee = new Employee();

		employee.setEmployeeCode(dto.getEmployeeCode());
		employee.setFirstName(dto.getFirstName());
		employee.setLastName(dto.getLastName());
		employee.setEmail(dto.getEmail());
		employee.setPhone(dto.getPhone());
		employee.setDepartment(dto.getDepartment());
		employee.setDesignation(dto.getDesignation());
		employee.setSalary(dto.getSalary());
		employee.setJoiningDate(dto.getJoiningDate());
		employee.setStatus(dto.getStatus());

		return employee;
	}

	/**
	 * Converts Employee Entity into EmployeeResponseDto.
	 *
	 * Used before sending response back to client.
	 */
	public EmployeeResponseDto toResponseDto(Employee employee) {

		EmployeeResponseDto dto = new EmployeeResponseDto();

		dto.setId(employee.getId());
		dto.setEmployeeCode(employee.getEmployeeCode());
		dto.setFirstName(employee.getFirstName());
		dto.setLastName(employee.getLastName());
		dto.setEmail(employee.getEmail());
		dto.setPhone(employee.getPhone());
		dto.setDepartment(employee.getDepartment());
		dto.setDesignation(employee.getDesignation());
		dto.setSalary(employee.getSalary());
		dto.setJoiningDate(employee.getJoiningDate());
		dto.setStatus(employee.getStatus());

		return dto;
	}

	public void updateEntity(Employee employee, EmployeeRequestDto dto) {

		employee.setEmployeeCode(dto.getEmployeeCode());
		employee.setFirstName(dto.getFirstName());
		employee.setLastName(dto.getLastName());
		employee.setEmail(dto.getEmail());
		employee.setPhone(dto.getPhone());
		employee.setDepartment(dto.getDepartment());
		employee.setDesignation(dto.getDesignation());
		employee.setSalary(dto.getSalary());
		employee.setJoiningDate(dto.getJoiningDate());
		employee.setStatus(dto.getStatus());
	}
}