package com.sa.restApi.errorHandling;

import org.springframework.http.HttpStatus;

public class ApiError {

    private HttpStatus status;
    private String message;

    public ApiError(HttpStatus status, String message) {
        super();
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
