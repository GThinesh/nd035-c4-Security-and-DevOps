package com.example.demo.model.requests;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UserValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidatedUser {
    String message() default "The given user request does not fulfills the minimum requirement for user";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
