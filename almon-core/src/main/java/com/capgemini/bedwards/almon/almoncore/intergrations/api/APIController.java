package com.capgemini.bedwards.almon.almoncore.intergrations.api;


import com.capgemini.bedwards.almon.almoncore.exceptions.InvalidCorrelationIdException;
import com.capgemini.bedwards.almon.almoncore.exceptions.InvalidPermissionException;
import com.capgemini.bedwards.almon.almoncore.exceptions.NotFoundException;
import com.capgemini.bedwards.almon.almoncore.exceptions.ValidationException;
import com.capgemini.bedwards.almon.almoncore.intergrations.api.error.BadRequestResponse;
import com.capgemini.bedwards.almon.almoncore.intergrations.api.error.ErrorCode;
import com.capgemini.bedwards.almon.almoncore.intergrations.api.error.ErrorResponse;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@OpenAPIDefinition(
        info = @Info(title = "ALMON - API",
                version = "1.5.3")
)
public abstract class APIController {


    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse httpMessageNotReadableException(HttpMessageNotReadableException exception) {
        return new ErrorResponse(ErrorCode.INVALID_PAYLOAD);
    }

    @ExceptionHandler(value = {MethodArgumentTypeMismatchException.class})
    @ResponseBody
    public ResponseEntity<ErrorResponse> methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {

        if (exception.getRootCause() != null) {
            if (exception.getRootCause().getClass().equals(NotFoundException.class)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resourceNotFoundException((NotFoundException) exception.getRootCause()));
            }
        }
        return ResponseEntity.internalServerError().body(new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR, exception.getMessage()));
    }

    @ExceptionHandler(value = {NotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse resourceNotFoundException(NotFoundException exception) {
        return new ErrorResponse(ErrorCode.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorResponse accessDeniedException(AccessDeniedException exception) {
        return new ErrorResponse(ErrorCode.UNAUTHORISED_API);
    }

    @ExceptionHandler(value = {InvalidCorrelationIdException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse invalidCorrelationIdException(InvalidCorrelationIdException exception) {
        return new ErrorResponse(ErrorCode.INVALID_CORRELATIONID);
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorResponse authenticationCredentialsNotFoundException(AuthenticationCredentialsNotFoundException exception) {
        return new ErrorResponse(ErrorCode.UNAUTHORISED_API);
    }


    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        return new ErrorResponse(ErrorCode.NOT_FOUND, "A request can not be found for this method at this url");
    }

    @ExceptionHandler(value = {InvalidPermissionException.class})
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorResponse invalidPermissionException(InvalidPermissionException exception) {
        return new ErrorResponse(ErrorCode.UNAUTHORISED_API, exception.getMessage());
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public List<BadRequestResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return handleObjectError(ex.getBindingResult().getAllErrors());
    }

    public List<BadRequestResponse> handleObjectError(List<ObjectError> objectErrors) {
        List<BadRequestResponse> errors = new ArrayList<>();
        objectErrors.forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.add(new BadRequestResponse(fieldName, errorMessage));
        });
        return errors;
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    @ResponseBody
    public List<BadRequestResponse> handleValidationExceptions(ValidationException ex) {
        return handleObjectError(ex.getErrors().getAllErrors());
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponse internalServerError(Throwable exception) {
        log.error("Unexpected error has occurred", exception);
        return new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR);
    }
}
