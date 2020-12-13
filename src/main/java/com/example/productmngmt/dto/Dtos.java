package com.example.productmngmt.dto;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.productmngmt.entity.Product;
import com.example.productmngmt.entity.Roles;
import com.example.productmngmt.entity.Users;
import com.example.productmngmt.util.CryptoUtil;

@Service
public class Dtos {

	@Autowired
	CryptoUtil cryptoUtil;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Value("${key}")
	private String key;

	public Product optionalToProduct(Optional<Product> optional) {
		Product product = new Product();
		if (optional.isPresent()) {
			product.setProdId(optional.get().getProdId());
			product.setName(optional.get().getName());
			product.setBrand(optional.get().getBrand());
			product.setPrice(optional.get().getPrice());
			product.setDetails(optional.get().getDetails());
			product.setQuantity(optional.get().getQuantity());
			return product;
		}
		return null;

	}

	public Product dtoToProduct(ProductDto productDto) {
		Product product = new Product();
		product.setName(productDto.getName());
		product.setBrand(productDto.getBrand());
		product.setPrice(productDto.getPrice());
		product.setDetails(productDto.getDetails());
		return product;
	}

	public Users encrypt(Users user) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException {
		user.setFirstName(cryptoUtil.encrypt(user.getFirstName(), key));
		user.setLastName(cryptoUtil.encrypt(user.getLastName(), key));
		user.setPhoneNumber(cryptoUtil.encrypt(user.getPhoneNumber(), key));
		user.setAddress(cryptoUtil.encrypt(user.getAddress(), key));
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		for (Roles role : user.getRoles()) {
			role.setRole(cryptoUtil.encrypt(role.getRole(), key));
		}
		return user;
	}

	public Users decrypt(Users user) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException {
		user.setFirstName(cryptoUtil.decrypt(user.getFirstName(), key));
		user.setLastName(cryptoUtil.decrypt(user.getLastName(), key));
		user.setPhoneNumber(cryptoUtil.decrypt(user.getPhoneNumber(), key));
		user.setAddress(cryptoUtil.decrypt(user.getAddress(), key));
		for (Roles role : user.getRoles()) {
			role.setRole(cryptoUtil.decrypt(role.getRole(), key));
		}
		return user;
	}

}
