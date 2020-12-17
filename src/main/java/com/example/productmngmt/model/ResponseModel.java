package com.example.productmngmt.model;

import java.util.Date;

import org.springframework.http.HttpStatus;

public class ResponseModel {

	private Date timestamp;
	private HttpStatus status;
	private Object data;

	public ResponseModel() {
	}

	public ResponseModel(Date timestamp, HttpStatus status, Object data) {
		super();
		this.timestamp = timestamp;
		this.status = status;
		this.data = data;
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

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
