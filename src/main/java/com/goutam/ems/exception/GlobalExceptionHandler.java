package com.goutam.ems.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(EmployeeNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleEmployeeNotFound(EmployeeNotFoundException ex,
			HttpServletRequest request) {

		ErrorResponse error = new ErrorResponse();

		error.setTimestamp(LocalDateTime.now());
		error.setStatus(HttpStatus.NOT_FOUND.value());
		error.setError(HttpStatus.NOT_FOUND.getReasonPhrase());
		error.setMessage(ex.getMessage());
		error.setPath(request.getRequestURI());

		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(EmployeeAlreadyExistsException.class)
	public ResponseEntity<ErrorResponse> handleDuplicateEmployeeFound(EmployeeAlreadyExistsException ex,
			HttpServletRequest request) {

		ErrorResponse error = new ErrorResponse();

		error.setTimestamp(LocalDateTime.now());
		error.setStatus(HttpStatus.CONFLICT.value());
		error.setError(HttpStatus.CONFLICT.getReasonPhrase());
		error.setMessage(ex.getMessage());
		error.setPath(request.getRequestURI());

		return new ResponseEntity<>(error, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(InvalidPaginationException.class)
	public ResponseEntity<ErrorResponse> handleInvalidPagination(InvalidPaginationException ex,
			HttpServletRequest request) {

		ErrorResponse error = new ErrorResponse();

		error.setTimestamp(LocalDateTime.now());
		error.setStatus(HttpStatus.BAD_REQUEST.value());
		error.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
		error.setMessage(ex.getMessage());
		error.setPath(request.getRequestURI());

		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(InvalidSortFieldException.class)
	public ResponseEntity<ErrorResponse> handleInvalidSortField(InvalidSortFieldException ex,
			HttpServletRequest request) {

		ErrorResponse error = new ErrorResponse();

		error.setTimestamp(LocalDateTime.now());
		error.setStatus(HttpStatus.BAD_REQUEST.value());
		error.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
		error.setMessage(ex.getMessage());
		error.setPath(request.getRequestURI());

		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex,
			HttpServletRequest request) {

		Map<String, String> validationErrors = new HashMap<>();

		ex.getBindingResult().getFieldErrors()
				.forEach(error -> validationErrors.put(error.getField(), error.getDefaultMessage()));

		Map<String, Object> response = new LinkedHashMap<>();

		response.put("timestamp", LocalDateTime.now());
		response.put("status", HttpStatus.BAD_REQUEST.value());
		response.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
		response.put("message", "Validation Failed");
		response.put("path", request.getRequestURI());
		response.put("validationErrors", validationErrors);

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex,
			HttpServletRequest request) {

		ErrorResponse error = new ErrorResponse();

		error.setTimestamp(LocalDateTime.now());
		error.setStatus(HttpStatus.BAD_REQUEST.value());
		error.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
		error.setMessage("Invalid value for parameter: " + ex.getName());
		error.setPath(request.getRequestURI());

		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, HttpServletRequest request) {

		ErrorResponse error = new ErrorResponse();

		error.setTimestamp(LocalDateTime.now());
		error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		error.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
		error.setMessage("Something went wrong");
		error.setPath(request.getRequestURI());

		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}