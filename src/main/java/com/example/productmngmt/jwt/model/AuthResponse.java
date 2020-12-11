package com.example.productmngmt.jwt.model;

import java.io.Serializable;

public class AuthResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String jwt;

	public AuthResponse(String jwt) {
		this.jwt = jwt;
	}

	public String getJwt() {
		return jwt;
	}

}
