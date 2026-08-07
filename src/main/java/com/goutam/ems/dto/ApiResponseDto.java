package com.goutam.ems.dto;

import com.goutam.ems.constant.ApiStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseDto {

    private String message;
    private ApiStatus status;
}