package it.uniroma3.siw.controller.validator;


import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Credentials;

import it.uniroma3.siw.model.User;


@Component
public class CredentialsValidator implements Validator {

	 @Override
	    public void validate(Object o, Errors errors) {
	        Credentials credentials = (Credentials) o;

	        if (credentials.getPassword().length() < 5) {
	            errors.rejectValue("password", "credentials.password.invalid", "Password must be at least 5 characters.");
	        }

	        if (!credentials.getUsername().matches("^[a-zA-Z0-9._-]{3,}$")) {
	            errors.rejectValue("username", "credentials.username.invalid", "Username must be at least 3 characters.");
	        }

	        
	    }
    @Override
    public boolean supports(Class<?> clazz) {
        return Credentials.class.equals(clazz);
    }
}
