package com.tmap.mit.map_viewer.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CountryValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface CountryValid {
        String message() default "잘못 된 country 값이 감지 되었습니다.";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
}
