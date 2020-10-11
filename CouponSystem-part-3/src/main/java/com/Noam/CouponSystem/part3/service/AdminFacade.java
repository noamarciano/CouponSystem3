package com.Noam.CouponSystem.part3.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Noam.CouponSystem.part3.beans.Company;
import com.Noam.CouponSystem.part3.beans.Coupon;
import com.Noam.CouponSystem.part3.beans.Customer;
import com.Noam.CouponSystem.part3.exceptions.AlreadyExistException;
import com.Noam.CouponSystem.part3.exceptions.CannotUpdateException;
import com.Noam.CouponSystem.part3.exceptions.DoesntExist;
import com.Noam.CouponSystem.part3.exceptions.LoginDeniedException;
import com.Noam.CouponSystem.part3.exceptions.NoSuchThingLikeThisException;

@Service
public class AdminFacade extends ClientFacade {

	@Autowired
	private CompaniesService companiesService;

	@Autowired
	private CustomersService customersService;

	@Autowired
	private CouponsService couponsService;

	@Override
	public boolean login(String email, String password) throws LoginDeniedException {
		if (email.equals("admin@admin.com") && password.equals("admin")) {
			return true;
		}
		throw new LoginDeniedException("Login manager denied..");
	}

	public void addCompany(Company company) throws AlreadyExistException {

		List<Company> companies = companiesService.getAllCompanies();
		for (Company c : companies) {
			if (c.getEmail().equals(company.getEmail()) || c.getName().equals(company.getName())) {
				throw new AlreadyExistException("Sorry, Company exist");
			}
		}
		companiesService.addCompany(company);
	}

	public void updateCompany(int companyID, Company company) throws DoesntExist, NoSuchThingLikeThisException {

		if (companiesService.getOneCompany(companyID) == null)
			throw new DoesntExist("Sorry, ID not exist");
		Company company2 = companiesService.getOneCompany1(companyID);
		if (!company.getName().equals(company2.getName())) {
			throw new NoSuchThingLikeThisException("Cannot update company name");
		}
		if (!company.getEmail().equals(company2.getEmail()) && (companiesService.getOneCompany(companyID) != null)) {
			throw new NoSuchThingLikeThisException("This mail already exists");
		}
		companiesService.updateCompany(company);
	}

	public void deleteCompany(int companyID) throws DoesntExist {

		if (companiesService.getOneCompany(companyID) == null)
			throw new DoesntExist("ID not exist");
		Company company = companiesService.getOneCompany1(companyID);
		List<Coupon> coupons = couponsService.getAllCouponsByCompanyId(companyID);
		List<Customer> customers = this.getAllCustomers();
		for (Coupon c : coupons) {
			if (coupons != null) {
				for (Customer cu : customers) {
					cu.getCoupons().remove(c);
					couponsService.deleteCoupon(c);
				}
			}
		}
		companiesService.deleteCompany(company);
	}

	public List<Company> getAllCompanies() {
		return companiesService.getAllCompanies();
	}

	public Optional<Company> getOneCompany(int companyID) throws DoesntExist {
		return companiesService.getOneCompany(companyID);
	}

	public Company getOneCompany1(int companyID) throws DoesntExist {
		return companiesService.getOneCompany1(companyID);
	}

	public void addCustomer(Customer customer) throws AlreadyExistException {
		List<Customer> customers = customersService.getAllCustomers();
		for (Customer c : customers) {
			if (c.getEmail().equals(customer.getEmail())) {
				throw new AlreadyExistException("Sorry, Email already exist..");
			}
		}
		customersService.addCustomer(customer);
	}

	public void updateCustomer(Customer customer) throws DoesntExist, NoSuchThingLikeThisException {

		if (customersService.getOneCustomer(customer.getId()) == null)
			throw new DoesntExist("ID not exist");
		Customer customer2 = customersService.getOneCustomer1(customer.getId());
		if (!customer.getEmail().equals(customer2.getEmail())
				&& (customersService.getOneCustomerByEmail(customer.getEmail()) != null)) {
			throw new NoSuchThingLikeThisException("This email already exists");
		}

		customersService.updateCustomer(customer);
	}

	public void deleteCustomer(int customerID) throws DoesntExist {

		if (customersService.getOneCustomer(customerID) == null)
			throw new DoesntExist("ID not exist");
		Customer customer = customersService.getOneCustomer1(customerID);
		customersService.deleteCustomer(customer);
	}

	public List<Customer> getAllCustomers() {
		return customersService.getAllCustomers();
	}

	public Optional<Customer> getOneCustomer(int customerID) throws NoSuchThingLikeThisException {
		return customersService.getOneCustomer(customerID);
	}

	public Customer getOneCustomer1(int customerID) throws DoesntExist {
			if (customersService.getOneCustomer(customerID) == null)
				throw new DoesntExist("ID not exist");
			return customersService.getOneCustomer1(customerID);
		}

}
