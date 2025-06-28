package it.uniroma3.siw.controller.validator;


import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.User;


@Component
public class UserValidator implements Validator {

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;

        if (!user.getName().matches("[a-zA-ZàèéìòùÀÈÉÌÒÙ\\s'-]+")) {
            errors.rejectValue("name", "user.name.invalid", "First name must contain only letters.");
        }

        if (!user.getSurname().matches("[a-zA-ZàèéìòùÀÈÉÌÒÙ\\s'-]+")) {
            errors.rejectValue("surname", "user.surname.invalid", "Last name must contain only letters.");
        }
        

    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }
}
