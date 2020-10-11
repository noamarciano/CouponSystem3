package com.Noam.CouponSystem.part3.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import com.Noam.CouponSystem.part3.security.LoginManager;
import com.Noam.CouponSystem.part3.security.TokenManager;

public abstract class ClientController {
	
	@Autowired
	LoginManager loginManager;
	@Autowired
	TokenManager tokenManager;

	public abstract ResponseEntity<?> login(@RequestParam String email, @RequestParam String password);

}
