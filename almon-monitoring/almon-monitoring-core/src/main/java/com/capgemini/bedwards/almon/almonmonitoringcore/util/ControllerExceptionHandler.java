package com.capgemini.bedwards.almon.almonmonitoringcore.util;

import com.capgemini.bedwards.almon.almoncore.exceptions.NotFoundException;
import com.capgemini.bedwards.almon.almonmonitoringcore.models.ErrorCode;
import com.capgemini.bedwards.almon.almonmonitoringcore.models.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import org.springframework.security.access.AccessDeniedException;

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
}
