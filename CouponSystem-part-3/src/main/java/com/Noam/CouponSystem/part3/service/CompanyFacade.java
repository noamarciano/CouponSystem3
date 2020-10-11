package com.Noam.CouponSystem.part3.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.Noam.CouponSystem.part3.beans.Category;
import com.Noam.CouponSystem.part3.beans.Company;
import com.Noam.CouponSystem.part3.beans.Coupon;
import com.Noam.CouponSystem.part3.beans.Customer;
import com.Noam.CouponSystem.part3.exceptions.AlreadyExistException;
import com.Noam.CouponSystem.part3.exceptions.DoesntExist;
import com.Noam.CouponSystem.part3.exceptions.LoginDeniedException;
import com.Noam.CouponSystem.part3.exceptions.NoSuchThingLikeThisException;

import lombok.Setter;

@Service
//@Scope("prototype")
@Setter
public class CompanyFacade extends ClientFacade {

	private int companyID;

	public CompanyFacade() {
		super();
	}

	@Override
	public boolean login(String email, String password) throws LoginDeniedException {
		if (companiesService.isCompanyExist(email, password) != null) {
			return true;
		}
		throw new LoginDeniedException("Sorry, One or two things are wrong..");
	}

	public void updateCompany(Company company) throws DoesntExist, NoSuchThingLikeThisException {
		if (companiesService.getOneCompany(company.getId()) == null)
			throw new DoesntExist("ID does not exist");
		Company company2 = companiesService.getOneCompany1(company.getId());
		if (!company.getName().equals(company2.getName())) {
			throw new NoSuchThingLikeThisException("Cannot update company name");
		}
		companiesService.updateCompany(company);
	}

	public void addCoupon(Coupon coupon) throws AlreadyExistException {
		List<Coupon> coupons = couponsService.getAllCouponsByCompanyId(companyID);
		for (Coupon c : coupons) {
			if (c.getTitle().equalsIgnoreCase(coupon.getTitle())) {
				throw new AlreadyExistException("Sorry, CouponTitle already exist..");
			}
		}
//		Company company = companiesService.getOneCompany1(this.companyID);
//		coupon.setCompanyID(companyID);
		couponsService.addCoupon(coupon);
//		companiesService.updateCompany(company);

	}

	public void updateCoupon(Coupon coupon) throws DoesntExist, AlreadyExistException {

		Coupon coupon2 = couponsService.getOneCoupon(coupon.getId());

		if (companyID != coupon.getCompanyID()) {
			throw new AlreadyExistException("Can't change ID");
		} else if (!coupon.getTitle().equals(coupon2.getTitle())
				&& (couponsService.getOneCoupon(coupon.getId())) != null) {
			throw new AlreadyExistException("Coupon with title " + coupon.getTitle() + " already exist");

		}
		couponsService.updateCoupon(coupon);
	}

	public void deleteCoupon(int couponID) throws DoesntExist {

		if (couponsService.getOneCoupon(couponID) == null)
			throw new DoesntExist("ID not exist");
		Company company = companiesService.getOneCompany1(this.companyID);
		List<Customer> customers = customersService.getAllCustomers();
		for (Customer customer : customers) {
			customer.getCoupons().remove((couponsService.getOneCoupon(couponID)));
			customersService.updateCustomer(customer);
		}
		company.getCoupons().remove(couponsService.getOneCoupon(couponID));
		companiesService.updateCompany(company);

		List<Coupon> coupons = couponsService.getAllCoupons();
		for (Coupon c : coupons) {
			if (c.getId() == couponID) {
				couponsService.deleteCouponPurchase(couponID);
				couponsService.deleteCouponById(couponID);
				return;
			}
		}
		System.out.println("Not found matching coupons");
	}

	public List<Coupon> getCompanyCoupons() {
		if (couponsService.getAllCouponsByCompanyId(companyID) != null) {
			return couponsService.getAllCouponsByCompanyId(companyID);
		}
		System.out.println("This company doesn't have any coupons..");
		return null;
	}

	public ArrayList<Coupon> getCompanyCouponsByCategory(Category category) {
		List<Coupon> coupons = couponsService.getAllCouponsByCompanyId(companyID);
		ArrayList<Coupon> couponsByCategory = new ArrayList<>();
		for (Coupon c : coupons) {
			if (c.getCategory().equals(category)) {
				couponsByCategory.add(c);
			}
		}
		return couponsByCategory;
	}

	public ArrayList<Coupon> getCompanyCouponsByMaxPrice(double maxPrice) {
		List<Coupon> coupons = couponsService.getAllCouponsByCompanyId(companyID);
		ArrayList<Coupon> couponsByPrice = new ArrayList<>();
		for (Coupon c : coupons) {
			if (c.getPrice() <= maxPrice) {
				couponsByPrice.add(c);
			}
		}
		return couponsByPrice;

	}

	public Company getCompanyDetails() {
		Company company = companiesService.getOneCompany1(companyID);
		try {
			company.setCoupons(getCompanyCoupons());
			return company;
		} catch (Exception e) {
			System.out.println("This company doesn't have coupons..");
		}
		return null;
	}

}
