package com.fuzamei.bonuspoint.validation.validator;

import com.fuzamei.bonuspoint.validation.Payword;
import com.fuzamei.bonuspoint.validation.TelePhone;
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
 * @create: 2018-07-20 19:26
 **/
@Component
@RefreshScope
public class TelePhoneValidator implements ConstraintValidator<TelePhone,String> {
    @Value("${reg.telephone}")
    private String telephoneReg;

    @Override
    public void initialize(TelePhone constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        //为空 不校验
        if (value==null){
            return true;
        }
        return Pattern.matches(telephoneReg, String.valueOf(value));
    }
}
