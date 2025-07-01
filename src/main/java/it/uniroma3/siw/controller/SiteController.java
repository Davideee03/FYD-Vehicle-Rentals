package it.uniroma3.siw.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.controller.validator.SiteValidator;
import it.uniroma3.siw.model.Site;
import it.uniroma3.siw.model.SitePhoto;
import it.uniroma3.siw.service.SitePhotoService;
import it.uniroma3.siw.service.SiteService;

@Controller
public class SiteController {

	@Autowired
	private SiteService siteService;
	
	@Autowired
	private SitePhotoService sitePhotoService;
	
	@Autowired
	private SiteValidator siteValidator;
	
	@GetMapping("/sites")
	public String getAllVehicles(Model model) {
		model.addAttribute("sites", this.siteService.getAllSites());
		return "sites.html";
	}
	
	@Transactional
	@GetMapping("/site/{id}")
	public String getSiteById(@PathVariable("id") Long id, Model model) {
		
		Site site = this.siteService.getSiteById(id);
		
		if(site!=null) {
			model.addAttribute("site", site);
			model.addAttribute("photo", site.getPhoto());
			return "site.html";
		}
		
		model.addAttribute("errorMessage", "Site not found");
		return "error.html";
	}
	
	@GetMapping("/administrator/formNewSite")
	public String addSite(Model model) {
		model.addAttribute("site", new Site());
		return "formNewSite.html";
	}

	@GetMapping("/administrator/formEditSite/{id}")
	public String editSite(@PathVariable Long id, Model model) {

		Site site = siteService.getSiteById(id);
		
		model.addAttribute("site",site);
		model.addAttribute("photo", site.getPhoto());

		return "formEditSite.html";
	}

	@PostMapping("/administrator/formEditSite/{id}")
	public String editSite(@PathVariable Long id, @RequestParam("file") MultipartFile file, @ModelAttribute Site site) {

		Site existingSite = siteService.getSiteById(id);

		existingSite.setName(site.getName());
		existingSite.setCity(site.getCity());
		existingSite.setAddress(site.getAddress());
		existingSite.setPostalCode(site.getPostalCode());
		existingSite.setPhone(site.getPhone());
		existingSite.setEmail(site.getEmail());

		if(!file.isEmpty()) {
			try{
				SitePhoto photo = existingSite.getPhoto();
				if(photo != null){
					photo.setData(file.getBytes());
					this.sitePhotoService.save(photo);
				}
				else{
					SitePhoto newPhoto = new SitePhoto();
					newPhoto.setData(file.getBytes());
					newPhoto.setSite(existingSite);
					this.sitePhotoService.save(newPhoto);
					existingSite.setPhoto(newPhoto);
				}
			} catch (IOException e) {
				e.printStackTrace();
				return "error.html";
			}
		}

		siteService.save(existingSite);

		return "redirect:/site/" + id;
	}
	
	@Transactional
	@PostMapping("/site")
	public String newAuthor(@ModelAttribute("site") Site site, @RequestParam("sitePhoto") MultipartFile photo, Model model, BindingResult bindingResult) {
		
		siteValidator.validate(site, bindingResult);
		if (bindingResult.hasErrors()) {
	        model.addAttribute("duplicate", "The site already exists in the system");
	        return "formNewSite.html";
	    }
				
		this.siteService.save(site);
		
		try {
			SitePhoto sitePhoto = new SitePhoto();
			sitePhoto.setData(photo.getBytes());
			sitePhoto.setSite(site);
			
			this.sitePhotoService.save(sitePhoto);
		} catch(IOException e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "Errore nel caricamento della foto");
			return "error.html";
		}
		
		return "redirect:site/"+site.getId();
	}
	
	@GetMapping("/administrator/deleteSite")
    public String showDeleteSiteForm(Model model) {
        List<Site> sites = siteService.getAllSites();
        model.addAttribute("sites", sites);
        return "formDeleteSite.html";
    }
	
    @PostMapping("/administrator/confirmDeleteSite")
    public String confirmDeleteSite(@RequestParam Long siteToDeleteId, @RequestParam Long newSiteId, Model model) {
    	if(siteToDeleteId.equals(newSiteId)) {
    		List<Site> sites = siteService.getAllSites();
    		model.addAttribute("sites", sites);
    		model.addAttribute("error", "You must choose a different site to reassign data");
    		return "formDeleteSite.html";
    	}
        this.siteService.reassignAndDeleteSite(siteToDeleteId, newSiteId);
        return "redirect:/sites";
    }
}
	
