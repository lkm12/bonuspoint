package com.fuzamei.bonuspoint.validation.validator;

import com.fuzamei.bonuspoint.validation.Phone;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * @program: bonus-point-cloud
 * @description:
 * @author: WangJie
 * @create: 2018-07-12 10:40
 **/
public class PhoneValidator implements ConstraintValidator<Phone,String> {
    @Value("${reg.phone}")
    private String phoneReg;
    @Override
    public void initialize(Phone constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        //为空 不校验
        if (value==null){
            return true;
        }
        return Pattern.matches(phoneReg,value);
    }
}
