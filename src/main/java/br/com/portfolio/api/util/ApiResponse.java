package br.com.portfolio.api.util;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponse<T> {
    private ApiStatus status;
    private int code;
    private String message;
    private T data;
}

