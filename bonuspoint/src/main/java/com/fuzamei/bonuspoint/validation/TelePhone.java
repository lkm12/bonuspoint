package com.fuzamei.bonuspoint.validation;


import com.fuzamei.bonuspoint.validation.validator.TelePhoneValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @program: bonus-point-cloud
 * @description:
 * @author: WangJie
 * @create: 2018-07-20 19:20
 **/

@Documented
@Constraint(validatedBy = {TelePhoneValidator.class })
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
public @interface TelePhone {
    String message() default "TELEPHONE_FORMAT_ERROR";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    @Documented
    @Target({ElementType.METHOD,ElementType.FIELD,ElementType.ANNOTATION_TYPE,ElementType.CONSTRUCTOR,ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface List{
        TelePhone[] value();
    }
}
