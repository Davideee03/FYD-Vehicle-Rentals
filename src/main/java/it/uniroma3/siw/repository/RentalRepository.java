package it.uniroma3.siw.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.Rental;

public interface RentalRepository extends CrudRepository<Rental, Long> {

	@Query("SELECT NOT EXISTS (SELECT r FROM Rental r WHERE r.vehicle.id = :vehicleId AND r.startDate <= :endDate AND r.endDate >= :startDate)")
	Boolean isVehicleAvailableForRental(@Param("vehicleId") Long vehicleId,
	                                    @Param("startDate") LocalDate startDate,
	                                    @Param("endDate") LocalDate endDate);
	
	@Query("SELECT r FROM Rental r WHERE r.vehicle.id IN :ids")
	List<Rental> findRentalsByVehicleIds(@Param("ids") List<Long> ids);
	
	@Modifying
	@Query("DELETE FROM Rental r WHERE r.vehicle.id IN :ids")
	void deleteRentalsByVehicleIds(@Param("ids") List<Long> ids);
	
	@Modifying
	@Query("DELETE FROM Rental r WHERE r.site.id IN :ids")
	void deleteRentalsBySiteIds(@Param("ids") List<Long> ids);
}
