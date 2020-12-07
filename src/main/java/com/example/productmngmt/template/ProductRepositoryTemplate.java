package com.example.productmngmt.template;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.productmngmt.entity.Product;

public interface ProductRepositoryTemplate {
	
	public List<Product> findProductByNameIgnoreCase(String productName);
	
	public Page<Product> findByNamePartialSearch(String regex,Pageable pageable);

}
