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

import com.Noam.CouponSystem.part3.beans.Company;
import com.Noam.CouponSystem.part3.beans.Coupon;
import com.Noam.CouponSystem.part3.beans.Customer;
import com.Noam.CouponSystem.part3.exceptions.AlreadyExistException;
import com.Noam.CouponSystem.part3.exceptions.DoesntExist;
import com.Noam.CouponSystem.part3.exceptions.LoginDeniedException;
import com.Noam.CouponSystem.part3.exceptions.NoSuchThingLikeThisException;
import com.Noam.CouponSystem.part3.exceptions.TokenNotExist;
import com.Noam.CouponSystem.part3.security.ClientType;
import com.Noam.CouponSystem.part3.service.AdminFacade;

@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
@RestController
@RequestMapping("admin")
public class AdminController extends ClientController {

	@Autowired
	private AdminFacade adminFacade;

	@PostMapping("login")
	public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
		HttpHeaders headers = new HttpHeaders();
		try {
			String token = loginManager.login(email, password, ClientType.Administrator);
			headers.add("Token", token);
			headers.add("Access", "Token");
			return ResponseEntity.ok().headers(headers).body("\"Logged in as admin\"");
		} catch (LoginDeniedException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("addCompany")
	public ResponseEntity<?> addCompany(@RequestBody Company company,
			@RequestHeader(name = "Token", required = false) String token) {
		try {
			tokenManager.isTokenExist(token);
			((AdminFacade) tokenManager.getMyService(token)).addCompany(company);
			return new ResponseEntity<>("\"Company was added \"", HttpStatus.CREATED);
		} catch (TokenNotExist e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
		} catch (AlreadyExistException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

	}

	@PutMapping("updateCompany")
	public ResponseEntity<?> updateCompany(@RequestBody Company company,
			@RequestHeader(name = "Token", required = false) String token) {

		try {
			tokenManager.isTokenExist(token);
			((AdminFacade) tokenManager.getMyService(token)).updateCompany(company.getId(), company);
			return new ResponseEntity<>("\"Company was updated\"", HttpStatus.NO_CONTENT);
		} catch (TokenNotExist e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
		} catch (DoesntExist e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (NoSuchThingLikeThisException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping("deleteCompany/{id}")
	public ResponseEntity<?> deleteCompany(@PathVariable int id,
			@RequestHeader(name = "Token", required = false) String token) {

		try {
			tokenManager.isTokenExist(token);
			((AdminFacade) tokenManager.getMyService(token)).deleteCompany(id);
			return new ResponseEntity<>("\"Company was deleted \"", HttpStatus.NO_CONTENT);
		} catch (TokenNotExist e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
		} catch (DoesntExist e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

	}

	@GetMapping("getAllCompanies")
	public ResponseEntity<?> getAllCompanies(@RequestHeader(name = "Token", required = false) String token) {

		try {
			tokenManager.isTokenExist(token);
			return new ResponseEntity<List<Company>>(((AdminFacade) tokenManager.getMyService(token)).getAllCompanies(),
					HttpStatus.OK);
		} catch (TokenNotExist e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
		}

	}

	@GetMapping("getOneCompany/{id}")
	public ResponseEntity<?> getOneCompany(@PathVariable int id,
			@RequestHeader(name = "Token", required = false) String token) {
		try {
			tokenManager.isTokenExist(token);
			return new ResponseEntity<Company>(((AdminFacade) tokenManager.getMyService(token)).getOneCompany1(id),
					HttpStatus.OK);
		} catch (TokenNotExist e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
		} catch (DoesntExist e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("addCustomer")
	public ResponseEntity<?> addCustomer(@RequestBody Customer customer,
			@RequestHeader(name = "Token", required = false) String token) {

		try {
			tokenManager.isTokenExist(token);
			((AdminFacade) tokenManager.getMyService(token)).addCustomer(customer);
			return new ResponseEntity<>("\"Customer was added \"", HttpStatus.CREATED);
		} catch (TokenNotExist e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
		} catch (AlreadyExistException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("updateCustomer")
	public ResponseEntity<?> updateCustomer(@RequestBody Customer customer,
			@RequestHeader(name = "Token", required = false) String token) {

		try {
			tokenManager.isTokenExist(token);
			((AdminFacade) tokenManager.getMyService(token)).updateCustomer(customer);
			return new ResponseEntity<>("\"Customer was updated\"", HttpStatus.NO_CONTENT);
		} catch (TokenNotExist e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
		} catch (DoesntExist e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (NoSuchThingLikeThisException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

	}

	@DeleteMapping("deleteCustomer/{id}")
	public ResponseEntity<?> deleteCustomer(@PathVariable int id,
			@RequestHeader(name = "Token", required = false) String token) {

		try {
			tokenManager.isTokenExist(token);
			((AdminFacade) tokenManager.getMyService(token)).deleteCustomer(id);
			return new ResponseEntity<>("\"Company was deleted \"", HttpStatus.NO_CONTENT);
		} catch (TokenNotExist e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
		} catch (DoesntExist e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

	}

	@GetMapping("getAllCustomers")
	public ResponseEntity<?> getAllCustomers(@RequestHeader(name = "Token", required = false) String token) {
		try {
			tokenManager.isTokenExist(token);
			return new ResponseEntity<List<Customer>>(
					((AdminFacade) tokenManager.getMyService(token)).getAllCustomers(), HttpStatus.OK);
		} catch (TokenNotExist e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
		}
	}

	@GetMapping("getOneCustomer/{id}")
	public ResponseEntity<?> getOneCustomer(@PathVariable int id,
			@RequestHeader(name = "Token", required = false) String token) {
		try {
			tokenManager.isTokenExist(token);
			return new ResponseEntity<Customer>(((AdminFacade) tokenManager.getMyService(token)).getOneCustomer1(id),
					HttpStatus.OK);
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
					((AdminFacade) tokenManager.getMyService(token)).getCouponsService().getAllCoupons(),
					HttpStatus.OK);
		} catch (TokenNotExist e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
		}

	}

}
