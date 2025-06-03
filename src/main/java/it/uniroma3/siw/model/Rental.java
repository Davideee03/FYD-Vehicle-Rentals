package it.uniroma3.siw.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Rental {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private LocalDate startDate;
	private LocalDate endDate;
	private Long total;

	@ManyToOne
	@JoinColumn(name = "client_id")
	private Client client;

	@ManyToOne
	@JoinColumn(name = "vehicle_id")
	private Vehicle vehicle;

	@ManyToOne
	@JoinColumn(name = "site_id")
	private Site site;

	public Long getId() {
		return id;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}
}
