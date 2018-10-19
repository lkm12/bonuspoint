package com.fuzamei.bonuspoint.validation.validator;

import com.fuzamei.bonuspoint.validation.CaptchaType;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @program: bonus-point-cloud
 * @description:
 * @author: WangJie
 * @create: 2018-07-12 11:13
 **/
public class CaptchaTypeValidator implements ConstraintValidator<CaptchaType,Integer> {

    @Value("${captcha.type}")
    private String captchaType;
    @Override
    public void initialize(CaptchaType constraintAnnotation) {

    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        // 为空 不校验
        if (value==null){
            return true;
        }
        String[] types = captchaType.split(",");
        for (String type:types
             ) {
            if (type.equals(String.valueOf(value))){
                return true;
            }
        }
        return false;
    }
}
