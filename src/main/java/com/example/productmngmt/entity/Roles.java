package com.example.productmngmt.entity;

import java.io.Serializable;

public class Roles implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String role;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
	

}
