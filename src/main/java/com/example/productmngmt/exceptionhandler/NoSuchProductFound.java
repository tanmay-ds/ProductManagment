package com.example.productmngmt.exceptionhandler;


public class NoSuchProductFound extends RuntimeException{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoSuchProductFound(String message) {
		super(message);
	}

}
