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
import com.Noam.CouponSystem.part3.beans.Company;
import com.Noam.CouponSystem.part3.beans.Coupon;
import com.Noam.CouponSystem.part3.exceptions.AlreadyExistException;
import com.Noam.CouponSystem.part3.exceptions.DoesntExist;
import com.Noam.CouponSystem.part3.exceptions.LoginDeniedException;
import com.Noam.CouponSystem.part3.exceptions.NoSuchThingLikeThisException;
import com.Noam.CouponSystem.part3.exceptions.TokenNotExist;
import com.Noam.CouponSystem.part3.security.ClientType;
import com.Noam.CouponSystem.part3.service.CompanyFacade;

@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
@RestController
@RequestMapping("company")
public class CompanyController extends ClientController {

	@Autowired
	private CompanyFacade companyFacade;

	@PostMapping("login")
	public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
		HttpHeaders resHeaders = new HttpHeaders();
		try {
			String token = loginManager.login(email, password, ClientType.Company);
			resHeaders.set("Token", token);
			resHeaders.add("Access-Control-Expose-Headers", "Token"); // set this to expose custom header
			return ResponseEntity.ok().headers(resHeaders).body("\"Logged in as company \"");
		} catch (LoginDeniedException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
		}
	}

	@GetMapping("getCompanyDetails")
	public ResponseEntity<?> getCompanyDetails(@RequestHeader(name = "Token", required = false) String token) {
		try {
			tokenManager.isTokenExist(token);
			return new ResponseEntity<Company>(((CompanyFacade) tokenManager.getMyService(token)).getCompanyDetails(),
					HttpStatus.OK);
		} catch (TokenNotExist e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

	}

	@PutMapping("updateCompany")
	public ResponseEntity<?> updateCompany(@RequestBody Company company,
			@RequestHeader(name = "Token", required = false) String token) {

		try {
			tokenManager.isTokenExist(token);
			((CompanyFacade) tokenManager.getMyService(token)).updateCompany(company);
			return new ResponseEntity<>("\"Company was updated\"", HttpStatus.NO_CONTENT);
		} catch (TokenNotExist e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
		} catch (DoesntExist e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (NoSuchThingLikeThisException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

	}

	@PostMapping("addCoupon")
	public ResponseEntity<?> addCoupon(@RequestBody Coupon coupon,
			@RequestHeader(name = "Token", required = false) String token) {
		try {
			tokenManager.isTokenExist(token);
			((CompanyFacade) tokenManager.getMyService(token)).addCoupon(coupon);
			return new ResponseEntity<>("\"Coupon was added \"", HttpStatus.CREATED);
		} catch (TokenNotExist e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
		} catch (AlreadyExistException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

	}

	@PutMapping("updateCoupon")
	public ResponseEntity<?> updateCoupon(@RequestBody Coupon coupon,
			@RequestHeader(name = "Token", required = false) String token) {

		try {
			tokenManager.isTokenExist(token);
			((CompanyFacade) tokenManager.getMyService(token)).updateCoupon(coupon);
			return new ResponseEntity<>("Coupon was updated", HttpStatus.NO_CONTENT);
		} catch (TokenNotExist e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
		} catch (DoesntExist e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (AlreadyExistException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

	}

	@DeleteMapping("deleteCoupon/{id}")
	public ResponseEntity<?> deleteCoupon(@PathVariable int id,
			@RequestHeader(name = "Token", required = false) String token) {

		try {
			tokenManager.isTokenExist(token);
			((CompanyFacade) tokenManager.getMyService(token)).deleteCoupon(id);
			return new ResponseEntity<>("\"Coupon was deleted \"", HttpStatus.NO_CONTENT);
		} catch (TokenNotExist e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
		} catch (DoesntExist e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("getOneCoupon/{id}")
	public ResponseEntity<?> getOneCoupon(@PathVariable int id,
			@RequestHeader(name = "Token", required = false) String token) {
		try {
			tokenManager.isTokenExist(token);
			return new ResponseEntity<Coupon>(
					((CompanyFacade) tokenManager.getMyService(token)).getCouponsService().getOneCoupon(id),
					HttpStatus.OK);
		} catch (TokenNotExist e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
		}
	}

	@GetMapping("getAllCoupons")
	public ResponseEntity<?> getAllCoupons(@RequestHeader(name = "Token", required = false) String token) {
		try {
			tokenManager.isTokenExist(token);
			return new ResponseEntity<List<Coupon>>(
					((CompanyFacade) tokenManager.getMyService(token)).getCouponsService().getAllCoupons(),
					HttpStatus.OK);
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
					((CompanyFacade) tokenManager.getMyService(token)).getCompanyCouponsByCategory(category),
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
					((CompanyFacade) tokenManager.getMyService(token)).getCompanyCouponsByMaxPrice(maxPrice),
					HttpStatus.OK);
		} catch (TokenNotExist e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
		}

	}

}
