package com.goutam.ems.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Generic response wrapper for paginated API results.
 *
 * @param <T> type of data contained in the current page
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResponseDto<T> {

	/**
	 * Records belonging to the current page.
	 */
	private List<T> content;

	/**
	 * Current page number. Starts from 0.
	 */
	private int pageNumber;

	/**
	 * Number of records requested per page.
	 */
	private int pageSize;

	/**
	 * Total number of records matching the query.
	 */
	private long totalElements;

	/**
	 * Total number of available pages.
	 */
	private int totalPages;

	/**
	 * Indicates whether this is the first page.
	 */
	private boolean first;

	/**
	 * Indicates whether this is the last page.
	 */
	private boolean last;
}