package it.uniroma3.siw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Site;
import it.uniroma3.siw.repository.SiteRepository;

@Service
public class SiteService {
	
	@Autowired
	private SiteRepository siteRepository;
	
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
}
