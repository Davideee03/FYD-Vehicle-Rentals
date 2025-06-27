package it.uniroma3.siw.model;

import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
	@Id 
	@GeneratedValue
	private Long id;

	private String name;
	private String surname;
	private String email;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Rental> rentals;
	
	@OneToOne(mappedBy = "user", cascade=CascadeType.ALL, orphanRemoval = true)
	private UserPhoto userPhoto;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Rental> getRentals() {
		return rentals;
	}

	public void setRentals(List<Rental> rentals) {
		this.rentals = rentals;
	}

	public UserPhoto getUserPhoto() {
		return userPhoto;
	}

	public void setUserPhoto(UserPhoto userPhoto) {
		this.userPhoto = userPhoto;
	}
	
	
	
	
}
