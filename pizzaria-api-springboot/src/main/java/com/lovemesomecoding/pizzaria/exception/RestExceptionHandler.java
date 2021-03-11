package com.lovemesomecoding.pizzaria.exception;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.persistence.PersistenceException;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.TransientPropertyValueException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * 
     * @param ex
     * @return
     */
    @ExceptionHandler(ApiException.class)
    protected ResponseEntity<ApiErrorResponse> handleGetException(ApiException ex) {
        log.info("handleApiException(..)");
        return buildResponseEntity(ex.getError());
    }

    /**
     * Access Denied Exception
     * 
     * @param ex
     * @return
     */
    // @ExceptionHandler(AccessDeniedException.class)
    // protected ResponseEntity<ApiErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
    // log.info("handleAccessDeniedException(..)");
    // ApiErrorResponse apiError = new ApiErrorResponse(HttpStatus.FORBIDDEN, "You do not have access to this api.",
    // null, Collections.singletonList(ex.getLocalizedMessage()));
    // return buildResponseEntity(apiError);
    // }

    /**
     * Fall back exception handler - if all fails, I WILL CATCH YOU!
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ApiErrorResponse> handleOther(Exception ex) {
        log.error("handleOther(..)", ex);
        String errorMessage = null != ex.getLocalizedMessage() ? ex.getLocalizedMessage() : ApiErrorResponse.DEFAULT_MSG;

        StringBuilder errorString = new StringBuilder();
        try {
            errorString.append(ex.getClass().getName()).append(" ");
            StackTraceElement[] traceArray = ex.getStackTrace();
            if (null != traceArray && 0 < traceArray.length) {
                for (StackTraceElement stackTraceElement : traceArray) {
                    errorString.append(stackTraceElement.toString());
                    if (StringUtils.contains(stackTraceElement.getClassName(), "sidecarhealth")) {
                        break;
                    } else {
                        errorString.append(",");
                    }
                }
            }
        } catch (Exception e) {
            log.warn("ignore");
        }

        ApiErrorResponse apiError = new ApiErrorResponse(HttpStatus.BAD_REQUEST, ApiErrorResponse.DEFAULT_MSG, errorMessage, Collections.singletonList(errorString.toString()));
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(JsonMappingException.class)
    protected ResponseEntity<ApiErrorResponse> handleJsonMappingException(JsonMappingException ex) {
        log.info("handleJsonMappingException(..)");
        String errorMessage = null != ex.getLocalizedMessage() ? ex.getLocalizedMessage() : ApiErrorResponse.DEFAULT_MSG;
        ApiErrorResponse apiError = new ApiErrorResponse(HttpStatus.BAD_REQUEST, ApiErrorResponse.DEFAULT_MSG, errorMessage, Collections.singletonList(ex.getLocalizedMessage()));
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(JsonProcessingException.class)
    protected ResponseEntity<ApiErrorResponse> handleJsonProcessingException(JsonProcessingException ex) {
        log.info("handleJsonProcessingException(..)");
        String errorMessage = null != ex.getLocalizedMessage() ? ex.getLocalizedMessage() : ApiErrorResponse.DEFAULT_MSG;
        ApiErrorResponse apiError = new ApiErrorResponse(HttpStatus.BAD_REQUEST, ApiErrorResponse.DEFAULT_MSG, errorMessage, Collections.singletonList(ex.getLocalizedMessage()));
        return buildResponseEntity(apiError);
    }

    /**
     * 
     * @param ex
     * @return
     */
    @ExceptionHandler(PersistenceException.class)
    protected ResponseEntity<ApiErrorResponse> handleDatabaseException(PersistenceException ex) {
        log.info("handleDatabaseException(..)");
        String errorMessage = null != ex.getLocalizedMessage() ? ex.getLocalizedMessage() : ApiErrorResponse.DEFAULT_MSG;
        ApiErrorResponse apiError = new ApiErrorResponse(HttpStatus.BAD_REQUEST, ApiErrorResponse.DEFAULT_MSG, errorMessage, Collections.singletonList(ex.getLocalizedMessage()));
        return buildResponseEntity(apiError);
    }

    /**
     * 
     * @param ex
     * @return
     */
    @ExceptionHandler(TransientPropertyValueException.class)
    protected ResponseEntity<ApiErrorResponse> handleTransientPropertyValueException(TransientPropertyValueException ex) {
        log.info("handleTransientPropertyValueException(..)");
        String errorMessage = null != ex.getLocalizedMessage() ? ex.getLocalizedMessage() : ApiErrorResponse.DEFAULT_MSG;
        ApiErrorResponse apiError = new ApiErrorResponse(HttpStatus.BAD_REQUEST, ApiErrorResponse.DEFAULT_MSG, errorMessage, Collections.singletonList(ex.getLocalizedMessage()));
        return buildResponseEntity(apiError);
    }

    /**
     * Stripe Exception
     * 
     * @param ex
     * @return
     */
    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<ApiErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.info("handleIllegalArgumentException(..)");
        String errorMessage = null != ex.getLocalizedMessage() ? ex.getLocalizedMessage() : ApiErrorResponse.DEFAULT_MSG;
        ApiErrorResponse apiError = new ApiErrorResponse(HttpStatus.BAD_REQUEST, ApiErrorResponse.DEFAULT_MSG, errorMessage, Collections.singletonList(ex.getMessage()));
        return buildResponseEntity(apiError);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info("handleHttpMediaTypeNotAcceptable(..)");
        String errorMessage = null != ex.getLocalizedMessage() ? ex.getLocalizedMessage() : ApiErrorResponse.DEFAULT_MSG;
        ApiErrorResponse apiError = new ApiErrorResponse(HttpStatus.BAD_REQUEST, ApiErrorResponse.DEFAULT_MSG, errorMessage, Collections.singletonList(errorMessage));
        return new ResponseEntity<>(apiError, status);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info("handleHttpMediaTypeNotAcceptable(..)");
        String errorMessage = null != ex.getLocalizedMessage() ? ex.getLocalizedMessage() : ApiErrorResponse.DEFAULT_MSG;
        ApiErrorResponse apiError = new ApiErrorResponse(HttpStatus.BAD_REQUEST, ApiErrorResponse.DEFAULT_MSG, errorMessage, Collections.singletonList(errorMessage));
        return new ResponseEntity<>(apiError, status);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info("handleExceptionInternal(..)");
        String errorMessage = null != ex.getLocalizedMessage() ? ex.getLocalizedMessage() : ApiErrorResponse.DEFAULT_MSG;
        ApiErrorResponse apiError = new ApiErrorResponse(HttpStatus.BAD_REQUEST, ApiErrorResponse.DEFAULT_MSG, errorMessage, Collections.singletonList(errorMessage));
        return new ResponseEntity<>(apiError, status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("handleHttpMessageNotReadable(..)", ex);
        String errorMessage = null != ex.getLocalizedMessage() ? ex.getLocalizedMessage() : ApiErrorResponse.DEFAULT_MSG;
        ApiErrorResponse apiError = new ApiErrorResponse(HttpStatus.BAD_REQUEST, ApiErrorResponse.DEFAULT_MSG, errorMessage,
                Arrays.asList("Invalid request", "Invalid Payload", ex.getLocalizedMessage()));
        return new ResponseEntity<>(apiError, status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info("handleHttpMediaTypeNotSupported(..)");
        log.error(ex.getMessage());
        String errorMessage = null != ex.getLocalizedMessage() ? ex.getLocalizedMessage() : ApiErrorResponse.DEFAULT_MSG;
        ApiErrorResponse apiError = new ApiErrorResponse(HttpStatus.BAD_REQUEST, ApiErrorResponse.DEFAULT_MSG, errorMessage, Collections.singletonList(errorMessage));
        return new ResponseEntity<>(apiError, status);
    }

    // @Override
    // protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders
    // headers, HttpStatus status, WebRequest request) {
    // log.debug("handleMethodArgumentNotValid(..)");
    //
    // List<String> errors = ValidationUtils.getErrors(ex.getBindingResult());
    // log.debug("errors: {}", errors);
    // StringBuilder bindingErrors;
    // if (null != errors && !errors.isEmpty()) {
    // bindingErrors = new StringBuilder();
    // for (String error : errors) {
    // bindingErrors.append(error).append("\n");
    // }
    // }
    //
    // ApiErrorResponse apiError = new ApiErrorResponse(HttpStatus.BAD_REQUEST, "Something went wrong", errors);
    //
    // return new ResponseEntity<>(apiError, status);
    // }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info("handleMissingPathVariable(..)");
        log.error(ex.getMessage());
        String errorMessage = null != ex.getLocalizedMessage() ? ex.getLocalizedMessage() : ApiErrorResponse.DEFAULT_MSG;
        ApiErrorResponse apiError = new ApiErrorResponse(HttpStatus.BAD_REQUEST, errorMessage, errorMessage, Collections.singletonList(errorMessage));
        return new ResponseEntity<>(apiError, status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info("handleHttpRequestMethodNotSupported(..)");
        log.error(ex.getMessage());
        String errorMessage = null != ex.getLocalizedMessage() ? ex.getLocalizedMessage() : ApiErrorResponse.DEFAULT_MSG;
        ApiErrorResponse apiError = new ApiErrorResponse(HttpStatus.BAD_REQUEST, errorMessage, errorMessage, Collections.singletonList(errorMessage));
        return new ResponseEntity<>(apiError, status);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info("handleTypeMismatch(..)");
        log.error(ex.getMessage());
        String errorMessage = null != ex.getLocalizedMessage() ? ex.getLocalizedMessage() : ApiErrorResponse.DEFAULT_MSG;
        ApiErrorResponse apiError = new ApiErrorResponse(HttpStatus.BAD_REQUEST, errorMessage, errorMessage, Collections.singletonList(errorMessage));
        return new ResponseEntity<>(apiError, status);
    }

    /**
     * Get - error code and string of message Delete - error code and string of message Post, Put, Patch - error code
     * and list of messages
     * 
     * @param apiError
     * @return ResponseEntity
     */
    private ResponseEntity<ApiErrorResponse> buildResponseEntity(ApiErrorResponse apiErrorResponse) {
        log.debug("Friendly Msg: {}", apiErrorResponse.getMessage());
        String errors = StringUtils.join(apiErrorResponse.getErrors(), ",");
        log.debug("API response detailed message: {}", errors);
        return new ResponseEntity<>(apiErrorResponse, null != apiErrorResponse.getStatus() ? apiErrorResponse.getStatus() : HttpStatus.BAD_REQUEST);
    }

}
