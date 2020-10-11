package com.Noam.CouponSystem.part3.clr;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.Noam.CouponSystem.part3.beans.Category;
import com.Noam.CouponSystem.part3.beans.Company;
import com.Noam.CouponSystem.part3.beans.Coupon;
import com.Noam.CouponSystem.part3.exceptions.AlreadyExistException;
import com.Noam.CouponSystem.part3.exceptions.DoesntExist;
import com.Noam.CouponSystem.part3.exceptions.LoginDeniedException;
import com.Noam.CouponSystem.part3.security.ClientType;
import com.Noam.CouponSystem.part3.security.LoginManager;
import com.Noam.CouponSystem.part3.service.CompaniesService;
import com.Noam.CouponSystem.part3.service.CompanyFacade;
import com.Noam.CouponSystem.part3.service.CouponsService;
import com.Noam.CouponSystem.part3.utils.CheckTitle;
import com.Noam.CouponSystem.part3.utils.DateUtils;

@Component
@Order(5)
public class CompanyFacadeTest implements CommandLineRunner {

	@Autowired
	CompanyFacade companyFacade;

	@Autowired
	CompaniesService CompaniesService;

	@Autowired
	CouponsService couponsService;

	@Autowired
	LoginManager loginManager;

	@Override
	public void run(String... args) throws Exception {

		CheckTitle.companyFacadeCheck();

		CompanyFacade burgerKingCompany = null;

		CheckTitle.printTestLine("Company Facade - !WRONG! login test");
		try {
			burgerKingCompany = (CompanyFacade) loginManager.login1("burger@company.com", "1234", ClientType.Company);
		} catch (LoginDeniedException e) {
			System.out.println(e.getMessage());
		}

		CheckTitle.printTestLine("Company Facade - Real login test");
		burgerKingCompany = null;
		try {
			burgerKingCompany = (CompanyFacade) loginManager.login1("burgerking@company.com", "1234",
					ClientType.Company);
		} catch (LoginDeniedException e) {
			System.out.println(e.getMessage());
		}

		burgerKingCompany.setCompanyID(3);

		CheckTitle.printTestLine("Company Facade - add coupons");

		Coupon cou4 = new Coupon();
		cou4.setCompanyID(3);
		cou4.setCategory(Category.Restaurant);
		cou4.setTitle("1+2");
		cou4.setDescription("buy one, get one");
		cou4.setStartDate(new Date(2020, 06, 23));
		cou4.setEndDate(new Date(2021, 03, 11));
		cou4.setAmount(100);
		cou4.setPrice(59.9);
		cou4.setImage("img.com");

		Coupon cou5 = new Coupon();
		cou5.setCompanyID(3);
		cou5.setCategory(Category.Food);
		cou5.setTitle("Free fries");
		cou5.setDescription("buy a meal and get fries for FREE!");
		cou5.setStartDate(new Date(2019, 06, 10));
		cou5.setEndDate(new Date(2021, 07, 11));
		cou5.setAmount(70);
		cou5.setPrice(49.9);
		cou5.setImage("img.com");

		try {
			burgerKingCompany.addCoupon(cou4);
			burgerKingCompany.addCoupon(cou5);

		} catch (AlreadyExistException e) {
			System.out.println(e.getMessage());
		}
		CheckTitle.printCouponsTable(companyFacade.getCompanyCoupons());

		CheckTitle.printTestLine("Company Facade - !WRONG! add coupons");

		Coupon cou6 = new Coupon();
		cou6.setCompanyID(3);
		cou6.setCategory(Category.Food);
		cou6.setTitle("Free fries");
		cou6.setDescription("buy a meal and get fries for FREE!");
		cou6.setStartDate(new Date(2021, 03, 15));
		cou6.setEndDate(new Date(2022, 02, 17));
		cou6.setAmount(40);
		cou6.setPrice(49.9);
		cou6.setImage("img.com");

		Coupon cou7 = new Coupon();
		cou7.setCompanyID(3);
		cou7.setCategory(Category.Food);
		cou7.setTitle("FreeFries");
		cou7.setDescription("buy a meal and get fries for free");
		cou7.setStartDate(new Date(2021, 07, 15));
		cou7.setEndDate(new Date(2022, 01, 17));
		cou7.setAmount(71);
		cou7.setPrice(49);
		cou7.setImage("img.comm");

		try {
			companyFacade.addCoupon(cou6);
			companyFacade.addCoupon(cou7);
		} catch (AlreadyExistException e) {
			System.out.println(e.getMessage());
		}
		CheckTitle.printCouponsTable(companyFacade.getCompanyCoupons());

		CheckTitle.printTestLine("Company Facade - update coupon");

		try {
			Coupon coupon = couponsService.getOneCoupon(5);

			coupon.setTitle("FreeFries");
			coupon.setDescription("buy a meal and get fries");
			coupon.setStartDate(DateUtils.convertUtilDateToSQL(new Date(2020, 1, 1)));
			coupon.setEndDate(DateUtils.convertUtilDateToSQL(new Date(2020, 2, 2)));
			coupon.setAmount(40);
			coupon.setPrice(59.9);
			coupon.setImage("img.comm");
			companyFacade.updateCoupon(coupon);
		} catch (Exception e1) {
			System.out.println(e1.getMessage());
		}

		CheckTitle.printCouponsTable(companyFacade.getCompanyCoupons());

		CheckTitle.printTestLine("Company Facade - !WRONG! update coupon");

		try {
			Coupon coupon = couponsService.getOneCoupon(5);

			coupon.setId(10);
			coupon.setCompanyID(15);
			companyFacade.updateCoupon(coupon);
		} catch (DoesntExist e) {
			System.out.println(e.getMessage());
		} catch (AlreadyExistException e) {
			System.out.println(e.getMessage());
		}
		CheckTitle.printCouponsTable(companyFacade.getCompanyCoupons());

		CheckTitle.printTestLine("Company Facade - delete coupon");

		companyFacade.deleteCoupon(4);
		CheckTitle.printCouponsTable(companyFacade.getCompanyCoupons());
		System.out.println();

		CheckTitle.printTestLine("Company Facade - !WRONG! delete coupon");
		companyFacade.deleteCoupon(9);
		CheckTitle.printCouponsTable(companyFacade.getCompanyCoupons());

		CheckTitle.printTestLine("Company Facade - get company coupons");
		CheckTitle.printCouponsTable(companyFacade.getCompanyCoupons());

		CheckTitle.printTestLine("Company Facade - get company coupons by category");

		CheckTitle.printCouponsTable(companyFacade.getCompanyCouponsByCategory(Category.Food));

		CheckTitle.printTestLine("Company Facade - !WRONG! get company coupons by category");

		CheckTitle.printCouponsTable(companyFacade.getCompanyCouponsByCategory(Category.Electricity));

		CheckTitle.printTestLine("Company Facade - get company coupons by maxPrice");

		CheckTitle.printCouponsTable(companyFacade.getCompanyCouponsByMaxPrice(52));

		CheckTitle.printTestLine("Company Facade - !WRONG! get company coupons by maxPrice");

		CheckTitle.printCouponsTable(companyFacade.getCompanyCouponsByMaxPrice(5.99));

		CheckTitle.printTestLine("Company Facade - get company details");
		CheckTitle.printOneCompany(companyFacade.getCompanyDetails());

		CheckTitle.separatorLine();

	}

}
