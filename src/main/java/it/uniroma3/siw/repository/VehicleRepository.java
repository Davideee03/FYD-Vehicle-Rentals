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
	List<Vehicle> queryAvailableVehicles(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate,
			@Param("city") String city);

	@Query("SELECT DISTINCT v FROM Vehicle v JOIN v.rentals r")
	List<Vehicle> findVehiclesWithAtLeastOneRental();

	@Query("""
			    SELECT DISTINCT v FROM Vehicle v
			    WHERE
			        (:brand = '' OR v.brand = :brand)
			        AND (:model = '' OR v.model = :model)
			        AND (:category = '' OR v.category = :category)
			        AND (:transmission = '' OR v.transmission = :transmission)
			        AND (:color = '' OR v.color = :color)
			        AND (:seats IS NULL OR v.seats = :seats)
			        AND (:price IS NULL OR v.price <= :price)
			""")
	List<Vehicle> filterVehicles(@Param("brand") String brand, @Param("model") String model,
			@Param("category") String category, @Param("transmission") String transmission,
			@Param("color") String color, @Param("seats") Integer seats, @Param("price") Long price);

}
