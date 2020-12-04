package com.example.productmngmt.entity;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "Product")
public class Product {

	@Transient
	public static final String SEQUENCE_NAME = "product_sequence";

	@Id
	private long prodId;
	@NotNull
	private String name;
	@NotNull
	private String brand;
	@NotNull
	@Min(0)
	private Long price;
	@NotNull
	private String details;
	@Min(0)
	private Long quantity;

	public long getProdId() {
		return prodId;
	}

	public void setProdId(long prodId) {
		this.prodId = prodId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "Product [prodId=" + prodId + ", name=" + name + ", brand=" + brand + ", price=" + price + ", details="
				+ details + ", quantity=" + quantity + "]";
	}

	

}
