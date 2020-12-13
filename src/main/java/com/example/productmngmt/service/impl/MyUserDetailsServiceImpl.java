package com.example.productmngmt.service.impl;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.productmngmt.dto.Dtos;
import com.example.productmngmt.entity.Users;
import com.example.productmngmt.exceptionhandler.BadCredsException;
import com.example.productmngmt.model.MyUserDetails;
import com.example.productmngmt.repo.UserRepo;

@Service
public class MyUserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	UserRepo userRepo;
	
	@Autowired
	Dtos dtos;
	
	@Value("${key}")
	private String key;
	
	@Override
	public UserDetails loadUserByUsername(String username){
		Users user = null;
		try {
			user = userRepo.findByEncryptEmail(username,key);
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException e1) {
			throw new BadCredsException("Incorrect username or password");
		}
		if(user==null) {
			throw new UsernameNotFoundException("user not found ");
		}
		return new MyUserDetails(user);
	}
	

}
