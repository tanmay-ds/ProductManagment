package com.example.productmngmt.model;


import org.springframework.http.HttpStatus;

public class ResponseModel {

	private Object timestamp;
	private HttpStatus status;
	private Object data;

	public ResponseModel(Object timestamp, HttpStatus status, Object data) {
		this.timestamp = timestamp;
		this.status = status;
		this.data = data;
	}

	public Object getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Object timestamp) {
		this.timestamp = timestamp;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
