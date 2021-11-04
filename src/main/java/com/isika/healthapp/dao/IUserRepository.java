package com.isika.healthapp.dao;

import org.springframework.data.repository.CrudRepository;

import com.isika.healthapp.modele.User;

public interface IUserRepository extends CrudRepository<User, Integer> {
	
	User findByMail(String mail);

}
