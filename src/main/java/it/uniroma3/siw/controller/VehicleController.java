package it.uniroma3.siw.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.service.VehicleService;

@Controller
public class VehicleController {

	@Autowired
	private VehicleService vehicleService;
	
	@GetMapping("/vehicles")
	public String getAllVehicles(Model model) {
		model.addAttribute("vehicles", this.vehicleService.getAllVehicles());
		return "vehicles.html";
	}
	
	@GetMapping("/vehicle/{id}")
	public String getVehicleById(@PathVariable("id") Long id, Model model) {
		model.addAttribute("vehicle", this.vehicleService.getVehicleById(id));
		return "vehicle.html";
	}
	
	@GetMapping("/availableVehicles")
	public String getAvailableVehicles(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
	                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
	                                   @RequestParam String city,
	                                   Model model) {
		
		if (startDate.isAfter(endDate)) {
			model.addAttribute("error", "Start date must be before or equal to end date.");
			return "error.html";
		}
		
	    model.addAttribute("vehicles", this.vehicleService.getAvailableVehicles(startDate, endDate, city));
	    model.addAttribute("startDate", startDate);
	    model.addAttribute("endDate", endDate);
	    model.addAttribute("city", city);
	    return "vehicles.html";
	}

}
