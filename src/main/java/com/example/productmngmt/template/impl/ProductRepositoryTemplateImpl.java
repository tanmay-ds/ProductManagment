package com.example.productmngmt.template.impl;

import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.example.productmngmt.entity.Product;
import com.example.productmngmt.template.ProductRepositoryTemplate;

public class ProductRepositoryTemplateImpl implements ProductRepositoryTemplate{
	
	
	@Autowired
	private MongoTemplate mongoTeamplate;

	@Override
	public List<Product> findProductByNameIgnoreCase(String productName) {
		Query query = new Query().addCriteria(Criteria.where("name").regex(Pattern.compile(productName,Pattern.CASE_INSENSITIVE)));
		return mongoTeamplate.find(query, Product.class);
		}

	@Override
	public Page<Product> findByNamePartialSearch(String regex, Pageable pageable) {
		Query query = new Query().addCriteria(Criteria.where("name").regex(Pattern.compile(regex,Pattern.CASE_INSENSITIVE))).with(pageable);
		
		List<Product> products = mongoTeamplate.find(query, Product.class);
		Long count = mongoTeamplate.count(query,Product.class);
		
		return new PageImpl<>(products, pageable, count);
	}
}
