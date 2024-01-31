package com.tmap.mit.map_viewer.annotation;

import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE_USE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CountryValidator.class)
public @interface CountryValid {
        String message() default "잘못 된 country 값이 감지 되었습니다.";

        Class[] groups() default {};

        Class[] payload() default {};
}
