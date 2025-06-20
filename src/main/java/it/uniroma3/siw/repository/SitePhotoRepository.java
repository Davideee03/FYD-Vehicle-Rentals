package it.uniroma3.siw.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.uniroma3.siw.model.SitePhoto;

@Repository
public interface SitePhotoRepository extends CrudRepository<SitePhoto, Long>{

}
