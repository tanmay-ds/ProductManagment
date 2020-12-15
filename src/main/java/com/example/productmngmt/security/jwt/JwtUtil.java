package com.example.productmngmt.security.jwt;

import org.springframework.security.core.context.SecurityContextHolder;

import com.example.productmngmt.model.MyUserDetails;

public class JwtUtil {
	
	private static String jwt;
	
	private JwtUtil() {
	    throw new IllegalStateException("JWT util");
	  }


	public static Long getUuidFromToken() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof MyUserDetails) {
			return ((MyUserDetails) principal).getUser().getUuid();
		} else {
			return null;
		}
	}

	public static void setToken(String jwt) {
		JwtUtil.jwt = jwt;		
	}
	
	public static String getToken() {
		return jwt;
	}

}
