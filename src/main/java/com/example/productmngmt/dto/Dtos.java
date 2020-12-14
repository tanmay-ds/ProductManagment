package com.example.productmngmt.dto;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
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
		Users encryptedUsers = new Users();
		encryptedUsers.setFirstName(cryptoUtil.encrypt(user.getFirstName(), key));
		encryptedUsers.setLastName(cryptoUtil.encrypt(user.getLastName(), key));
		encryptedUsers.setPhoneNumber(cryptoUtil.encrypt(user.getPhoneNumber(), key));
		encryptedUsers.setAddress(cryptoUtil.encrypt(user.getAddress(), key));
		encryptedUsers.setEmail(cryptoUtil.encrypt(user.getEmail(), key));
		encryptedUsers.setPassword(passwordEncoder.encode(user.getPassword()));
		List<Roles> roles = new ArrayList<>();
		for (Roles role : user.getRoles()) {
			Roles newRoles = new Roles();
			newRoles.setRole(cryptoUtil.encrypt(role.getRole(), key));
			roles.add(newRoles);
		}
		encryptedUsers.setRoles(roles);
		return encryptedUsers;
	}

	public Users decrypt(Users user) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException {
		Users decryptedUsers = new Users();
		decryptedUsers.setFirstName(cryptoUtil.decrypt(user.getFirstName(), key));
		decryptedUsers.setLastName(cryptoUtil.decrypt(user.getLastName(), key));
		decryptedUsers.setPhoneNumber(cryptoUtil.decrypt(user.getPhoneNumber(), key));
		decryptedUsers.setAddress(cryptoUtil.decrypt(user.getAddress(), key));
		decryptedUsers.setEmail(cryptoUtil.decrypt(user.getEmail(), key));
		decryptedUsers.setPassword(user.getPassword());
		List<Roles> roles = new ArrayList<>();
		for (Roles role : user.getRoles()) {
			Roles newRoles = new Roles();
			newRoles.setRole(cryptoUtil.decrypt(role.getRole(), key));
			roles.add(newRoles);
		}
		decryptedUsers.setRoles(roles);
		return decryptedUsers;
	}

}
