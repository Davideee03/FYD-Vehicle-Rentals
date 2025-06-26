package it.uniroma3.siw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.UserPhoto;
import it.uniroma3.siw.repository.UserPhotoRepository;

@Service
public class UserPhotoService {
	
	@Autowired
	private UserPhotoRepository userPhotoRepository;

	public UserPhoto save(UserPhoto userPhoto) {
		return userPhotoRepository.save(userPhoto);
	}
	
	public UserPhoto findById(Long id) {
		return this.userPhotoRepository.findById(id).orElse(null);
	}
	
	public List<UserPhoto> findAll(){
		return (List<UserPhoto>) this.userPhotoRepository.findAll();
	}

}
