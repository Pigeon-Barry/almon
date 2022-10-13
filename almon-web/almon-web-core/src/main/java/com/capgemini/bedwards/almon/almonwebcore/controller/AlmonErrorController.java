package com.capgemini.bedwards.almon.almonwebcore.controller;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
@Slf4j
public class AlmonErrorController implements ErrorController {

    @SneakyThrows
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object statusObj = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        log.error("Error received: " + statusObj + ": from " + request.getAttribute("javax.servlet.forward.request_uri"));
        if (statusObj != null) {
            int statusCode = Integer.parseInt(statusObj.toString());
            HttpStatus status = HttpStatus.valueOf(statusCode);
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
