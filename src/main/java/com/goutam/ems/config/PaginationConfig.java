package com.goutam.ems.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;

@Configuration
public class PaginationConfig {

	private static final int DEFAULT_PAGE_SIZE = 10;
	private static final int MAX_PAGE_SIZE = 50;

	@Bean
	public PageableHandlerMethodArgumentResolverCustomizer pageableCustomizer() {

		return resolver -> {

			resolver.setFallbackPageable(
					PageRequest.of(0, DEFAULT_PAGE_SIZE, Sort.by(Sort.Direction.ASC, "firstName")));

			resolver.setMaxPageSize(MAX_PAGE_SIZE);
		};
	}
}