package com.example.productmngmt.exceptionhandler;

import java.util.Date;

import org.springframework.http.HttpStatus;

public class ResponseMessage {

	private Date timestamp;
	private HttpStatus status;
	private String message;

	
	
	public ResponseMessage() {
	}

	public ResponseMessage(Date timestamp, HttpStatus status,String message) {
		super();
		this.timestamp = timestamp;
		this.status = status;
		this.message = message;
	}
	
	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
