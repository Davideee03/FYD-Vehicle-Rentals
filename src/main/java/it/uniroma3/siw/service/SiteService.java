package it.uniroma3.siw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Rental;
import it.uniroma3.siw.model.Site;
import it.uniroma3.siw.model.Vehicle;
import it.uniroma3.siw.repository.RentalRepository;
import it.uniroma3.siw.repository.SiteRepository;
import it.uniroma3.siw.repository.VehicleRepository;
import jakarta.transaction.Transactional;

@Service
public class SiteService {
	
	@Autowired
	private SiteRepository siteRepository;
	
	@Autowired
	private VehicleRepository vehicleRepository;
	
	@Autowired
	private RentalRepository rentalRepository;
	
	public List<String> getCityList() {
		return (List<String>) this.siteRepository.queryCityList();
	}
	
	public List<Site> getAllSites() {
		return (List<Site>) this.siteRepository.findAll();
	}
	
	public Site getSiteById(Long siteId) {
		return this.siteRepository.findById(siteId).orElse(null);
	}
	
	public void save(Site site) {
		this.siteRepository.save(site);
	}
	
	public void deleteSitesById(List<Long> ids) {
		this.siteRepository.deleteAllById(ids);
	}
	
	public List<Site> getAllSitesById(List<Long> ids) {
		return (List<Site>) this.siteRepository.findAllById(ids);
	}

	@Transactional
	public void reassignAndDeleteSite(Long oldSiteId, Long newSiteId) {
	    Site oldSite = siteRepository.findById(oldSiteId).orElseThrow();
	    Site newSite = siteRepository.findById(newSiteId).orElseThrow();

	    // move the vehicles to the new site
	    for (Vehicle v : oldSite.getVehicles()) {
	        v.setSite(newSite);
	    }

	    // move also the associated rentals
	    for (Rental r : oldSite.getRentals()) {
	        r.setSite(newSite);
	    }

	    siteRepository.delete(oldSite);
	}

}
