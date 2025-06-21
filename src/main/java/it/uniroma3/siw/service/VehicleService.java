package it.uniroma3.siw.service;

import it.uniroma3.siw.model.Rental;
import it.uniroma3.siw.model.Vehicle;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.repository.RentalRepository;
import it.uniroma3.siw.repository.VehicleRepository;
import jakarta.transaction.Transactional;

@Service
public class VehicleService {

	@Autowired
	private VehicleRepository vehicleRepository;
	
	@Autowired
	private RentalRepository rentalRepository;

	public List<Vehicle> getAllVehicles(){
		return (List<Vehicle>) this.vehicleRepository.findAll();
	}

	public Vehicle getVehicleById(Long id) {
		return this.vehicleRepository.findById(id).orElse(null);
	}
	
	public List<Vehicle> getAvailableVehicles(LocalDate startDate, LocalDate endDate, String city) {
	    return vehicleRepository.queryAvailableVehicles(startDate, endDate, city);
	}

	public void save(Vehicle vehicle) {
		this.vehicleRepository.save(vehicle);
	}

	public List<Vehicle> getVehiclesByIds(List<Long> vehicleIds) {
		return (List<Vehicle>) vehicleRepository.findAllById(vehicleIds);
	}

	public void deleteVehiclesByIds(List<Long> vehicleIds) {
		this.vehicleRepository.deleteAllById(vehicleIds);
		
	}

	// called when we need to eliminate the vehicles but leave the active rentals in the system available
	@Transactional
	public void deleteVehiclesOnly(List<Long> vehicleIds) {
		for (Long vehicleId : vehicleIds) {
	        Vehicle vehicle = vehicleRepository.findById(vehicleId).orElse(null);

	        if (vehicle != null) {
	            List<Rental> rentals = vehicle.getRentals();

	            for (Rental rental : rentals) {
	                // Copy minimal data
	                rental.setVehicleBrand(vehicle.getBrand());
	                rental.setVehicleModel(vehicle.getModel());

	                rental.setVehicle(null); // need to keep the rental but not the vehicle
	            }
	            rentalRepository.saveAll(rentals);

	            vehicleRepository.delete(vehicle);
	        }
	    }
	}

}
