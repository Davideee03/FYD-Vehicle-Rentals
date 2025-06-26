package it.uniroma3.siw.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.model.UserPhoto;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.UserService;
import jakarta.transaction.Transactional;

@Controller
public class UserController {
	
	@Autowired
	private CredentialsService credentialsService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/profile") 
	public String getProfile(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    UserDetails userDetails = (UserDetails) auth.getPrincipal();
	    Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
	    User user = credentials.getUser();
	    
	    model.addAttribute("user", user);
	    model.addAttribute("credentials", credentials);
	    model.addAttribute("rentals", user.getRentals());

		return "profile.html";
	}
	
	// modifica profilo 
	@GetMapping("/profile/editProfile")
	public String editProfile(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    UserDetails userDetails = (UserDetails) auth.getPrincipal();
	    Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
	    User user = credentials.getUser();
	    
		model.addAttribute("credentials", credentials);
		model.addAttribute("user", user);
		
		return "editProfile.html";
	}
	
	@Transactional
	@PostMapping("/profile/editProfile")
	public String saveEditProfile(@ModelAttribute("user") User user,
	                              @ModelAttribute("credentials") Credentials credentials,
	                              @RequestParam(name = "file", required = false) MultipartFile photoFile,
	                              Model model) throws IOException {

	    User existingUser = userService.getUser(user.getId());

	    existingUser.setName(user.getName());
	    existingUser.setSurname(user.getSurname());
	    existingUser.setEmail(user.getEmail());

	    if (photoFile != null && !photoFile.isEmpty()) {
	        UserPhoto photo = existingUser.getUserPhoto();
	        if (photo == null) {
	            photo = new UserPhoto();
	            photo.setUser(existingUser);
	        }
	        photo.setData(photoFile.getBytes());
	        existingUser.setUserPhoto(photo);
	    }

	    userService.saveUser(existingUser);

	    model.addAttribute("success", "Perfect, you have modified your profile!");
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
	    	model.addAttribute("msgError", "Wrong Password");
	    	return "editPsw.html";
	    }
	    
	    if (!newPsw.equals(confirmPsw))  {
	    	model.addAttribute("msgError", "Wrong new Passwords");
	
	    	return "editPsw.html";
	    }
	    
	    // se tutto va bene
	    credenziali.setPassword(newPsw);
	    credentialsService.saveCredentials(credenziali);
	    
	    model.addAttribute("success", "You have modified your profile!");
	    
		return "redirect:/profile";
	}
	

}
