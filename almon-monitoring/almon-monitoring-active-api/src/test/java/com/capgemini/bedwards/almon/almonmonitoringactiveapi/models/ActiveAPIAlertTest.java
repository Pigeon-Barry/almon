package com.capgemini.bedwards.almon.almonmonitoringactiveapi.models;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ActiveAPIAlertTest {

    @Test
    public void positive_getLongMessage() {
        ActiveAPIMonitor monitor = ActiveAPIMonitor.builder()
                .method(HttpMethod.DELETE)
                .url("www.almon.com")
                .build();
        ActiveAPIAlert alert = ActiveAPIAlert.builder()
                .monitor(monitor)
                .requestDurationMS(500)
                .responseStatusCode(200)
                .message("message")
                .build();
        assertEquals("Method: " + monitor.getMethod() + "\n" +
                        "URL: " + monitor.getUrl() + "\n" +
                        "Response: " + null + "\n" +
                        "Message: " + alert.getMessage(),
                alert.getLongMessage());
    }

    @Test
    public void positive_getHTMLMessage() {
        ActiveAPIMonitor monitor = ActiveAPIMonitor.builder()
                .method(HttpMethod.DELETE)
                .url("www.almon.com")
                .build();
        ActiveAPIAlert alert = ActiveAPIAlert.builder()
                .monitor(monitor)
                .requestDurationMS(500)
                .responseStatusCode(200)
                .message("message")
                .build();
        assertEquals("<p><h3>Method: </h3>" + monitor.getMethod() + "</p>" +
                        "<p><h3>URL: </h3>" + monitor.getUrl() + "</p>" +
                        "<p><h3>Status Code: </h3>" + alert.getResponseStatusCode() + "</p>" +
                        "<p><h3>Duration: </h3>" + alert.getRequestDurationMS() + " ms</p>" +
                        "<p><h3>Message: </h3>" + alert.getMessage() + "</p>",
                alert.getHTMLMessage());
    }

    @Test
    public void positive_getHTMLMessage_no_message() {
        ActiveAPIMonitor monitor = ActiveAPIMonitor.builder()
                .method(HttpMethod.DELETE)
                .url("www.almon.com")
                .build();
        ActiveAPIAlert alert = ActiveAPIAlert.builder()
                .monitor(monitor)
                .requestDurationMS(500)
                .responseStatusCode(200)
                .build();
        assertEquals("<p><h3>Method: </h3>" + monitor.getMethod() + "</p>" +
                        "<p><h3>URL: </h3>" + monitor.getUrl() + "</p>" +
                        "<p><h3>Status Code: </h3>" + alert.getResponseStatusCode() + "</p>" +
                        "<p><h3>Duration: </h3>" + alert.getRequestDurationMS() + " ms</p>",
                alert.getHTMLMessage());
    }
}
