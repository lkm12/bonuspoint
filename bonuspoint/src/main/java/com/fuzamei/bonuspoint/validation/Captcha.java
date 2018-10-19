package com.fuzamei.bonuspoint.validation;


import com.fuzamei.bonuspoint.validation.validator.CaptchaValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = {CaptchaValidator.class})
@Target({METHOD,FIELD,ANNOTATION_TYPE,CONSTRUCTOR,PARAMETER})
@Retention(RUNTIME)
public @interface Captcha {
    String message() default "CAPTCHA_WRONG";
    Class<?>[] groups() default {};
    Class<?extends Payload>[] payload() default {};
    @Target({METHOD,FIELD,ANNOTATION_TYPE,CONSTRUCTOR,PARAMETER})
    @Retention(RUNTIME)
    @Documented
    public @interface List {
        Captcha[] value();
    }
}
