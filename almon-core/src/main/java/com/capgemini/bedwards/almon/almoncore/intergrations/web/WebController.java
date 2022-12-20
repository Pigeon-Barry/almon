package com.capgemini.bedwards.almon.almoncore.intergrations.web;

import com.capgemini.bedwards.almon.almoncore.exceptions.NotFoundException;
import com.capgemini.bedwards.almon.almoncore.intergrations.api.error.BadRequestResponse;
import com.capgemini.bedwards.almon.almoncore.util.SecurityUtil;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract class WebController {


    @ModelAttribute("user")
    public User getUser() {
        return SecurityUtil.getAuthenticatedUser();
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @Order(1)
    public ModelAndView accessDeniedException(AccessDeniedException exception) {
        return new ModelAndView("error/error-unauthorized");
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

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @Order(1)
    public ModelAndView notFoundException(NotFoundException exception) {
        return new ModelAndView("error/error-404");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @Order(1)
    public ModelAndView methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
        return new ModelAndView("error/error-404");
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @Order(9)
    public ModelAndView throwable(Throwable exception) {
        log.error("Unexpected exception thrown", exception);
        return new ModelAndView("error/error-500");
    }
}
