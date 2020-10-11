package com.Noam.CouponSystem.part3.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.Noam.CouponSystem.part3.exceptions.TokenNotExist;
import com.Noam.CouponSystem.part3.service.ClientFacade;

@Component
public class TokenManager {

	private static final long deleteTime = 100 * 30 * 60;

	private Map<String, CustomSession> tokens = new HashMap<String, CustomSession>();

	public String addToken(ClientFacade clientFacade) {
		String token = UUID.randomUUID().toString();
		CustomSession customSession = new CustomSession(clientFacade, new Date());
		tokens.put(token, customSession);
		return token;
	}

	public void deleteToken(String token) {
		tokens.remove(token);

	}

	public boolean isTokenExist(String token) throws TokenNotExist {

		if (tokens.get(token) != null) {
			return true;
		}

		throw new TokenNotExist();
	}

	public ClientFacade getMyService(String token) {
		return tokens.get(token).getClientFacade();
	}

	public void deleteExpieredTokens() {

		for (Map.Entry<String, CustomSession> entry : tokens.entrySet()) {
			if (System.currentTimeMillis() - entry.getValue().getDate().getTime() > deleteTime)
				tokens.remove(entry.getKey());
		}
	}

}
