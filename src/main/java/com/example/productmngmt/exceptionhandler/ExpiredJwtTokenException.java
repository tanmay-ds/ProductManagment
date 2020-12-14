package com.example.productmngmt.exceptionhandler;

import io.jsonwebtoken.JwtException;

public class ExpiredJwtTokenException extends JwtException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ExpiredJwtTokenException(String message) {
		super(message);
	}


}
