package com.capgemini.bedwards.almon.almonmonitoringcore.validators;

import com.capgemini.bedwards.almon.almoncore.exceptions.NotFoundException;
import com.capgemini.bedwards.almon.almonmonitoringcore.Monitors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
@Component
public class MonitorTypeExistsValidator implements ConstraintValidator<MonitorTypeExists, String> {
    private final Monitors MONITORS;

    @Autowired
    public MonitorTypeExistsValidator(Monitors monitors) {
        this.MONITORS = monitors;
    }


    @Override
    public void initialize(MonitorTypeExists constraintAnnotation) {

    }

    @Override
    public boolean isValid(String id, ConstraintValidatorContext context) {
        if (id == null || id.length() == 0)
            return true;
        try {
            MONITORS.getMonitorTypeFromId(id);
            return true;
        } catch (NotFoundException e) {
            return false;
        }
    }
}