package com.Noam.CouponSystem.part3.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Noam.CouponSystem.part3.beans.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

	public Customer findByEmailAndPassword(String email, String password);
	
	Customer findByEmail(String email);

}
