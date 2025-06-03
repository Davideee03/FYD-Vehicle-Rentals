package it.uniroma3.siw.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.Vehicle;

public interface VehicleRepository extends CrudRepository<Vehicle, Long> {
	
	@Query("SELECT v FROM Vehicle v WHERE LOWER(v.site.city) = LOWER(:city) AND NOT EXISTS "
	     + "(SELECT r FROM Rental r WHERE r.vehicle = v AND r.startDate <= :endDate AND r.endDate >= :startDate)")
	List<Vehicle> queryAvailableVehicles(@Param("startDate") LocalDate startDate, 
	                                    @Param("endDate") LocalDate endDate, 
	                                    @Param("city") String city);
	
	@Query("SELECT DISTINCT v FROM Vehicle v JOIN v.rentals r")
    List<Vehicle> findVehiclesWithAtLeastOneRental();
}
