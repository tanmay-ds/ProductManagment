package com.example.productmngmt.template.impl;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.example.productmngmt.entity.Users;
import com.example.productmngmt.template.UserRepositoryTemplate;
import com.example.productmngmt.util.CryptoUtil;

public class UserRepositoryTemplateImpl implements UserRepositoryTemplate{

	@Autowired
	CryptoUtil cryptoUtil;
	
	@Autowired
	private MongoTemplate mongoTeamplate;
	
	@Override
	public Users findByEncryptEmail(String email,String key) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		Query query = new Query().addCriteria(Criteria.where("email").is(cryptoUtil.decrypt(email, key)));
		
		return mongoTeamplate.findOne(query, Users.class);
	}

}
