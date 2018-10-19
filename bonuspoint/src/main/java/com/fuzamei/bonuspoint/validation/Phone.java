package com.fuzamei.bonuspoint.validation;

import com.fuzamei.bonuspoint.validation.validator.PhoneValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PhoneValidator.class)
@Target({ElementType.METHOD,ElementType.FIELD,ElementType.ANNOTATION_TYPE,ElementType.CONSTRUCTOR,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Phone {
    String message() default "MOBILE_FORMAT_ERROR";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    @Documented
    @Target({ElementType.METHOD,ElementType.FIELD,ElementType.ANNOTATION_TYPE,ElementType.CONSTRUCTOR,ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface List{
        Phone[] value();
    }
}
