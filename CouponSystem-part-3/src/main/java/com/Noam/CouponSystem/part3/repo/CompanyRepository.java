package com.Noam.CouponSystem.part3.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Noam.CouponSystem.part3.beans.Company;

public interface CompanyRepository extends JpaRepository<Company, Integer> {

	public Company findCompanyByEmailAndPassword(String email, String password);

}
