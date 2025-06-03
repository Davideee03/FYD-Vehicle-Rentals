package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import it.uniroma3.siw.model.Site;

public interface SiteRepository extends CrudRepository<Site, Long>{

	@Query("SELECT DISTINCT s.city FROM Site s")
	List<String> queryCityList();
}
