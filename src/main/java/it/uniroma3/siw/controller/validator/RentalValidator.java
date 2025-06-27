package it.uniroma3.siw.controller.validator;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.uniroma3.siw.model.Rental;
import it.uniroma3.siw.model.Vehicle;
import it.uniroma3.siw.service.VehicleService;

@Component
public class RentalValidator {

    @Autowired
    private VehicleService vehicleService;

    public String validate(Long vehicleId, LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null)
            return "Start and end dates must not be null.";

        if (startDate.isBefore(LocalDate.now()))
            return "Start date must be today or later.";

        if (startDate.isAfter(endDate))
            return "Start date must be before or equal to end date.";

        Vehicle vehicle = vehicleService.getVehicleById(vehicleId);
        if (vehicle == null)
            return "Vehicle not found.";

        for (Rental rental : vehicle.getRentals()) {
            boolean overlaps = !startDate.isAfter(rental.getEndDate()) && !rental.getStartDate().isAfter(endDate);
            if (overlaps) {
                return "This vehicle is already reserved from " +
                        rental.getStartDate() + " to " + rental.getEndDate();
            }
        }

        return null;
    }
}

