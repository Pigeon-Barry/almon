package com.capgemini.bedwards.almon.almondatastore.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ReflectionUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ValidationException;
import java.lang.reflect.Field;

@Slf4j
public class FieldsMatchValidator implements ConstraintValidator<FieldsMatch, Object> {

    private String[] fields;
    private String message;

    @Override
    public void initialize(FieldsMatch constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.fields = constraintAnnotation.fields();
        this.message = constraintAnnotation.message();
        if (this.fields == null || this.fields.length < 2)
            throw new ValidationException("Fields must contain at least 2 entries");
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            Object firstVal = null;
            boolean valid = true;
            for (String field : fields) {
                Object valueToMatch = getFieldValue(value, field);
                if (firstVal == null)
                    firstVal = valueToMatch;
                else {
                    boolean res = firstVal.equals(valueToMatch);
                    if (!res) {
                        valid = false;
                        context.buildConstraintViolationWithTemplate(this.message)
                                .addPropertyNode(field)
                                .addConstraintViolation();
                    }
                }
            }
            return valid;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private Object getFieldValue(Object parent, String fieldName) throws IllegalAccessException {
        Field field = ReflectionUtils.findField(parent.getClass(), fieldName);
        if (field == null)
            throw new ValidationException("Field with name: " + fieldName + " not found in class " + parent.getClass());
        field.setAccessible(true);
        return field.get(parent);
    }
}