package it.uniroma3.siw.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.VehiclePhoto;
import it.uniroma3.siw.repository.VehiclePhotoRepository;

@Service
public class VehiclePhotoService {

	@Autowired
	private VehiclePhotoRepository vehiclePhotoRepository;

	public VehiclePhoto save(VehiclePhoto bookPhoto) {
		return vehiclePhotoRepository.save(bookPhoto);
	}
	
	public VehiclePhoto findById(Long id) {
		return this.vehiclePhotoRepository.findById(id).orElse(null);
	}
	
	public List<VehiclePhoto> findAll(){
		return (List<VehiclePhoto>) this.vehiclePhotoRepository.findAll();
	}
}
