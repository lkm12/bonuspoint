package com.fuzamei.bonuspoint.validation.validator;

import com.fuzamei.bonuspoint.constant.PointInfoConstant;
import com.fuzamei.bonuspoint.validation.PointType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PointTypeValidator implements ConstraintValidator<PointType,Integer>{

    @Override
    public void initialize(PointType constraintAnnotation) {

    }

    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
        if(integer == null){
            return true;
        }
        if(integer != PointInfoConstant.ONE && integer != PointInfoConstant.TWO && integer != PointInfoConstant.THREE){
            return false;
        }
        return true;
    }
}
