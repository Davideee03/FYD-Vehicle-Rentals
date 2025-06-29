package it.uniroma3.siw.controller.validator;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Credentials;

import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.CredentialsService;


@Component
public class CredentialsValidator implements Validator {
	
	@Autowired
	private CredentialsService credentialsService;
	

	 @Override
	    public void validate(Object o, Errors errors) {
	        Credentials credentials = (Credentials) o;

	        if (credentials.getPassword().length() < 5) {
	            errors.rejectValue("password", "credentials.password.invalid", "Password must be at least 5 characters.");
	        }

	        if (!credentials.getUsername().matches("^[a-zA-Z0-9._-]{3,}$")) {
	            errors.rejectValue("username", "credentials.username.invalid", "Username must be at least 3 characters.");
	        }
	        
	        if(credentialsService.getCredentials(credentials.getUsername()) != null) {
				errors.rejectValue("username", "credentials.username.invalid","Username already used. Choose another one");
			}

	        
	    }
    @Override
    public boolean supports(Class<?> clazz) {
        return Credentials.class.equals(clazz);
    }
}
