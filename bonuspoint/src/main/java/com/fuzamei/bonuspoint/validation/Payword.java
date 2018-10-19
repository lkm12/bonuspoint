package com.fuzamei.bonuspoint.validation;


import com.fuzamei.bonuspoint.validation.validator.PaywordValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = {PaywordValidator.class })
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
public @interface Payword {
    String message() default "PAYWORD_FORMAT_WRONG";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    @Documented
    @Target({ElementType.METHOD,ElementType.FIELD,ElementType.ANNOTATION_TYPE,ElementType.CONSTRUCTOR,ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface List{
        Payword[] value();
    }

}
