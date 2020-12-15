package com.example.productmngmt.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.productmngmt.entity.BlackListedToken;

@Repository
public interface BlackListedTokenRepo extends MongoRepository<BlackListedToken, Long>{

	List<BlackListedToken> findByAccesToken(String accessToken);
}
