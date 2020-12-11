package com.example.productmngmt.exceptionhandler;

public class BadCredsException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public BadCredsException(String message) {
		super(message);
	}
}
