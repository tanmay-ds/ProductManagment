package com.example.productmngmt.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.productmngmt.dto.ProductDto;
import com.example.productmngmt.entity.Product;

public interface ProductService {

	public List<String> create(List<ProductDto> productsDto);
	
	public Product getProductById(Long pid);
	
	public Page<Product> getAll(Pageable pageable);
	
	public Page<Product> getAll(String search, Pageable pageable);
	
	public Product updateProd(Long pid, ProductDto productDto);
	
	public Long deleteProd(Long pid);
	
	public String addStock(Map<Long, Long> stockList);
	
	public String removeStock(Map<Long, Long> stockList);
	
	
}
