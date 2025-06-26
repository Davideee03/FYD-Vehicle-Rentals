/*
 * package it.uniroma3.siw.model;
 * 
 * import java.util.List;
 * 
 * import jakarta.persistence.Entity; import jakarta.persistence.GeneratedValue;
 * import jakarta.persistence.GenerationType; import jakarta.persistence.Id;
 * import jakarta.persistence.OneToMany;
 * 
 * @Entity public class Client {
 * 
 * @Id
 * 
 * @GeneratedValue(strategy = GenerationType.AUTO) private Long id;
 * 
 * private String email; private String address; private String phone;
 * 
 * @OneToMany(mappedBy = "client") private List<Rental> rentals;
 * 
 * public String getEmail() { return email; }
 * 
 * public void setEmail(String email) { this.email = email; }
 * 
 * public String getAddress() { return address; }
 * 
 * public void setAddress(String address) { this.address = address; }
 * 
 * public String getPhone() { return phone; }
 * 
 * public void setPhone(String phone) { this.phone = phone; }
 * 
 * public List<Rental> getRentals() { return rentals; }
 * 
 * public void setRentals(List<Rental> rentals) { this.rentals = rentals; } }
 */