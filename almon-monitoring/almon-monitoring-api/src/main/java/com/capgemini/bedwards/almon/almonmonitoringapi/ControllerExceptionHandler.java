package com.capgemini.bedwards.almon.almonmonitoringapi;

import com.capgemini.bedwards.almon.almoncore.exceptions.InvalidPermissionException;
import com.capgemini.bedwards.almon.almoncore.exceptions.NotFoundException;
import com.capgemini.bedwards.almon.almonmonitoringapi.error.BadRequestResponse;
import com.capgemini.bedwards.almon.almonmonitoringapi.error.ErrorCode;
import com.capgemini.bedwards.almon.almonmonitoringapi.error.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

    @ExceptionHandler(value = {NotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorResponse resourceNotFoundException(NotFoundException exception, WebRequest request) {
        return new ErrorResponse(ErrorCode.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ErrorResponse internalServerError(AccessDeniedException exception, WebRequest request) {
        return new ErrorResponse(ErrorCode.UNAUTHORISED);
    }

    @ExceptionHandler(Exception.class)
    public ErrorResponse internalServerError(Exception exception, WebRequest request) {
        log.error("Unexpected error has occurred", exception);
        return new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {InvalidPermissionException.class})
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ErrorResponse invalidPermissionException(NotFoundException exception, WebRequest request) {
        return new ErrorResponse(ErrorCode.UNAUTHORISED, exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<BadRequestResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<BadRequestResponse> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.add(new BadRequestResponse(fieldName, errorMessage));
        });
        return errors;
    }
}
