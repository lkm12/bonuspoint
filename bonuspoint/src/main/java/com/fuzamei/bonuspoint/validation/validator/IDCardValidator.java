package com.fuzamei.bonuspoint.validation.validator;

import com.fuzamei.bonuspoint.util.RegxUtils;
import com.fuzamei.bonuspoint.validation.IDCard;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @program: bonus-point-cloud
 * @description:
 * @author: WangJie
 * @create: 2018-07-13 15:47
 **/
public class IDCardValidator implements ConstraintValidator<IDCard,String> {
    @Override
    public void initialize(IDCard constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 为空 不校验
        if (value==null){
            return true;
        }
        return RegxUtils.isIDNumber(value);
    }
}
