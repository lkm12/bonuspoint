package com.fuzamei.bonuspoint.validation.validator;


import com.fuzamei.bonuspoint.validation.Password;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * @author wangtao
 * @create 2018/5/30
 */
@Component
@RefreshScope
public class PasswordValidator implements ConstraintValidator<Password, String> {

    @Value("${reg.password}")
    private String passwordReg;

    @Override
    public void initialize(Password constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 为空不校验
        if (value==null){
            return true;
        }
        return Pattern.matches(passwordReg, String.valueOf(value));
    }
}
