package com.goutam.ems.service;

import java.util.List;

import com.goutam.ems.entity.Employee;
import com.goutam.ems.exception.ErrorResponse;

public interface EmployeeService {

    Employee saveEmployee(Employee employee);
	List<Employee> getAllEmployees();
	Employee getEmployeeById(Long id);
	Employee updateEmployee(Long id, Employee employee);

}