package com.example.demo.model.requests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserValidator implements ConstraintValidator<ValidatedUser, CreateUserRequest> {
    private final Logger logger = LoggerFactory.getLogger(UserValidator.class);
    @Value("${app.password.min-length}")
    private int minLength;

    @Override
    public void initialize(ValidatedUser constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(CreateUserRequest createUserRequest, ConstraintValidatorContext constraintValidatorContext) {
        if (createUserRequest.getPassword() == null || createUserRequest.getConfirmPassword() == null || createUserRequest.getUsername() == null) {
            logger.warn("User validating failed. Null is passed for one of mandatory property");
            return false;
        }
        if (createUserRequest.getPassword().length() < minLength) {
            logger.warn("User password does not meet the minimum length requirement");
            return false;
        }
        boolean same = createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword());
        if (!same) {
            logger.warn("Both passwords should be same");
        }
        return same;
    }
}
