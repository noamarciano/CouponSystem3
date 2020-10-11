package com.Noam.CouponSystem.part3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.Noam.CouponSystem.part3.exceptions.LoginDeniedException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
public abstract class ClientFacade {

	@Autowired
	protected CompaniesService companiesService;
	@Autowired
	protected CustomersService customersService;
	@Autowired
	protected CouponsService couponsService;

	public abstract boolean login(String email, String password) throws LoginDeniedException;
}
