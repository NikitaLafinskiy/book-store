package com.bookstore.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;
import org.springframework.beans.BeanWrapperImpl;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {
    private String firstFieldName;
    private String secondFieldName;

    @Override
    public void initialize(FieldMatch constraintAnnotation) {
        firstFieldName = constraintAnnotation.first();
        secondFieldName = constraintAnnotation.second();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        boolean toReturn;

        final Object firstFieldValue = new BeanWrapperImpl(value)
                .getPropertyValue(firstFieldName);
        final Object secondFieldValue = new BeanWrapperImpl(value)
                .getPropertyValue(secondFieldName);
        toReturn = Objects.equals(firstFieldValue, secondFieldValue);

        if (!toReturn) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate("The two fields are not equal.")
                    .addConstraintViolation();
        }

        return toReturn;
    }
}
