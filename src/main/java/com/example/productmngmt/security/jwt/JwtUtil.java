package com.example.productmngmt.security.jwt;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.example.productmngmt.model.MyUserDetails;

@Component
public class JwtUtil {

	private String jwt;

	public static Long getUuidFromToken() {

		MyUserDetails principal = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof MyUserDetails) {
			return principal.getUser().getUuid();
		} else {
			return null;
		}
	}

	public void setToken(String jwt) {
		this.jwt = jwt;
	}

	public String getToken() {
		return jwt;
	}

}
