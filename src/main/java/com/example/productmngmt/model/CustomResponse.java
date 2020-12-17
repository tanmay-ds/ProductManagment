package com.example.productmngmt.model;

public class CustomResponse {

	private final Object data;

	public CustomResponse() {
		this(null);
	}

	public CustomResponse(Object data) {
		super();
		this.data = data;
	}

	public Object getData() {
		return data;
	}

}
