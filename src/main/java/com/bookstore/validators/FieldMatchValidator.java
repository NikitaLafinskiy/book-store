package com.bookstore.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;
import org.apache.commons.beanutils.BeanUtils;

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

        try {
            final Object firstFieldValue = BeanUtils.getProperty(value, firstFieldName);
            final Object secondFieldValue = BeanUtils.getProperty(value, secondFieldName);
            toReturn = (firstFieldValue == null && secondFieldValue == null)
                        || (firstFieldValue != null && firstFieldValue.equals(secondFieldValue));
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException("Unable to compare two field names, " + e);
        }

        if (!toReturn) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate("The two fields are not equal.")
                    .addConstraintViolation();
        }

        return toReturn;
    }
}
