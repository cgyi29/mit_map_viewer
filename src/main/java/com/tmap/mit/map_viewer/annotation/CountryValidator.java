package com.tmap.mit.map_viewer.annotation;

import com.tmap.mit.map_viewer.cd.ManageLocale;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class CountryValidator implements ConstraintValidator<CountryValid, String> {

    @Override
    public void initialize(CountryValid constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Arrays.stream(ManageLocale.values()).anyMatch(manageLocale -> false);
        return false;
    }
}