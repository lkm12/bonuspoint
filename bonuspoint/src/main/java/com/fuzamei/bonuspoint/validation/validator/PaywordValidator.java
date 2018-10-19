package com.fuzamei.bonuspoint.validation.validator;

import com.fuzamei.bonuspoint.validation.Password;
import com.fuzamei.bonuspoint.validation.Payword;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * @program: bonus-point-cloud
 * @description:
 * @author: WangJie
 * @create: 2018-07-20 18:11
 **/
@Component
@RefreshScope
public class PaywordValidator implements ConstraintValidator<Payword,String> {
    @Value("${reg.payword}")
    private String paywordReg;

    @Override
    public void initialize(Payword constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        //为空 不校验
        if (value==null){
            return true;
        }
        return Pattern.matches(paywordReg, String.valueOf(value));
    }
}
