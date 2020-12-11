package com.example.productmngmt.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class Users implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Transient
	public static final String SEQUENCE_NAME = "user_sequence";

	@Id
	private Long uuid;
	@NotNull
	private String firstName;
	@NotNull
	private String lastName;
	@NotNull
	private String email;
	@NotNull
	private String phoneNumber;
	@NotNull
	private String address;

	@NotNull
	private String password;

	private List<Roles> roles = new ArrayList<>();

	public Long getUuid() {
		return uuid;
	}

	public void setUuid(Long uuid) {
		this.uuid = uuid;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Roles> getRoles() {
		return roles;
	}

	public void setRoles(List<Roles> roles) {
		this.roles = roles;
	}

}
