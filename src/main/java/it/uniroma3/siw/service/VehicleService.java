package it.uniroma3.siw.service;

import it.uniroma3.siw.model.Vehicle;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.repository.VehicleRepository;

@Service
public class VehicleService {

	@Autowired
	private VehicleRepository vehicleRepository;

	public List<Vehicle> getAllVehicles(){
		return (List<Vehicle>) this.vehicleRepository.findAll();
	}

	public Vehicle getVehicleById(Long id) {
		return this.vehicleRepository.findById(id).orElse(null);
	}
	
	public List<Vehicle> getAvailableVehicles(LocalDate startDate, LocalDate endDate, String city) {
	    return vehicleRepository.queryAvailableVehicles(startDate, endDate, city);
	}

}
