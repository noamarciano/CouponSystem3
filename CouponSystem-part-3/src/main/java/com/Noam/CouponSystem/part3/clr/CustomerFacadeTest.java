package com.Noam.CouponSystem.part3.clr;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.Noam.CouponSystem.part3.beans.Category;
import com.Noam.CouponSystem.part3.beans.Coupon;
import com.Noam.CouponSystem.part3.beans.Customer;
import com.Noam.CouponSystem.part3.exceptions.DoesntExist;
import com.Noam.CouponSystem.part3.exceptions.LoginDeniedException;
import com.Noam.CouponSystem.part3.exceptions.NoSuchThingLikeThisException;
import com.Noam.CouponSystem.part3.exceptions.PurchasedCouponException;
import com.Noam.CouponSystem.part3.security.ClientType;
import com.Noam.CouponSystem.part3.security.LoginManager;
import com.Noam.CouponSystem.part3.service.CompanyFacade;
import com.Noam.CouponSystem.part3.service.CouponsService;
import com.Noam.CouponSystem.part3.service.CustomerFacade;
import com.Noam.CouponSystem.part3.utils.CheckTitle;
import com.Noam.CouponSystem.part3.utils.DateUtils;

@Component
@Order(6)
public class CustomerFacadeTest implements CommandLineRunner {

	@Autowired
	CustomerFacade customerFacade;

	@Autowired
	CouponsService couponsService;

	@Autowired
	LoginManager loginManager;

	@Override
	public void run(String... args) throws Exception {
		CheckTitle.customerFacadeCheck();

		CustomerFacade danaCustomer = null;

		CheckTitle.printTestLine("Customer Facade - !WRONG! login test");
		try {
			danaCustomer = (CustomerFacade) loginManager.login1("mosh@gmail.com", "1234", ClientType.Customer);
		} catch (LoginDeniedException e) {
			System.out.println(e.getMessage());
		}

		CheckTitle.printTestLine("Customer Facade - Real login test");
		danaCustomer = null;
		try {
			danaCustomer = (CustomerFacade) loginManager.login1("dana@gmail.com", "1234", ClientType.Customer);
			danaCustomer.setCustomerID(3);
		} catch (LoginDeniedException e) {
			System.out.println(e.getMessage());
		}

		CheckTitle.printTestLine("Customer Facade - purchase coupon");

		Coupon coupon = new Coupon();
		coupon.setCompanyID(3);
		coupon.setCategory(Category.Electricity);
		coupon.setTitle("blala");
		coupon.setDescription("????");
		coupon.setStartDate(DateUtils.convertUtilDateToSQL(new Date(2021, 07, 15)));
		coupon.setEndDate(DateUtils.convertUtilDateToSQL(new Date(2022, 01, 17)));
		coupon.setAmount(500);
		coupon.setPrice(49);
		coupon.setImage("img.comm");

		Coupon coupon1 = new Coupon();
		coupon1.setCompanyID(3);
		coupon1.setCategory(Category.Vacation);
		coupon1.setTitle("Eilat");
		coupon1.setDescription("!!!!");
		coupon1.setStartDate(DateUtils.convertUtilDateToSQL(new Date(2022, 06, 15)));
		coupon1.setEndDate(DateUtils.convertUtilDateToSQL(new Date(2022, 11, 17)));
		coupon1.setAmount(400);
		coupon1.setPrice(129);
		coupon1.setImage("img.comm");

		try {
			customerFacade.purchaseCoupon(coupon1);
			customerFacade.purchaseCoupon(coupon);
		} catch (PurchasedCouponException e) {
			System.out.println(e.getMessage());
		} catch (DoesntExist e) {
			System.out.println(e.getMessage());
		}
		CheckTitle.printCouponsTable(customerFacade.getCustomerCoupons());

		CheckTitle.printTestLine("Customer Facade - !WRONG! purchase coupon (amount - 0 )");

		try {
			coupon.setAmount(0);
			customerFacade.purchaseCoupon(coupon);
		} catch (PurchasedCouponException e) {
			System.out.println(e.getMessage());
		} catch (DoesntExist e) {
			System.out.println(e.getMessage());
		}

		CheckTitle.printCouponsTable(customerFacade.getCustomerCoupons());

		CheckTitle.printTestLine("Customer Facade - !WRONG! purchase coupon (expired)");

		try {

			Coupon coupon4 = new Coupon();
			coupon4.setCompanyID(3);
			coupon4.setCategory(Category.Electricity);
			coupon4.setTitle("blala");
			coupon4.setDescription("????");
			coupon4.setStartDate(DateUtils.convertUtilDateToSQL(new Date(2021, 07, 15)));
			coupon4.setEndDate(DateUtils.convertUtilDateToSQL(new Date(2020, 07, 23)));
			coupon4.setAmount(0);
			coupon4.setPrice(49);
			coupon4.setImage("img.comm");

//			coupon.setAmount(50);
//			coupon.setEndDate(DateUtils.convertUtilDateToSQL(new Date(2020, 07, 23)));
			customerFacade.purchaseCoupon(coupon4);
		} catch (PurchasedCouponException e) {
			System.out.println(e.getMessage());
		} catch (DoesntExist e) {
			System.out.println(e.getMessage());
		}

		CheckTitle.printCouponsTable(customerFacade.getCustomerCoupons());

		CheckTitle.printTestLine("Customer Facade - get customer coupons");

		CheckTitle.printCouponsTable(customerFacade.getCustomerCoupons());

		CheckTitle.printTestLine("Customer Facade - get customer coupons by category");

		CheckTitle.printCouponsTable(customerFacade.getCustomerCouponsByCategory(Category.Electricity));

		CheckTitle.printTestLine("Customer Facade - !WRONG! get customer coupons by category");

		CheckTitle.printCouponsTable(customerFacade.getCustomerCouponsByCategory(Category.Food));

		CheckTitle.printTestLine("Customer Facade - get customer coupons by maxPrice");

		CheckTitle.printCouponsTable(customerFacade.getCustomerCouponsByMaxPrice(89));

		CheckTitle.printTestLine("Customer Facade - !WRONG! get customer coupons by maxPrice");

		CheckTitle.printCouponsTable(customerFacade.getCustomerCouponsByMaxPrice(4.99));

		CheckTitle.printTestLine("Customer Facade - get customer details");
		CheckTitle.printOneCustomer(customerFacade.getCustomerDetails());

		CheckTitle.separatorLine();

	}

}
