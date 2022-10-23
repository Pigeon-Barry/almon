package com.capgemini.bedwards.almon.almoncore.util;

import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import javax.validation.Validation;
import javax.validation.Validator;


public final class ValidatorUtil {

    private ValidatorUtil() {
    }

    private static final Validator javaxValidator = Validation.buildDefaultValidatorFactory().getValidator();
    private static final SpringValidatorAdapter validator = new SpringValidatorAdapter(javaxValidator);


    public static <T> BeanPropertyBindingResult validate(T entry,String objectName)  {
        SpringValidatorAdapter v = new SpringValidatorAdapter(validator);
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(entry, objectName);
        v.validate(entry, errors);
        return errors;
    }

//
//    @SneakyThrows(JsonProcessingException.class)
//    public static <T> T convertAndValidate(ObjectNode objectNode, Class<T> _class) throws MethodArgumentNotValidException {
//        T res = MappingUtil.getObjectMapper().treeToValue(objectNode, _class);
//        ValidatorUtil.validate(res);
//        return res;
//    }
}
