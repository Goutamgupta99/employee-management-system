package com.goutam.ems.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.goutam.ems.dto.ApiResponse;
import com.goutam.ems.entity.Employee;
import com.goutam.ems.exception.EmployeeNotFoundException;
import com.goutam.ems.exception.ErrorResponse;
import com.goutam.ems.repository.EmployeeRepository;
import com.goutam.ems.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	private final EmployeeRepository employeeRepository;

	public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}

	@Override
	public Employee saveEmployee(Employee employee) {
		return employeeRepository.save(employee);
	}

	@Override
	public List<Employee> getAllEmployees() {
		return employeeRepository.findAll();
	}

	@Override
	public Employee getEmployeeById(Long id) {

		return employeeRepository.findById(id)
				.orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id : " + id));
	}

	@Override
	public Employee updateEmployee(Long id, Employee employee) {
		Employee existingEmployee = employeeRepository.findById(id)
				.orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id : " + id));
		
		existingEmployee.setDepartment(employee.getDepartment());
		existingEmployee.setDesignation(employee.getDesignation());
		existingEmployee.setEmail(employee.getEmail());
		existingEmployee.setEmployeeCode(employee.getEmployeeCode());
		existingEmployee.setFirstName(employee.getFirstName());
		existingEmployee.setJoiningDate(employee.getJoiningDate());
		existingEmployee.setLastName(employee.getLastName());
		existingEmployee.setPhone(employee.getPhone());
		existingEmployee.setSalary(employee.getSalary());
		existingEmployee.setStatus(employee.getStatus());
		return employeeRepository.save(existingEmployee); 
	}

	@Override
	public ApiResponse deleteEmployee(Long id) {

		Employee existingEmployee = employeeRepository.findById(id)
				.orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id : " + id));

		employeeRepository.delete(existingEmployee);

		ApiResponse response = new ApiResponse();
		response.setStatus("SUCCESS");
		response.setMessage("Employee deleted successfully");

		return response;
	}
	
}