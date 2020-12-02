package com.example.productmngmt.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.productmngmt.entity.Product;
import com.example.productmngmt.exceptionhandler.ResponseMessage;
import com.example.productmngmt.service.impl.ProductServiceImpl;

@Validated
@RestController
public class Controller {

	@Autowired
	ProductServiceImpl proServiceImpl;

	@PostMapping("create")
	public ResponseEntity<?> createProduct(@Valid @RequestBody List<@Valid Product> products) {
		return ResponseEntity.ok(proServiceImpl.create(products));
	}

	@GetMapping("getproduct/{pid}")
	public ResponseEntity<?> getProduct(@PathVariable Long pid) {
		return ResponseEntity.ok(proServiceImpl.getProduct(pid));
	}

	@PutMapping("update/{pid}")
	public ResponseEntity<?> updateProduct(@PathVariable Long pid, @RequestBody Product product) {
		return ResponseEntity.ok(proServiceImpl.updateProd(pid, product));
	}

	@GetMapping("getall")

	public Page<Product> getall(Pageable pageable) {
		return proServiceImpl.getAll(pageable);
	}

	@GetMapping("getall/{search}")

	public Page<Product> getall(@PathVariable String search, Pageable pageable) {
		return proServiceImpl.getAll(search, pageable);
	}

	@DeleteMapping("delete/{pid}")
	public ResponseEntity<?> deleteProduct(@PathVariable Long pid) {
		return new ResponseEntity<ResponseMessage>(
				new ResponseMessage(new Date(), HttpStatus.OK, "Product with Id : " + proServiceImpl.deleteProd(pid) + " is deleted"),
				new HttpHeaders(), HttpStatus.OK);
	}

	@PostMapping("addStock")
	public ResponseEntity<?> addStock(@RequestBody Map<Long, Long> stockList) {
		return new ResponseEntity<ResponseMessage>(
				new ResponseMessage(new Date(), HttpStatus.OK, proServiceImpl.addStock(stockList)),
				new HttpHeaders(), HttpStatus.OK);
	}

	@PostMapping("removeStock")
	public ResponseEntity<?> removeStock(@RequestBody Map<Long, Long> stockList) {
		return new ResponseEntity<ResponseMessage>(
				new ResponseMessage(new Date(), HttpStatus.OK, proServiceImpl.removeStock(stockList)),
				new HttpHeaders(), HttpStatus.OK);
	}
}
