package com.Noam.CouponSystem.part3.schedule;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.Noam.CouponSystem.part3.beans.Coupon;
import com.Noam.CouponSystem.part3.exceptions.DoesntExist;
import com.Noam.CouponSystem.part3.service.CompanyFacade;
import com.Noam.CouponSystem.part3.service.CouponsService;

@Component
public class DailyJob implements Runnable {

	@Autowired
	private CouponsService couponsService;

	@Autowired
	private CompanyFacade companyFacade;

	private boolean quit = false;

	public DailyJob() {
		super();
	}

	@Scheduled(fixedDelay = 1000 * 10)
	@Override
	public void run() {

		while (!quit) {
			List<Coupon> coupons = couponsService.getAllCoupons();
			for (Coupon coupon : coupons) {
				if (coupon.getEndDate().before(new Date())) {
					System.out.println("Coupon " + coupon.getId() + " deleted");
					try {
						companyFacade.deleteCoupon(coupon.getId());
					} catch (DoesntExist e) {
						System.out.println("Coupon not exist");
					}
				}
			}
			try {
				Thread.sleep(2 * 1000);
			} catch (Exception e) {
				return;
			}
		}
	}

	public void stopJob() {
		this.quit = true;
		System.out.println("DailyJob is stopped");

	}

}
