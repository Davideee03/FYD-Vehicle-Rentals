package it.uniroma3.siw.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Rental;
import it.uniroma3.siw.repository.RentalRepository;
import jakarta.transaction.Transactional;

@Service
public class RentalService {
	
	@Autowired
	private RentalRepository rentalRepository;
	
	public Rental getRentalById(Long rentalId) {
		return this.rentalRepository.findById(rentalId).orElseThrow();
	}
	
	public void confirmRental(Rental rental) {
		this.rentalRepository.save(rental);
	}
	
	public Boolean isVehicleAvailableForRental(Long vehicleId, LocalDate startDate, LocalDate endDate) {
		return this.rentalRepository.isVehicleAvailableForRental(vehicleId, startDate, endDate);
	}

	@Transactional
	public void deleteRentalsByVehicleIds(List<Long> ids) {
	    this.rentalRepository.deleteRentalsByVehicleIds(ids);
	}
	
	@Transactional
	public void deleteRentalsBySiteIds(List<Long> ids) {
	    this.rentalRepository.deleteRentalsBySiteIds(ids);
	}
}
