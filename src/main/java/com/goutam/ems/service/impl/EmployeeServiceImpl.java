package com.goutam.ems.service.impl;

import org.springframework.stereotype.Service;

import com.goutam.ems.entity.Employee;
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
}