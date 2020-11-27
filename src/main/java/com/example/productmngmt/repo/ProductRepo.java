package com.example.productmngmt.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.productmngmt.entity.Product;

public interface ProductRepo extends MongoRepository<Product,Long>{

	Optional<Product> findByName(String name);
	
	@Query("{'name': {$regex :?0}}")
	List<Product> findByNamePartialSearch(String regex,Pageable pageable);
	
}
