package com.capgemini.bedwards.almon.almoncore.util;

import com.capgemini.bedwards.almon.almoncore.exceptions.ValidationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.SneakyThrows;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import javax.validation.Validation;
import javax.validation.Validator;


public class ValidatorUtil {

    private static final Validator javaxValidator = Validation.buildDefaultValidatorFactory().getValidator();
    private static final SpringValidatorAdapter validator = new SpringValidatorAdapter(javaxValidator);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Object validate(Object entry) {
        Errors errors = new BeanPropertyBindingResult(entry, entry.getClass().getName());
        validator.validate(entry, errors);
        if (errors.hasErrors()) {
            throw new ValidationException(errors);
        }
        return entry;
    }

    @SneakyThrows
    public static <T> T convertAndValidate(ObjectNode objectNode, Class<T> _class) {
        T res = objectMapper.treeToValue(objectNode, _class);
        ValidatorUtil.validate(res);
        return res;
    }
}
