package it.uniroma3.siw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.SitePhoto;
import it.uniroma3.siw.repository.SitePhotoRepository;

@Service
public class SitePhotoService {

	@Autowired
	private SitePhotoRepository sitePhotoRepository;
	
	public SitePhoto save(SitePhoto sitePhoto) {
		return this.sitePhotoRepository.save(sitePhoto);
	}
	
	public SitePhoto findById(Long id) {
		return this.sitePhotoRepository.findById(id).orElse(null);
	}
	
	public List<SitePhoto> findAll(){
		return (List<SitePhoto>) this.sitePhotoRepository.findAll();
	}
}
