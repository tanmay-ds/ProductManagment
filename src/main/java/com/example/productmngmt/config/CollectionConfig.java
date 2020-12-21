package com.example.productmngmt.config;

import java.util.Locale;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.query.Collation;

import com.example.productmngmt.entity.Product;

@Configuration
@DependsOn("mongoTemplate")
public class CollectionConfig {
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	@PostConstruct
	public void initIndexes() {
		mongoTemplate.indexOps(Product.class).ensureIndex(
				new Index().on("name", Sort.Direction.ASC).unique().collation(Collation.of(Locale.ENGLISH))
				);
	}

}
