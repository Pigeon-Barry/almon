package com.capgemini.bedwards.almon.almoncore.intergrations.web;

import com.capgemini.bedwards.almon.almoncore.exceptions.NotFoundException;
import com.capgemini.bedwards.almon.almoncore.util.SecurityUtil;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
public abstract class WebController {


    @ModelAttribute("user")
    public User getUser() {
        return SecurityUtil.getAuthenticatedUser();
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @Order(1)
    public ModelAndView internalServerError(AccessDeniedException exception, WebRequest request) {
        return new ModelAndView("error/error-unauthorized");
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @Order(1)
    public ModelAndView notFoundException(NotFoundException exception, WebRequest request) {
        return new ModelAndView("error/error-404");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @Order(1)
    public ModelAndView notFoundException(MethodArgumentTypeMismatchException exception, WebRequest request) {
        return new ModelAndView("error/error-404");
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @Order(9)
    public ModelAndView throwable(Throwable exception, WebRequest request) {
        log.error("Unexpected exception thrown", exception);
        return new ModelAndView("error/error-500");
    }
}
