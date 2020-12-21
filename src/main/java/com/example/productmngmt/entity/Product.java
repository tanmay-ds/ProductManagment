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
	private String id;
	
	private Long prodId;
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

	public Product() {
	}

	public Product(long prodId, @NotNull String name, @NotNull String brand, @NotNull @Min(0) Long price,
			@NotNull String details, @Min(0) Long quantity) {
		this.prodId = prodId;
		this.name = name;
		this.brand = brand;
		this.price = price;
		this.details = details;
		this.quantity = quantity;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getProdId() {
		return prodId;
	}

	public void setProdId(Long prodId) {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((brand == null) ? 0 : brand.hashCode());
		result = prime * result + ((details == null) ? 0 : details.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((price == null) ? 0 : price.hashCode());
		result = prime * result + (int) (prodId ^ (prodId >>> 32));
		result = prime * result + ((quantity == null) ? 0 : quantity.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		if (brand == null || details == null || name == null || price == null || quantity == null) {
			if (other.brand != null || other.details != null || other.name != null || other.price != null
					|| other.quantity != null || prodId != other.prodId)
				return false;
		} else if (!brand.equals(other.brand) || !details.equals(other.details) || !name.equals(other.name)
				|| !price.equals(other.price) || !quantity.equals(other.quantity))
			return false;

		return true;
	}

}
