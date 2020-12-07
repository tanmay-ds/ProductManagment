package com.example.productmngmt.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.example.productmngmt.dto.Dtos;
import com.example.productmngmt.entity.Product;
import com.example.productmngmt.exceptionhandler.NegativeArgumentException;
import com.example.productmngmt.exceptionhandler.NoSuchProductFound;
import com.example.productmngmt.exceptionhandler.ProductAlreadyExists;
import com.example.productmngmt.repo.ProductRepo;
import com.example.productmngmt.service.ProductService;
import com.example.productmngmt.service.SequenceGenrationService;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	ProductRepo productRepo;

	@Autowired
	SequenceGenrationService genrationService;

	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	Dtos dtos;

	@Override
	public List<String> create(List<Product> products) {

			List<Product> saveProducts = new ArrayList<>();
			List<String> productNames = products.stream()
					.map(product-> product.getName().toLowerCase())
					.collect(Collectors.toList());			
			List<String> messages = new ArrayList<>();
			List<String> checkProducts = productRepo.findAll().stream()
					.filter(name -> productNames.contains(name.getName().toLowerCase()))
					.map(product-> product.getName().toLowerCase())
					.collect(Collectors.toList());

			if(!checkProducts.isEmpty()) {
				throw new ProductAlreadyExists("Product With name: " + checkProducts + " Already Exists");
			}
			
			for (Product product : products) {
				product.setProdId(genrationService.generateSequence(Product.SEQUENCE_NAME));
				product.setQuantity(0l);
				messages.add("Product added with Id: " + product.getProdId());
				saveProducts.add(product);
			}
			
			productRepo.saveAll(saveProducts);
			return messages;
	}

	@Override
	public Product getProduct(Long pid) {
		Optional<Product> getProduct = productRepo.findById(pid);
		if (getProduct.isPresent()) {
			return dtos.optionalToProduct(getProduct);
		} else {
			throw new NoSuchProductFound("Product with this id " + pid + " Not found");
		}
	}

	@Override
	public Product updateProd(Long pid, Product product) {
		Optional<Product> checkProduct = productRepo.findById(pid);
		if (checkProduct.isPresent()) {
			product.setProdId(checkProduct.get().getProdId());
			product.setQuantity(checkProduct.get().getQuantity());
			return productRepo.save(product);
		} else
			throw new NoSuchProductFound("Product with this id " + pid + " Not found");
	}

	@Override
	public Page<Product> getAll(Pageable pageable) {
		return productRepo.findAll(pageable);
	}

	@Override
	public Page<Product> getAll(String search, Pageable pageable) {
		Page<Product> products = productRepo.findByNamePartialSearch(search, pageable);
		if (products.isEmpty())
			throw new NoSuchProductFound("No Result Found");
		else
			return products;
	}

	@Override
	public Long deleteProd(Long pid) {
		Optional<Product> getProduct = productRepo.findById(pid);

		try {
			Product deleteProduct = dtos.optionalToProduct(getProduct);
			productRepo.delete(deleteProduct);
			return pid;
		} catch (NoSuchElementException e) {
			throw new NoSuchProductFound("Product with this id " + pid + " Not found");
		}
	}

	@Override
	public String addStock(Map<Long, Long> stockList) {
		List<Long> ids = new ArrayList<>();
		List<Long> quantities = new ArrayList<>();
		for (Map.Entry<Long, Long> m : stockList.entrySet()) {
			if (m.getKey() < 0 || m.getValue() < 0) {
				throw new NegativeArgumentException("Ids Or Quantity Cannot be Negative");
			}
			ids.add(m.getKey());
			quantities.add(m.getValue());
		}
		for (Long id : ids) {
			Optional<Product> checkProduct = productRepo.findById(id);
			Product product = dtos.optionalToProduct(checkProduct);
			if (checkProduct.isPresent()) {
				product.setQuantity(checkProduct.get().getQuantity() + quantities.get(ids.indexOf(id)));
				productRepo.save(product);
			} else {
				throw new NoSuchProductFound("Product with this id " + id + " Not found");
			}
		}

		return "Stock added";
	}

	@Override
	public String removeStock(Map<Long, Long> stockList) {
		List<Long> ids = new ArrayList<>();
		List<Long> quantities = new ArrayList<>();
		for (Map.Entry<Long, Long> m : stockList.entrySet()) {
			if (m.getKey() < 0 || m.getValue() < 0) {
				throw new NegativeArgumentException("Ids Or Quantity Cannot be Negative");
			}
			ids.add(m.getKey());
			quantities.add(m.getValue());
		}
		for (Long id : ids) {
			Optional<Product> checkProduct = productRepo.findById(id);
			Product product = dtos.optionalToProduct(checkProduct);
			if (checkProduct.isPresent()) {
				if (quantities.get(ids.indexOf(id)) > checkProduct.get().getQuantity()) {
					throw new NegativeArgumentException("Quantity Cannot exceed present Stock");
				}
				product.setQuantity(checkProduct.get().getQuantity() - quantities.get(ids.indexOf(id)));
				productRepo.save(product);
			} else {
				throw new NoSuchProductFound("Product with this id " + id + " Not found");
			}
		}
		return "Stock Updated";
	}

}
