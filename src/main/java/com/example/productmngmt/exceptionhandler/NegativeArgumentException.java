package com.example.productmngmt.exceptionhandler;

public class NegativeArgumentException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NegativeArgumentException(String message) {
		super(message);
	}

}
