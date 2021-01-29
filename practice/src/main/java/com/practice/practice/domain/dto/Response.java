package com.practice.practice.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Response {
    private String status;
    private String message;
}
