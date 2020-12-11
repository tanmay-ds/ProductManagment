package com.example.productmngmt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.productmngmt.entity.Users;
import com.example.productmngmt.model.MyUserDetails;
import com.example.productmngmt.repo.UserRepo;
@Service
public class MyUserDetailsService implements UserDetailsService{

	@Autowired
	UserRepo userRepo;
	
	
	@Override
	public UserDetails loadUserByUsername(String username){
		Users user = userRepo.findByEmail(username);
		if(user==null) {
			throw new UsernameNotFoundException("user not found ");
		}
		return new MyUserDetails(user);
	}
	

}
