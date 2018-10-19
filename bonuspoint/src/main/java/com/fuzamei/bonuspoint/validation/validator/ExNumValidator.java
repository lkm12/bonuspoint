package com.fuzamei.bonuspoint.validation.validator;

import com.fuzamei.bonuspoint.validation.ExNum;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

public class ExNumValidator implements ConstraintValidator<ExNum,BigDecimal> {

    @Override
    public void initialize(ExNum constraintAnnotation) {

    }

    @Override
    public boolean isValid(BigDecimal bigDecimal, ConstraintValidatorContext constraintValidatorContext) {
        if(bigDecimal == null){
            return true;
        }
        if(bigDecimal.compareTo(BigDecimal.ZERO) == -1){
            return false;
        }
        return true;
    }
}
