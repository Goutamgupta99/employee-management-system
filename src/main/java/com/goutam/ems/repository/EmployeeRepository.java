package com.goutam.ems.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.goutam.ems.entity.Employee;

/**
 * ============================================================================
 * EmployeeRepository
 * ============================================================================
 *
 * Responsibility: Provides database access operations for Employee entity.
 *
 * Spring Data JPA: - JpaRepository provides built-in CRUD operations. - Custom
 * methods are used for employee-specific queries.
 *
 * Repository Flow:
 *
 * Service ↓ EmployeeRepository ↓ Spring Data JPA ↓ Hibernate ↓ PostgreSQL
 *
 * ============================================================================
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	/**
	 * Checks whether an Employee Code already exists.
	 *
	 * Used during Employee creation and update validation.
	 */
	boolean existsByEmployeeCode(String employeeCode);

	/**
	 * Checks whether an Email already exists.
	 *
	 * Used during Employee creation and update validation.
	 */
	boolean existsByEmail(String email);

	/**
	 * Search Employees using a keyword.
	 *
	 * The keyword is searched across: - Employee Code - First Name - Last Name -
	 * Email - Department - Designation
	 *
	 * Pageable provides: - Pagination - Sorting
	 *
	 * Example:
	 *
	 * keyword = "java"
	 *
	 * This can match: Java Developer Java Engineer Java Department
	 *
	 * =========================================================================
	 */
	@Query("""
			SELECT e FROM Employee e
			WHERE LOWER(e.employeeCode) LIKE LOWER(CONCAT('%', :keyword, '%'))
			   OR LOWER(e.firstName) LIKE LOWER(CONCAT('%', :keyword, '%'))
			   OR LOWER(e.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))
			   OR LOWER(e.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
			   OR LOWER(e.department) LIKE LOWER(CONCAT('%', :keyword, '%'))
			   OR LOWER(e.designation) LIKE LOWER(CONCAT('%', :keyword, '%'))
			""")
	Page<Employee> searchEmployees(@Param("keyword") String keyword, Pageable pageable);
	
	boolean existsByPhone(String phone);
}