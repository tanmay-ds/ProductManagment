package com.example.productmngmt.repo;

import org.springframework.stereotype.Repository;

import com.example.productmngmt.entity.Users;

import org.springframework.data.mongodb.repository.MongoRepository;

@Repository
public interface UserRepo extends MongoRepository<Users, Long>{
	
	Users findByEmail(String email);

}
