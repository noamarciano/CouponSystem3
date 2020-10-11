package com.Noam.CouponSystem.part3.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Noam.CouponSystem.part3.beans.Category;
import com.Noam.CouponSystem.part3.beans.Coupon;
import com.Noam.CouponSystem.part3.beans.Customer;
import com.Noam.CouponSystem.part3.exceptions.DoesntExist;
import com.Noam.CouponSystem.part3.exceptions.LoginDeniedException;
import com.Noam.CouponSystem.part3.exceptions.NoSuchThingLikeThisException;
import com.Noam.CouponSystem.part3.exceptions.PurchasedCouponException;
import com.Noam.CouponSystem.part3.exceptions.TokenNotExist;
import com.Noam.CouponSystem.part3.security.ClientType;
import com.Noam.CouponSystem.part3.service.CustomerFacade;

@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
@RestController
@RequestMapping("customer")
public class CustomerController extends ClientController {

	@Autowired
	private CustomerFacade customerFacade;

	@PostMapping("login")
	public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
		HttpHeaders resHeaders = new HttpHeaders();
		try {
			String token = loginManager.login(email, password, ClientType.Customer);
			resHeaders.set("Token", token);
			resHeaders.add("Access-Control-Expose-Headers", "Token"); // set this to expose custom header
			return ResponseEntity.ok().headers(resHeaders).body("\"Logged in as customer \"");
		} catch (LoginDeniedException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
		}
	}

	@GetMapping("getCustomerDetails")
	public ResponseEntity<?> getCustomerDetails(@RequestHeader(name = "Token", required = false) String token) {
		try {
			tokenManager.isTokenExist(token);
			return new ResponseEntity<Customer>(
					((CustomerFacade) tokenManager.getMyService(token)).getCustomerDetails1(), HttpStatus.OK);
		} catch (TokenNotExist e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

	}

	@PutMapping("updateCustomer")
	public ResponseEntity<?> updateCustomer(@RequestBody Customer customer,
			@RequestHeader(name = "Token", required = false) String token) {

		try {
			tokenManager.isTokenExist(token);
			((CustomerFacade) tokenManager.getMyService(token)).updateCustomer(customer);
			return new ResponseEntity<>("\"Customer was updated\"", HttpStatus.NO_CONTENT);
		} catch (TokenNotExist e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
		} catch (DoesntExist e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (NoSuchThingLikeThisException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

	}

	@PostMapping("purchaseCoupon")
	public ResponseEntity<?> purchaseCoupon(@RequestBody Coupon coupon,
			@RequestHeader(name = "Token", required = false) String token)
			throws DoesntExist, NoSuchThingLikeThisException {

		try {
			tokenManager.isTokenExist(token);
			((CustomerFacade) tokenManager.getMyService(token)).purchaseCoupon(coupon);
			return new ResponseEntity<>("\"Coupon was purchased successfuly \"", HttpStatus.NO_CONTENT);
		} catch (TokenNotExist e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
		} catch (DoesntExist e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (PurchasedCouponException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

	}

	@DeleteMapping("deleteCouponPurchase/{id}")
	public ResponseEntity<?> deleteCoupon(@PathVariable int id,
			@RequestHeader(name = "Token", required = false) String token) {

		try {
			tokenManager.isTokenExist(token);
			((CustomerFacade) tokenManager.getMyService(token)).deleteCouponPurchase(id);
			return new ResponseEntity<>("\"Coupon purchase was deleted \"", HttpStatus.NO_CONTENT);
		} catch (TokenNotExist e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
		} catch (DoesntExist e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("getAllCoupons")
	public ResponseEntity<?> getAllCoupons(@RequestHeader(name = "Token", required = false) String token) {
		try {
			tokenManager.isTokenExist(token);
			return new ResponseEntity<List<Coupon>>(
					((CustomerFacade) tokenManager.getMyService(token)).getCustomerCoupons(), HttpStatus.OK);
		} catch (TokenNotExist e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
		}
	}

	@GetMapping("getAllCouponsByCategory/{category}")
	public ResponseEntity<?> getAllCouponsByCategory(@PathVariable Category category,
			@RequestHeader(name = "Token", required = false) String token) {
		try {
			tokenManager.isTokenExist(token);
			return new ResponseEntity<List<Coupon>>(
					((CustomerFacade) tokenManager.getMyService(token)).getCustomerCouponsByCategory(category),
					HttpStatus.OK);
		} catch (TokenNotExist e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
		}
	}

	@GetMapping("getAllCouponsUnderPrice/{price}")
	public ResponseEntity<?> getAllCouponsUnderPrice(@PathVariable double maxPrice,
			@RequestHeader(name = "Token", required = false) String token) {

		try {
			tokenManager.isTokenExist(token);
			return new ResponseEntity<List<Coupon>>(
					((CustomerFacade) tokenManager.getMyService(token)).getCustomerCouponsByMaxPrice(maxPrice),
					HttpStatus.OK);
		} catch (TokenNotExist e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
		}
	}
}
