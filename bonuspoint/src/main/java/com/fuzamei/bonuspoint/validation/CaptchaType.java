package com.fuzamei.bonuspoint.validation;

import com.fuzamei.bonuspoint.validation.validator.CaptchaTypeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target({ElementType.METHOD,ElementType.ANNOTATION_TYPE,ElementType.CONSTRUCTOR,ElementType.PARAMETER,ElementType.FIELD})
@Constraint(validatedBy = CaptchaTypeValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface CaptchaType {
    String message() default "CAPTCHA_TYPE_WRONG";
    Class<?>[] groups() default {};
    Class<?extends Payload>[] payload() default {};
    @Target({METHOD,FIELD,ANNOTATION_TYPE,CONSTRUCTOR,PARAMETER})
    @Retention(RUNTIME)
    @Documented
    public @interface List {
        CaptchaType[] value();
    }
}
