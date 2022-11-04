package com.capgemini.bedwards.almon.almonweb.controller;

import com.capgemini.bedwards.almon.almoncore.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
@Slf4j
public class AlmonErrorController implements ErrorController {


    @RequestMapping("/web/error")
    public Object handleError(HttpServletRequest request, Model model) {
        Integer statusObj = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String requestUri = (String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        Exception exception = (Exception) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
//        String errorMessage = (String) request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        log.error("Error received: " + statusObj + ": from " + requestUri, exception);
        model.addAttribute("user", SecurityUtil.getAuthenticatedUser());
        if (statusObj != null) {
            HttpStatus status = HttpStatus.valueOf(statusObj);
            if (status == HttpStatus.NOT_FOUND) {
                return "/error/error-404";
            } else if (status == HttpStatus.INTERNAL_SERVER_ERROR) {
                return "/error/error-500";
            } else if (status == HttpStatus.UNAUTHORIZED || status == HttpStatus.FORBIDDEN) {
                return "/error/error-unauthorized";
            }
        }
        return "/error/error";
    }
}
