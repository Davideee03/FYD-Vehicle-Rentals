package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import it.uniroma3.siw.service.SiteService;

@Controller
public class HomepageController {
	
	@Autowired
	private SiteService siteService;

	@GetMapping("/")
	public String getHomepage(Model model) {
		model.addAttribute("cityList", this.siteService.getCityList());
		return "homepage.html";
	}
}
