package com.capgemini.bedwards.almon.almoncore.exceptions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@DisplayName("Given I am testing the ValidationException")
public class ValidationExceptionTest {


  @ParameterizedTest
  @ValueSource(strings = {"My First Message", "My Second Message", "An error has occurred"})
  public void positive_constructor_canCreateWithErrors(String objectName) {
    TestErrors testErrors = new TestErrors(objectName);
    ValidationException validationException = new ValidationException(testErrors);
    assertEquals(objectName, validationException.getMessage());
    assertEquals(testErrors, validationException.getErrors());

  }

  private class TestErrors implements Errors {

    private final String OBJECT_NAME;

    public TestErrors(String objName) {
      this.OBJECT_NAME = objName;
    }

    @Override
    public String toString() {
      return this.getObjectName();
    }

    @Override
    public String getObjectName() {
      return this.OBJECT_NAME;
    }

    @Override
    public void setNestedPath(String nestedPath) {

    }

    @Override
    public String getNestedPath() {
      return null;
    }

    @Override
    public void pushNestedPath(String subPath) {

    }

    @Override
    public void popNestedPath() throws IllegalStateException {

    }

    @Override
    public void reject(String errorCode) {

    }

    @Override
    public void reject(String errorCode, String defaultMessage) {

    }

    @Override
    public void reject(String errorCode, Object[] errorArgs, String defaultMessage) {

    }

    @Override
    public void rejectValue(String field, String errorCode) {

    }

    @Override
    public void rejectValue(String field, String errorCode, String defaultMessage) {

    }

    @Override
    public void rejectValue(String field, String errorCode, Object[] errorArgs,
                            String defaultMessage) {

    }

    @Override
    public void addAllErrors(Errors errors) {

    }

    @Override
    public boolean hasErrors() {
      return false;
    }

    @Override
    public int getErrorCount() {
      return 0;
    }

    @Override
    public List<ObjectError> getAllErrors() {
      return null;
    }

    @Override
    public boolean hasGlobalErrors() {
      return false;
    }

    @Override
    public int getGlobalErrorCount() {
      return 0;
    }

    @Override
    public List<ObjectError> getGlobalErrors() {
      return null;
    }

    @Override
    public ObjectError getGlobalError() {
      return null;
    }

    @Override
    public boolean hasFieldErrors() {
      return false;
    }

    @Override
    public int getFieldErrorCount() {
      return 0;
    }

    @Override
    public List<FieldError> getFieldErrors() {
      return null;
    }

    @Override
    public FieldError getFieldError() {
      return null;
    }

    @Override
    public boolean hasFieldErrors(String field) {
      return false;
    }

    @Override
    public int getFieldErrorCount(String field) {
      return 0;
    }

    @Override
    public List<FieldError> getFieldErrors(String field) {
      return null;
    }

    @Override
    public FieldError getFieldError(String field) {
      return null;
    }

    @Override
    public Object getFieldValue(String field) {
      return null;
    }

    @Override
    public Class<?> getFieldType(String field) {
      return null;
    }
  }
}
