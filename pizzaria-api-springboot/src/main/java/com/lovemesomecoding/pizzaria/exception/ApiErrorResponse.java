package com.lovemesomecoding.pizzaria.exception;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.lovemesomecoding.pizzaria.dto.ApiDefaultResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author folaukaveinga
 *
 */

@Setter
@Getter
@ToString
@JsonInclude(value = Include.NON_NULL)
public class ApiErrorResponse extends ApiDefaultResponseDTO {

    public static final String DEFAULT_MSG = "Something went wrong. Please try again.";

    private String             debugMessage;

    private List<String>       errors;

    /**
     * Internal use only, not visible to clients
     */
    @JsonIgnore
    private HttpStatus         status;

    /**
     * Internal use only, not visible to clients
     */
    @JsonIgnore
    private Date               timestamp;

    public ApiErrorResponse() {
        this(null, null);
    }

    public ApiErrorResponse(String message) {
        this(null, message);
    }

    public ApiErrorResponse(HttpStatus status, String message) {
        this(status, message, Arrays.asList());
    }

    public ApiErrorResponse(HttpStatus status, String message, String... subErrors) {
        this(status, message, null, Arrays.asList(subErrors));
    }

    public ApiErrorResponse(HttpStatus status, String message, List<String> subErrors) {
        this(status, message, null, subErrors);
    }

    public ApiErrorResponse(HttpStatus status, String message, String debugMessage, List<String> subErrors) {
        this(status, message, debugMessage, subErrors, null);
    }

    public ApiErrorResponse(HttpStatus status, String message, String debugMessage, List<String> subErrors, Throwable ex) {
        this.status = status;
        this.message = message;
        this.timestamp = new Date();
        this.errors = subErrors;
        this.debugMessage = (debugMessage != null) ? debugMessage : (ex != null) ? ex.getLocalizedMessage() : null;
    }

    public void addSubError(String subError) {
        if (this.errors == null) {
            this.errors = new ArrayList<>();
        }
        this.errors.add(subError);
    }

}
