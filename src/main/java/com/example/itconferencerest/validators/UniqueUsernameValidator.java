package com.example.itconferencerest.validators;

import com.example.itconferencerest.ContextProvider;
import com.example.itconferencerest.models.User;
import com.example.itconferencerest.service.ConferenceServiceImpl;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, User> {

    private ConferenceServiceImpl service;

    @Override
    public void initialize(UniqueUsername constraintAnnotation) {
        this.service = (ConferenceServiceImpl) ContextProvider.getBean(ConferenceServiceImpl.class);
    }

    @Override
    public boolean isValid(User value, ConstraintValidatorContext context) {
        return service.checkIfLoginAlreadyExists(value);
    }
}
