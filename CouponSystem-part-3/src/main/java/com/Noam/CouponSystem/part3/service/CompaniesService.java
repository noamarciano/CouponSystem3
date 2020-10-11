package com.Noam.CouponSystem.part3.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Noam.CouponSystem.part3.beans.Company;
import com.Noam.CouponSystem.part3.repo.CompanyRepository;

@Service
public class CompaniesService {

	@Autowired
	private CompanyRepository repo;

	public void addCompany(Company company) {
		repo.save(company);
	}

	public void updateCompany(Company company) {
		repo.saveAndFlush(company);
	}

	public void deleteCompany(Company company) {
		repo.delete(company);
	}

	public List<Company> getAllCompanies() {
		return repo.findAll();
	}

	public Optional<Company> getOneCompany(int id) {
		return repo.findById(id);
	}

	public Company getOneCompany1(int id) {
		return repo.getOne(id);
	}

	public Company isCompanyExist(String email, String password) {
		return repo.findCompanyByEmailAndPassword(email, password);
	}

}
