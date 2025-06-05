package it.uniroma3.siw.controller;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.model.Credentials;

import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.SiteService;
import it.uniroma3.siw.service.UserService;

@Controller
public class AuthenticationController {
	
	@Autowired
	private CredentialsService credentialsService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private SiteService siteService;

	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@GetMapping(value = "/register") 
	public String showRegisterForm (Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()
		        && !(authentication instanceof AnonymousAuthenticationToken)) {
			return "redirect:/";
		}
		model.addAttribute("user", new User());
		model.addAttribute("credentials", new Credentials());
		return "formRegisterUser";
	}
	
	@GetMapping(value = "/login") 
	public String showLoginForm (Model model, @RequestParam(value = "error", required = false) String error) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()
		        && !(authentication instanceof AnonymousAuthenticationToken)) {
			return "redirect:/";
		}
		if (error != null) 
			model.addAttribute("msgError", "Username o Password sbagliati");
		return "formLogin";
	}

	@GetMapping(value = "/") 
	public String index(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			model.addAttribute("cityList", this.siteService.getCityList());
	        return "homepage.html";
		}
		else {		
			UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
			if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
				return "/admin/homepageAdmin.html";
			}
		}
        return "homepage.html";
	}
		
    @GetMapping(value = "/success")
    public String defaultAfterLogin(Model model) {
        
    	UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
    	if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
            return "/admin/homePageAdmin.html";
        }
        return "homepage.html";
    }

	@PostMapping(value = { "/register" })
    public String registerUser(@Valid @ModelAttribute("user") User user,
                 BindingResult userBindingResult, @Valid
                 @ModelAttribute("credentials") Credentials credentials,
                 BindingResult credentialsBindingResult,
                 Model model, @RequestParam("confirmPassword") String psw) {
		if(!credentials.getPassword().equals(psw)) {
			model.addAttribute("passwordError", "Error, the password are not the same");
			 return "formRegisterUser";
		}

        // se user e credential hanno entrambi contenuti validi, memorizza User e the Credentials nel DB
        if(!userBindingResult.hasErrors() && ! credentialsBindingResult.hasErrors()) {
            credentials.setUser(user);
            credentialsService.saveCredentials(credentials);
            model.addAttribute("user", user);
			/*
			 * Authentication authentication = new UsernamePasswordAuthenticationToken(
			 * credentials.getUsername(), psw // <- la password in chiaro inserita
			 * dall’utente, non quella codificata ); Authentication auth =
			 * authenticationManager.authenticate(authentication);
			 * SecurityContextHolder.getContext().setAuthentication(auth);
			 */
            
            return "redirect:/login";
        }
        return "registerUser";
    }
	
	@GetMapping("/profile") 
	public String getProfile(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    UserDetails userDetails = (UserDetails) auth.getPrincipal();

	    
	    Credentials credenziali = credentialsService.getCredentials(userDetails.getUsername());
	    User utente = credenziali.getUser();
	    
	    model.addAttribute("user", utente);
	    model.addAttribute("credentials", credenziali);

		return "profile.html";
	}
	
	// modifica profilo 
	@GetMapping("/profile/editProfile")
	public String editProfile(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    UserDetails userDetails = (UserDetails) auth.getPrincipal();

	    
	    Credentials credenziali = credentialsService.getCredentials(userDetails.getUsername());
	    User utente = credenziali.getUser();
		model.addAttribute("credentials", credenziali);
		model.addAttribute("user", utente);
		
		return "editProfile.html";
	}
	
	@PostMapping("/profile/editProfile")
	public String saveEditProfile(Model model, User user, Credentials credentials) {

	    credentials.setUser(user);
	    this.userService.saveUser(user);
	   
        // le credenziali non vengono cambiate in questo caso credentialsService.saveCredentials(credentials);
        model.addAttribute("success", "Success! You have modified your Profile");

	    return "redirect:/profile";
	}
	
	@GetMapping("/profile/modifyPassword") 
	public String editPsw(Model model) {
		return "editPsw.html";
	}
	
	@PostMapping("/profile/modifyPassword") 
	public String modPsw(Model model, @RequestParam("oldPsw") String oldPsw, @RequestParam("newPsw") String newPsw,
							@RequestParam("confirmPsw") String confirmPsw) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    UserDetails userDetails = (UserDetails) auth.getPrincipal();
	    Credentials credenziali = credentialsService.getCredentials(userDetails.getUsername());
	    User user = credenziali.getUser();
	    
	    if (!passwordEncoder.matches(oldPsw, credenziali.getPassword())) {
	    	model.addAttribute("msgError", "Error, wrong Password");
	    	return "editPsw.html";
	    }
	    
	    if (!newPsw.equals(confirmPsw))  {
	    	model.addAttribute("msgError", "Password not the same");
	
	    	return "editPsw.html";
	    }
	    
	    // se tutto va bene
	    credenziali.setPassword(newPsw);
	    credentialsService.saveCredentials(credenziali);
	    
	    model.addAttribute("success", "Success! You have changed your password");
	    
		return "redirect:/profile";
	}
	

	
	
	
}
 