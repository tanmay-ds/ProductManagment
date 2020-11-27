package com.example.productmngmt.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import com.example.productmngmt.service.ProdService;

@Validated
@RestController
public class Controller {

	@Autowired
	ProdService proService;

	@PostMapping("create")
	public ResponseEntity<?> createProduct(@Valid @RequestBody List<@Valid Product> products) {
		return proService.create(products);
	}

	@GetMapping("getproduct/{pid}")
	public ResponseEntity<?> getProduct(@PathVariable Long pid) {
		return proService.getProduct(pid);
	}

	@PutMapping("update/{pid}")
	public ResponseEntity<?> updateProduct(@PathVariable Long pid, @RequestBody Product product) {
		return proService.updateProd(pid, product);
	}

	@GetMapping("getall")

	public Page<Product> getall(Pageable pageable) {
		return proService.getall(pageable);
	}

	@GetMapping("getall/{search}")

	public ResponseEntity<?> getall(@PathVariable String search, Pageable pageable) {
		return proService.getPartial(search, pageable);
	}

	@DeleteMapping("delete/{pid}")
	public ResponseEntity<?> deleteProduct(@PathVariable Long pid) {
		return proService.deleteProd(pid);
	}

	@PostMapping("addStock")
	public ResponseEntity<?> addStock(@RequestBody Map<Long, Long> stockList) {
		return proService.addStock(stockList);
	}

	@PostMapping("removeStock")
	public ResponseEntity<?> removeStock(@RequestBody Map<Long, Long> stockList) {
		return proService.removeStock(stockList);
	}
}
