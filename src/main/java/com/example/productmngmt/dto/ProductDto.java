package com.example.productmngmt.dto;

public class ProductDto {

	private String name;
	private String brand;
	private Long price;
	private String details;
	private Long quantity;

	public ProductDto(String name, String brand, Long price, String details, Long quantity) {
		this.name = name;
		this.brand = brand;
		this.price = price;
		this.details = details;
		this.quantity = quantity;
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

}
