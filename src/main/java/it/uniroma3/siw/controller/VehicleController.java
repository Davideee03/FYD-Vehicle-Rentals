package it.uniroma3.siw.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Vehicle;
import it.uniroma3.siw.model.VehiclePhoto;
import it.uniroma3.siw.service.SiteService;
import it.uniroma3.siw.service.VehiclePhotoService;
import it.uniroma3.siw.service.VehicleService;

@Controller
public class VehicleController {

	@Autowired
	private VehicleService vehicleService;

	@Autowired
	private SiteService siteService;

	@Autowired
	private VehiclePhotoService vehiclePhotoService;

	@GetMapping("/vehicles")
	public String getAllVehicles(Model model) {
		model.addAttribute("vehicles", this.vehicleService.getAllVehicles());
		return "vehicles.html";
	}

	@Transactional
	@GetMapping("/vehicle/{id}")
	public String getVehicleById(@PathVariable("id") Long id, Model model) {
		Vehicle vehicle = this.vehicleService.getVehicleById(id);

		if (vehicle != null) {
			model.addAttribute("vehicle", vehicle);
			model.addAttribute("photo", vehicle.getPhoto());
			return "vehicle.html";
		}

		model.addAttribute("errorMessage", "Vehicle not found");
		return "error.html";
	}

	@GetMapping("/availableVehicles")
	public String getAvailableVehicles(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate, @RequestParam String city,
			Model model) {

		if (startDate.isAfter(endDate) || startDate.isBefore(LocalDate.now())) {
			model.addAttribute("error", "Invalid pick-up or drop-off date.");
			return "homepage.html";
		}

		model.addAttribute("vehicles", this.vehicleService.getAvailableVehicles(startDate, endDate, city));
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("city", city);
		return "rentVehicle.html";
	}

	@GetMapping("/administrator/formNewVehicle")
	public String addVehicle(Model model) {
		model.addAttribute("vehicle", new Vehicle());
		model.addAttribute("sites", this.siteService.getAllSites());
		return "formNewVehicle.html";
	}

	@Transactional
	@PostMapping("/vehicle")
	public String saveVehicle(@ModelAttribute("vehicle") Vehicle vehicle, @RequestParam("site_id") Long id,
			@RequestParam("file") MultipartFile file, Model model) {
		vehicle.setSite(this.siteService.getSiteById(id));
		this.vehicleService.save(vehicle);

		if (!file.isEmpty()) {
			try {
				VehiclePhoto vehiclePhoto = new VehiclePhoto();
				vehiclePhoto.setData(file.getBytes());
				vehiclePhoto.setVehicle(vehicle);
				this.vehiclePhotoService.save(vehiclePhoto);
			} catch (IOException e) {
				e.printStackTrace();
				model.addAttribute("errorMessage", "Errore nel caricamento della foto");
				return "error.html";
			}
		}

		return "redirect:/vehicle/" + vehicle.getId();
	}

	@GetMapping("/filterVehicles")
	public String filterVehicles(@RequestParam(required = false, defaultValue = "") String brand,
			@RequestParam(required = false, defaultValue = "") String model,
			@RequestParam(required = false, defaultValue = "") String category,
			@RequestParam(required = false, defaultValue = "") String transmission,
			@RequestParam(required = false, defaultValue = "") String color,
			@RequestParam(required = false) Integer seats, @RequestParam(required = false) Long price,
			Model htmlModel) {
		List<Vehicle> vehicles = vehicleService.filterVehicles(brand, model, category, transmission, color, seats,
				price);
		htmlModel.addAttribute("vehicles", vehicles);
		return "vehicles.html";
	}

}
