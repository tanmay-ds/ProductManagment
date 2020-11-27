package com.example.productmngmt.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.productmngmt.dto.Dtos;
import com.example.productmngmt.entity.Product;
import com.example.productmngmt.exceptionhandler.NoSuchProductFound;
import com.example.productmngmt.exceptionhandler.ResponseMessage;
import com.example.productmngmt.repo.ProductRepo;

@Service
public class ProdService {

	@Autowired
	ProductRepo productRepo;

	@Autowired
	SequenceGenrationService genrationService;

	@Autowired
	Dtos dtos;

	public ResponseEntity<?> create(@Valid Product products) {
		productRepo.save(products);
		return ResponseEntity.ok(products);
	}

	public ResponseEntity<?> create(List<Product> products) {

		List<String> messages = new ArrayList<String>();
		for (Product product : products) {
			Optional<Product> checkProduct = productRepo.findByName(product.getName());
			if (checkProduct.isPresent()) {
				return new ResponseEntity<ResponseMessage>(
						new ResponseMessage(new Date(), HttpStatus.INTERNAL_SERVER_ERROR,
								"Product with name: " + product.getName() + " already Exits"),
						new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
			}
			product.setProdId(genrationService.generateSequence(Product.SEQUENCE_NAME));
			product.setQuantity(0l);
			productRepo.save(product);
			messages.add("Product added with Id: " + product.getProdId());

		}
		return ResponseEntity.ok(messages);
	}

	public ResponseEntity<?> getProduct(Long pid) {
		try {
			Product getProduct = productRepo.findById(pid).orElseThrow(() -> new NoSuchElementException());
			return ResponseEntity.ok(getProduct);
		} catch (NoSuchElementException e) {
			throw new NoSuchProductFound("Product with this id " + pid + " Not found");
		}

	}

	public ResponseEntity<?> updateProd(Long pid, Product product) {
		try {
			Product checkProduct = productRepo.findById(pid).orElseThrow(() -> new NoSuchElementException());
			product.setProdId(checkProduct.getProdId());
			product.setQuantity(checkProduct.getQuantity());
			productRepo.save(product);
			return ResponseEntity.ok(product);
		} catch (NoSuchElementException e) {
			throw new NoSuchProductFound("Product with this id " + pid + " Not found");
		}

	}

	public Page<Product> getall(Pageable pageable) {
		Page<Product> products = productRepo.findAll(pageable);
		return products;
	}

	public ResponseEntity<?> getPartial(String search, Pageable pageable) {
		List<Product> products = productRepo.findByNamePartialSearch(search, pageable);
		try {

			if (products.isEmpty())
				throw new NoSuchElementException();
		} catch (NoSuchElementException e) {
			throw new NoSuchProductFound("No Result Found");
		}
		return ResponseEntity.ok(products);
	}

	public ResponseEntity<?> deleteProd(Long pid) {
		Optional<Product> getProduct = productRepo.findById(pid);

		try {
			Product deleteProduct = dtos.optionalToProduct(getProduct);
			productRepo.delete(deleteProduct);
			return new ResponseEntity<ResponseMessage>(
					new ResponseMessage(new Date(), HttpStatus.OK, "Product with Id : " + pid + " is deleted"),
					new HttpHeaders(), HttpStatus.OK);
		} catch (NoSuchElementException e) {
			throw new NoSuchProductFound("Product with this id " + pid + " Not found");
		}
	}

	public ResponseEntity<?> addStock(Map<Long, Long> stockList) {
		List<Long> ids = new ArrayList<Long>();
		List<Long> quantities = new ArrayList<Long>();
		for (Map.Entry<Long, Long> m : stockList.entrySet()) {
			if(m.getKey() <0 || m.getValue() <0) {
				return new ResponseEntity<ResponseMessage>(
						new ResponseMessage(new Date(), HttpStatus.INTERNAL_SERVER_ERROR, "Id or Quantity Cannot be negative")
						,new HttpHeaders(),HttpStatus.INTERNAL_SERVER_ERROR);
			}
			ids.add(m.getKey());
			quantities.add(m.getValue());
		}
		for (Long id : ids) {

			try {
				Product checkProduct = productRepo.findById(id).orElseThrow(() -> new NoSuchElementException());
				checkProduct.setQuantity(checkProduct.getQuantity() + quantities.get(ids.indexOf(id)));
				productRepo.save(checkProduct);
			} catch (NoSuchElementException e) {
				throw new NoSuchProductFound("Product with this id " + id + " Not found");
			}
		}

		return ResponseEntity.ok("Stock added");
	}

	public ResponseEntity<?> removeStock(Map<Long, Long> stockList) {
		List<Long> ids = new ArrayList<Long>();
		List<Long> quantities = new ArrayList<Long>();
		for (Map.Entry<Long, Long> m : stockList.entrySet()) {
			if(m.getKey() <0 || m.getValue() <0) {
				return new ResponseEntity<ResponseMessage>(
						new ResponseMessage(new Date(), HttpStatus.INTERNAL_SERVER_ERROR, "Id or Quantity Cannot be negative")
						,new HttpHeaders(),HttpStatus.INTERNAL_SERVER_ERROR);
			}
			ids.add(m.getKey());
			quantities.add(m.getValue());
		}

		for (Long id : ids) {
			try {
				Product checkProduct = productRepo.findById(id).orElseThrow(() -> new NoSuchElementException());
				if(quantities.get(ids.indexOf(id)) > checkProduct.getQuantity()) {
					return new ResponseEntity<ResponseMessage>(
							new ResponseMessage(new Date(), HttpStatus.INTERNAL_SERVER_ERROR, "Quantity Cannot exceed present Stock")
							,new HttpHeaders(),HttpStatus.INTERNAL_SERVER_ERROR);
				}
				checkProduct.setQuantity(checkProduct.getQuantity() - quantities.get(ids.indexOf(id)));
				
				productRepo.save(checkProduct);
			} catch (NoSuchElementException e) {
				throw new NoSuchProductFound("Product with this id " + id + " Not found");
			}
		}
		return ResponseEntity.ok("Stock Updated");
	}

}
