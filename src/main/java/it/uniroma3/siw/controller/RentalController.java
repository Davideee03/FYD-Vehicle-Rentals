package it.uniroma3.siw.controller;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.uniroma3.siw.model.Rental;
import it.uniroma3.siw.model.Site;
import it.uniroma3.siw.model.Vehicle;
import it.uniroma3.siw.service.RentalService;
import it.uniroma3.siw.service.SiteService;
import it.uniroma3.siw.service.VehicleService;

@Controller
public class RentalController {
	
	@Autowired
	private RentalService rentalService;
	
	@Autowired
	private VehicleService vehicleService;
	
	@Autowired
	private SiteService siteService;

	@GetMapping("/rentalSummary")
	public String rentalSummary(@RequestParam Long siteId,
	                            @RequestParam Long vehicleId,
	                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
	                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
	                            Model model) {
		
		if (startDate.isAfter(endDate)) {
			model.addAttribute("error", "Start date must be before or equal to end date.");
			return "error.html";
		}
		
		Vehicle vehicle = vehicleService.getVehicleById(vehicleId);
		
		//Check if there are no rental conflicts
		for(Rental rental : vehicle.getRentals()) {
			if(!startDate.isAfter(rental.getEndDate()) && !rental.getStartDate().isAfter(endDate)) {
				model.addAttribute("vehicle", vehicle);
				model.addAttribute("photo", vehicle.getPhoto());
				model.addAttribute("error", "This vehicle is already reserved from " + startDate + " to " + endDate);
				return "vehicle.html";
			}
		}
			
		Site site = siteService.getSiteById(siteId);
		long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;
		long total = vehicle.getPrice() * days;
		
		model.addAttribute("site", site);
		model.addAttribute("vehicle", vehicle);
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("total", total);
		
		return "rentalSummary.html";
	}

	@PostMapping("/confirmRental")
	public String confirmRental(@RequestParam Long siteId,
	                            @RequestParam Long vehicleId,
	                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
	                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
	                            RedirectAttributes redirectAttributes,
	                            Model model) {

		if (startDate.isAfter(endDate)) {
			model.addAttribute("error", "Start date must be before or equal to end date.");
			return "error.html";
		}

		if (!rentalService.isVehicleAvailableForRental(vehicleId, startDate, endDate)) {
			model.addAttribute("error", "The selected vehicle is not available for the chosen dates.");
			return "error.html";
		}
		
		Vehicle vehicle = vehicleService.getVehicleById(vehicleId);
		Site site = siteService.getSiteById(siteId);
		
		long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;
		long total = vehicle.getPrice() * days;

		Rental rental = new Rental();
		rental.setSite(site);
		rental.setVehicle(vehicle);
		rental.setStartDate(startDate);
		rental.setEndDate(endDate);
		rental.setTotal(total);
		
		// snap
		rental.setVehicleBrand(vehicle.getBrand());
		rental.setVehicleModel(vehicle.getModel());
		
		this.rentalService.confirmRental(rental);
		
		redirectAttributes.addAttribute("rentalId", rental.getId());
		return "redirect:/rentalConfirmed";
	}

	@GetMapping("/rentalConfirmed")
	public String rentalConfirmed(@RequestParam Long rentalId, Model model) {
		Rental rental = rentalService.getRentalById(rentalId);
		model.addAttribute("rental", rental);
		return "rentalConfirmed.html";
	}
	
	@GetMapping("/administrator/deleteRentals") 
	public String showDeleteRentals(Model model) {
		model.addAttribute("rentals", this.rentalService.getAllRentals());
		return "deleteRentals.html";
	}
	
	// summary page with all selected vehicles to delete
	@PostMapping("/administrator/confirmDeleteRentals") 
	public String confirmDeleteVehicles(@RequestParam List<Long> rentalIds, Model model) {
		List<Rental> selectedRentals = this.rentalService.getRentalByIds(rentalIds);
		model.addAttribute("rental", selectedRentals); 
		return "deleteRentalsSummary.html";
	}
	

	// delete active rentals
	@PostMapping("/administrator/deleteRentals")
	public String deleteRentals(@RequestParam List<Long> rentalIds, Model model) {
		this.rentalService.deleteRentals(rentalIds);
		return "redirect:/vehicles";
	}
	
	
}
