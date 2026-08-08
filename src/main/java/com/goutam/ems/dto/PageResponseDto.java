package com.goutam.ems.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ============================================================================
 * PageResponseDto
 * ============================================================================
 *
 * PURPOSE:
 * Custom response structure for paginated APIs.
 *
 * Instead of exposing Spring Data's Page object directly,
 * this DTO provides only the pagination information required
 * by the client.
 *
 * Benefits:
 * - Clean API response
 * - Better control over API contract
 * - Frontend-friendly structure
 * - Hides Spring Data implementation details
 * ============================================================================
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResponseDto<T> {

    private List<T> content;

    private int pageNumber;

    private int pageSize;

    private long totalElements;

    private int totalPages;

    private boolean first;

    private boolean last;
}