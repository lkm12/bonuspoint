package com.fuzamei.bonuspoint.validation;


import com.fuzamei.bonuspoint.validation.validator.IDCardValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


@Documented
@Target({ElementType.METHOD,ElementType.ANNOTATION_TYPE,ElementType.CONSTRUCTOR,ElementType.PARAMETER,ElementType.FIELD})
@Constraint(validatedBy = IDCardValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface IDCard {
    String message() default "WRONG_ID_CARD";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    @Documented
    @Target({ElementType.METHOD,ElementType.FIELD,ElementType.ANNOTATION_TYPE,ElementType.CONSTRUCTOR,ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface List{
        IDCard[] value();
    }
}
