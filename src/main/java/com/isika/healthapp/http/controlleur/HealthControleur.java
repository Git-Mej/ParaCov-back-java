package com.isika.healthapp.http.controlleur;

import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.isika.healthapp.dao.IUserRepository;
import com.isika.healthapp.exceptions.UserIntrouvableException;
import com.isika.healthapp.modele.User;


@RestController
@RequestMapping(path="/health")
public class HealthControleur {
	
	@Autowired
	private IUserRepository userRepository;
	
	
	Logger log = LoggerFactory.getLogger(this.getClass());
	
	@GetMapping(path="/rechercheParMail/{mail}")
	public User rechercherUserMail(@PathVariable String mail) {
		
		User utilisateur = userRepository.findByMail(mail);
		log.info("----------------- Récupération de l'utilisateur dont l'adresse mail est " + mail + " : {"+ utilisateur +"}");
		
		return utilisateur;
		
	}
	
	@GetMapping(path="/Utilisateur/{id}")
	public MappingJacksonValue  rechercherAssureId(@PathVariable Integer id) {
		//		return assureRepository.findById(id);
		//J2- 13

		Optional<User> utilisateur =  userRepository.findById(id);

		//J2- 14
		if (!utilisateur.isPresent()) throw new UserIntrouvableException("L'utilisateur avec l'id " + id + " n'existe pas !"); 

		FilterProvider listeFiltres = creerFiltre("filtreDynamiqueJson", "mail");

		ArrayList<User> listeUtilisateurs = new ArrayList<User>();
		listeUtilisateurs.add(utilisateur.get());

		return filtrerUtilisateurs(listeUtilisateurs, listeFiltres);
	}
	
	@PostMapping(path="/ajouterUtilisateur")
	// J3- 4 @Valid
	public ResponseEntity<Void> creerUtilisateur(@RequestBody User user) {
		User utilisateurAjoute = user;
		
		userRepository.save(utilisateurAjoute);
		

		if (utilisateurAjoute == null)
			return ResponseEntity.noContent().build();

		URI uri = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(utilisateurAjoute.getId())
				.toUri();

		return ResponseEntity.created(uri).build(); 
	}
	
	
	public FilterProvider creerFiltre(String nomDuFiltre, String attribut) {

		SimpleBeanPropertyFilter unFiltre;
		if (attribut != null) {
			unFiltre = SimpleBeanPropertyFilter.serializeAllExcept(attribut);
		}
		else {
			unFiltre = SimpleBeanPropertyFilter.serializeAll();
		}

		return new SimpleFilterProvider().addFilter(nomDuFiltre, unFiltre);
	}

	//J2- 13
	public MappingJacksonValue filtrerUtilisateurs(List<User> utilisateurs, FilterProvider listeFiltres) {

		MappingJacksonValue utilisateursFiltres = new MappingJacksonValue(utilisateurs);

		utilisateursFiltres.setFilters(listeFiltres);

		return utilisateursFiltres;
	}

	
	
	


}
