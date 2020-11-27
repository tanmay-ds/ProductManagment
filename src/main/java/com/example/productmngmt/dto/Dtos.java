package com.example.productmngmt.dto;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.productmngmt.entity.Product;

@Service
public class Dtos {

	public Product optionalToProduct(Optional<Product> optional) {
		Product product = new Product();
		product.setProdId(optional.get().getProdId());
		product.setName(optional.get().getName());
		product.setBrand(optional.get().getBrand());
		product.setPrice(optional.get().getPrice());
		product.setDetails(optional.get().getDetails());
		product.setQuantity(optional.get().getQuantity());
		return product;
	}
}
