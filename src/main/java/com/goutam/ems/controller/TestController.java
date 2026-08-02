package com.goutam.ems.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.goutam.ems.dto.ApiResponse;

@RestController
@RequestMapping("/api")
public class TestController {

	@GetMapping("/test")
	public ApiResponse welcome() {

		return new ApiResponse("Welcome to Employee Management System", "SUCCESS");
	}

}
