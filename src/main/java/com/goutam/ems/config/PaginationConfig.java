package com.goutam.ems.config;

import java.util.Set;

import org.springframework.context.annotation.Configuration;

@Configuration
public class PaginationConfig {

	public static final int DEFAULT_PAGE = 0;

	public static final int DEFAULT_PAGE_SIZE = 10;

	public static final int MAX_PAGE_SIZE = 50;

	public static final Set<String> ALLOWED_SORT_FIELDS = Set.of("firstName", "lastName", "email", "employeeCode",
			"id");
}