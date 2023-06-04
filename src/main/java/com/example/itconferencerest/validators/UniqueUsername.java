package com.example.itconferencerest.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = UniqueUsernameValidator.class)
@Retention(RUNTIME)
@Target({ElementType.TYPE})
public @interface UniqueUsername {

    public String message() default "Podany login jest już zajęty";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default{};

}
