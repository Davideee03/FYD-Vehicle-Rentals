package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.siw.model.UserPhoto;
import it.uniroma3.siw.service.UserPhotoService;

@Controller
public class UserPhotoController {
	@Autowired
	private UserPhotoService userPhotoService;
	
	@GetMapping("/userPhoto/{id}")
	public ResponseEntity<byte[]> getPhoto(@PathVariable Long id) {
	    UserPhoto photo = this.userPhotoService.findById(id);
	    if (photo == null) {
	        return ResponseEntity.notFound().build();
	    }
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.IMAGE_JPEG); // o IMAGE_PNG
	    return new ResponseEntity<>(photo.getData(), headers, HttpStatus.OK);
	}
}
