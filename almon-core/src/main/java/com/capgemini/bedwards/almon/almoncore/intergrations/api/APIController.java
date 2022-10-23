package com.capgemini.bedwards.almon.almoncore.intergrations.api;


import com.capgemini.bedwards.almon.almoncore.exceptions.InvalidPermissionException;
import com.capgemini.bedwards.almon.almoncore.exceptions.NotFoundException;
import com.capgemini.bedwards.almon.almoncore.intergrations.api.error.BadRequestResponse;
import com.capgemini.bedwards.almon.almoncore.intergrations.api.error.ErrorCode;
import com.capgemini.bedwards.almon.almoncore.intergrations.api.error.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract class APIController {
    @ExceptionHandler(value = {NotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse resourceNotFoundException(NotFoundException exception, WebRequest request) {
        return new ErrorResponse(ErrorCode.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorResponse internalServerError(AccessDeniedException exception, WebRequest request) {
        return new ErrorResponse(ErrorCode.UNAUTHORISED_API);
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorResponse authenticationCredentialsNotFoundException(AuthenticationCredentialsNotFoundException exception, WebRequest request) {
        return new ErrorResponse(ErrorCode.UNAUTHORISED_API);
    }


    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception, WebRequest request) {
        return new ErrorResponse(ErrorCode.NOT_FOUND, "A request can not be found for this method at this url");
    }

    @ExceptionHandler(value = {InvalidPermissionException.class})
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorResponse invalidPermissionException(InvalidPermissionException exception, WebRequest request) {
        return new ErrorResponse(ErrorCode.UNAUTHORISED_API, exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public List<BadRequestResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<BadRequestResponse> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.add(new BadRequestResponse(fieldName, errorMessage));
        });
        return errors;
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponse internalServerError(Throwable exception, WebRequest request) {
        log.error("Unexpected error has occurred", exception);
        return new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR);
    }
}
