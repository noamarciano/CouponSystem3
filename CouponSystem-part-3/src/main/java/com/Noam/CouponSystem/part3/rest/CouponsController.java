package com.Noam.CouponSystem.part3.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Noam.CouponSystem.part3.beans.Coupon;
import com.Noam.CouponSystem.part3.service.CouponsService;

@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
@RestController
@RequestMapping("coupon")
public class CouponsController {

	@Autowired
	private CouponsService couponsService;

	@GetMapping("getOneCoupon/{id}")
	public ResponseEntity<?> getOneCoupon(@PathVariable int id) {
		return new ResponseEntity<Coupon>(couponsService.getOneCoupon(id), HttpStatus.OK);
	}

	@GetMapping("getAllCoupons")
	public ResponseEntity<?> getAllCoupons() {
		return new ResponseEntity<List<Coupon>>(couponsService.getAllCoupons(), HttpStatus.OK);
	}

}
