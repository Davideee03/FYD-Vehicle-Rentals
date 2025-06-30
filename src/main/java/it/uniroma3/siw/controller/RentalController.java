package it.uniroma3.siw.controller;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.uniroma3.siw.controller.validator.RentalValidator;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Rental;
import it.uniroma3.siw.model.Site;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.model.Vehicle;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.RentalService;
import it.uniroma3.siw.service.SiteService;
import it.uniroma3.siw.service.VehicleService;
import jakarta.transaction.Transactional;

@Controller
public class RentalController {

	@Autowired
	private RentalService rentalService;

	@Autowired
	private VehicleService vehicleService;

	@Autowired
	private SiteService siteService;

	@Autowired
	private CredentialsService credentialsService;

	@Autowired
	private RentalValidator rentalValidator;
	
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public String handleMissingParams(Exception e) {
	    System.out.println(">>> Parametro mancante: " + e.getMessage());
	    return "error";
	}

	@Transactional
	@GetMapping("/rentalSummary")
	public String rentalSummary(@RequestParam Long siteId, @RequestParam Long vehicleId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate, Model model) {
	    
		String validationError = rentalValidator.validate(vehicleId, startDate, endDate);
		if (validationError != null) {
			Vehicle vehicle = vehicleService.getVehicleById(vehicleId);
			model.addAttribute("vehicle", vehicle);
			model.addAttribute("photo", vehicle.getVehiclePhoto());
			model.addAttribute("error", validationError);
			return "vehicle.html";
		}

		Vehicle vehicle = vehicleService.getVehicleById(vehicleId);
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
	public String confirmRental(@RequestParam Long siteId, @RequestParam Long vehicleId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
			RedirectAttributes redirectAttributes, Model model) {

		String validationError = rentalValidator.validate(vehicleId, startDate, endDate);
		if (validationError != null) {
			Vehicle vehicle = vehicleService.getVehicleById(vehicleId);
			Site site = siteService.getSiteById(siteId);
			long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;
			long total = vehicle.getPrice() * days;

			model.addAttribute("site", site);
			model.addAttribute("vehicle", vehicle);
			model.addAttribute("startDate", startDate);
			model.addAttribute("endDate", endDate);
			model.addAttribute("total", total);
			model.addAttribute("error", validationError);

			return "rentalSummary.html";
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

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		Credentials cred = credentialsService.getCredentials(userDetails.getUsername());
		User user = cred.getUser();

		rental.setUser(user);
		user.getRentals().add(rental);

		this.rentalService.confirmRental(rental);

		redirectAttributes.addAttribute("rentalId", rental.getId());
		return "redirect:/rentalConfirmed";
	}

	@GetMapping("/rentalConfirmed")
	public String rentalConfirmed(@RequestParam Long rentalId, Model model) {
		Rental rental = rentalService.getRentalById(rentalId);
		model.addAttribute("rental", rental);

		List<Vehicle> vehicles = this.vehicleService.getAllVehicles();
		vehicles.remove(rental.getVehicle());
		Collections.shuffle(vehicles);
		vehicles = vehicles.subList(0, Math.min(3, vehicles.size()));
		model.addAttribute("vehicles", vehicles);

		return "rentalConfirmed.html";
	}

	@GetMapping("/administrator/deleteRentals")
	public String showDeleteRentals(Model model) {
		model.addAttribute("rentals", this.rentalService.getAllRentals());
		return "deleteRentals.html";
	}
	
	
	// summary page with all selected vehicles to delete
	@PostMapping("/administrator/confirmDeleteRentals")
	public String confirmDeleteVehicles(@RequestParam (name="rentalIds", required = false) List<Long> rentalIds, Model model) {
		if (rentalIds == null || rentalIds.isEmpty()) {
			model.addAttribute("rentals", this.rentalService.getAllRentals());
			model.addAttribute("error", "Select at least one rental, please.");
			return "deleteRentals.html";
		}
		List<Rental> selectedRentals = this.rentalService.getRentalByIds(rentalIds);
		model.addAttribute("rental", selectedRentals);
		return "deleteRentalsSummary.html";
	}

	// delete active rentals
	@PostMapping("/administrator/deleteRentals")
	public String deleteRentals(@RequestParam List<Long> rentalIds, Model model) {
		this.rentalService.deleteRentals(rentalIds);
		return "redirect:/";
	}

}
