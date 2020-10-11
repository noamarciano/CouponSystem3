package com.Noam.CouponSystem.part3.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Noam.CouponSystem.part3.exceptions.LoginDeniedException;
import com.Noam.CouponSystem.part3.service.AdminFacade;
import com.Noam.CouponSystem.part3.service.ClientFacade;
import com.Noam.CouponSystem.part3.service.CompanyFacade;
import com.Noam.CouponSystem.part3.service.CustomerFacade;

@Service
public class LoginManager {

	@Autowired
	private AdminFacade adminFacade;

	@Autowired
	private CompanyFacade companyFacade;

	@Autowired
	private CustomerFacade customerFacade;

	@Autowired
	TokenManager tokenManager;

	public ClientFacade login1(String email, String password, ClientType clientType) throws LoginDeniedException {

		switch (clientType) {
		case Administrator:
			if (adminFacade.login(email, password)) {
				System.out.println("Admin Facade - Login successful");
				return adminFacade;
			} else {
				return null;
			}
		case Company:

			if (companyFacade.login(email, password)) {
				System.out.println("Company Facade - Login successful");
				return companyFacade;
			} else {
				return null;
			}

		case Customer:
			if (customerFacade.login(email, password)) {
				System.out.println("Cuatomer Facade - Login successful");
				return customerFacade;
			} else {
				return null;
			}
		default:
			System.out.println("One or more details are wrong, please try again..");
			return null;
		}
	}

	public String login(String email, String password, ClientType clientType) throws LoginDeniedException {

		switch (clientType) {
		case Administrator: {
			if (adminFacade.login(email, password))
				return tokenManager.addToken(adminFacade);
		}
		case Company: {
			if (companyFacade.login(email, password))
				return tokenManager.addToken(companyFacade);
		}
		case Customer: {
			if (customerFacade.login(email, password))
				return tokenManager.addToken(customerFacade);
		}
		default:
			throw new LoginDeniedException("One or more details are wrong, please try again..");
		}
	}

}
