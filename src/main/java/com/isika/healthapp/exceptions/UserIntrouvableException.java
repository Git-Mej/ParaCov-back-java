package com.isika.healthapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserIntrouvableException extends RuntimeException {
	
	public UserIntrouvableException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
