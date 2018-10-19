package com.fuzamei.bonuspoint.validation.validator;

import com.fuzamei.bonuspoint.validation.Captcha;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * @program: bonus-point-cloud
 * @description:
 * @author: WangJie
 * @create: 2018-07-12 10:21
 **/
public class CaptchaValidator implements ConstraintValidator<Captcha , String> {

    @Value("${reg.captcha}")
    private String captchaReg;
    @Override
    public void initialize(Captcha constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 为空 不校验
        if (value==null){
            return true;
        }
        return Pattern.matches(captchaReg,value);
    }
}
