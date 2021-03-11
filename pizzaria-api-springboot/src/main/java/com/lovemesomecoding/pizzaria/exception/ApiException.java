package com.lovemesomecoding.pizzaria.exception;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author folaukaveinga
 *
 */

@Setter
@Getter
@ToString
public class ApiException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private ApiErrorResponse  error;

    public ApiException() {
    }

    public ApiException(ApiErrorResponse error) {
        super(error.getMessage());
        this.error = error;
    }

    public ApiException(String message) {
        super(message);
        this.error = new ApiErrorResponse(HttpStatus.BAD_REQUEST, message, Arrays.asList());
    }

    public ApiException(String message, List<String> subErrors) {
        super(message);
        this.error = new ApiErrorResponse(HttpStatus.BAD_REQUEST, message, subErrors);
    }

    public ApiException(String message, String... subErrors) {
        super(message);
        this.error = new ApiErrorResponse(HttpStatus.BAD_REQUEST, message, Arrays.asList(subErrors));
    }

    public ApiException(HttpStatus status, String message, List<String> subErrors) {
        super(message);
        this.error = new ApiErrorResponse(status, message, subErrors);
    }

}
