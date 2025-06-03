package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.siw.service.SiteService;

@Controller
public class SiteController {

	@Autowired
	private SiteService siteService;
	
	@GetMapping("/sites")
	public String getAllVehicles(Model model) {
		model.addAttribute("sites", this.siteService.getAllSites());
		return "sites.html";
	}
	
	@GetMapping("/site/{id}")
	public String getSiteById(@PathVariable("id") Long id, Model model) {
		model.addAttribute("site", this.siteService.getSiteById(id));
		return "site.html";
	}
}
