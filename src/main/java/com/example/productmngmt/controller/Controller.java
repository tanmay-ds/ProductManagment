package com.example.productmngmt.controller;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.productmngmt.dto.ProductDto;
import com.example.productmngmt.entity.Product;
import com.example.productmngmt.entity.Users;
import com.example.productmngmt.exceptionhandler.ResponseMessage;
import com.example.productmngmt.jwt.model.AuthRequest;
import com.example.productmngmt.service.ProductService;

@Validated
@RestController
@RequestMapping("/api")
public class Controller {

	@Autowired
	ProductService proService;

	@PostMapping("authenticate")
	public ResponseEntity<ResponseMessage> authenticate(@RequestBody AuthRequest authRequest) {
		return ResponseEntity.ok(new ResponseMessage(new Date(), HttpStatus.OK,
				"Token : " + proService.authenticate(authRequest)));
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping("create")
	public ResponseEntity<List<String>> createProduct(@RequestBody List<@Valid ProductDto> productsDto) {
		return ResponseEntity.ok(proService.create(productsDto));
	}

//	@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping("createuser")
	public ResponseEntity<String> createUser(@RequestBody List<@Valid Users> users) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		return ResponseEntity.ok(proService.createUser(users));
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping("getproduct/{pid}")
	public ResponseEntity<Product> getProductById(@PathVariable Long pid) {
		return ResponseEntity.ok(proService.getProductById(pid));
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@PutMapping("update/{pid}")
	public ResponseEntity<Product> updateProduct(@PathVariable Long pid, @RequestBody ProductDto productDto) {
		return ResponseEntity.ok(proService.updateProd(pid, productDto));
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping("getall")

	public ResponseEntity<Page<Product>> getall(Pageable pageable) {
		return ResponseEntity.ok(proService.getAll(pageable));
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping("getall/{search}")

	public ResponseEntity<Page<Product>> getall(@PathVariable String search, Pageable pageable) {
		return ResponseEntity.ok(proService.getAll(search, pageable));
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@DeleteMapping("delete/{pid}")
	public ResponseEntity<ResponseMessage> deleteProduct(@PathVariable Long pid) {
		return ResponseEntity.ok(new ResponseMessage(new Date(), HttpStatus.OK,
				"Product with Id : " + proService.deleteProd(pid) + " is deleted"));
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping("addStock")
	public ResponseEntity<ResponseMessage> addStock(@RequestBody Map<Long, Long> stockList) {
		return ResponseEntity.ok(new ResponseMessage(new Date(), HttpStatus.OK, proService.addStock(stockList)));
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping("removeStock")
	public ResponseEntity<ResponseMessage> removeStock(@RequestBody Map<Long, Long> stockList) {
		return ResponseEntity.ok(new ResponseMessage(new Date(), HttpStatus.OK, proService.removeStock(stockList)));
	}
}
