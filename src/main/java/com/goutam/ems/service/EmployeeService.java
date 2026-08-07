package com.goutam.ems.service;

import java.util.List;

import com.goutam.ems.dto.ApiResponseDto;
import com.goutam.ems.dto.EmployeeRequestDto;
import com.goutam.ems.dto.EmployeeResponseDto;
import jakarta.validation.Valid;

public interface EmployeeService {

    EmployeeResponseDto saveEmployee(EmployeeRequestDto employee);
	List<EmployeeResponseDto> getAllEmployees();
	EmployeeResponseDto getEmployeeById(Long id);
	EmployeeResponseDto updateEmployee(Long id, @Valid EmployeeRequestDto employee);
	ApiResponseDto deleteEmployee(Long id);

}