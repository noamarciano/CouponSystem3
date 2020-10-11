package com.Noam.CouponSystem.part3.security;

import java.util.Date;

import com.Noam.CouponSystem.part3.service.ClientFacade;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomSession {

	private ClientFacade clientFacade;
	private Date date;

}
