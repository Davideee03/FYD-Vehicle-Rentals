package it.uniroma3.siw.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import it.uniroma3.siw.model.Site;
import it.uniroma3.siw.service.SiteService;

@Component
public class SiteValidator implements Validator {
	@Autowired
	private SiteService siteService;
	@Override
	public void validate(Object o, Errors errors) {
		Site site = (Site)o;
		if (site.getName()!= null && site.getAddress()!=null && site.getCity()!=null 
				&& siteService.existsByNameAndAddressAndCity(site.getName(), site.getAddress(), site.getCity())) {
			errors.reject("site.duplicate");
		}
	}
	@Override
	public boolean supports(Class<?> aClass) {
		return Site.class.equals(aClass);
	}
}
