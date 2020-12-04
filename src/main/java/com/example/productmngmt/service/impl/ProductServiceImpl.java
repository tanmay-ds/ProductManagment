package com.example.productmngmt.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public class ProductServiceImpl implements ProductService{

	@Autowired
	ProductRepo productRepo;

	@Autowired
	SequenceGenrationService genrationService;

	@Autowired
	Dtos dtos;
	
	@Override
	public List<String> create(List<Product> products) {

		try {
			List<Product> saveProducts = new ArrayList<Product>();
			List<String> messages = new ArrayList<String>();
			for (Product product : products) {
				Optional<Product> checkProduct = productRepo.findByName(product.getName());
				if (checkProduct.isPresent()) {
					throw new ProductAlreadyExists("Product With name: " + product.getName() + " Already Exists");
				}

				product.setProdId(genrationService.generateSequence(Product.SEQUENCE_NAME));
				product.setQuantity(0l);
				messages.add("Product added with Id: " + product.getProdId());
				saveProducts.add(product);
			}
			productRepo.saveAll(saveProducts);
			return messages;
		} catch (IllegalArgumentException e) {
			throw new NoSuchElementException("Product Not Added Successfully");
		}
	}

	@Override
	public Product getProduct(Long pid) {

		try {
			Product getProduct = productRepo.findById(pid).orElseThrow(() -> new NoSuchElementException());
			return getProduct;
		} catch (NoSuchElementException e) {
			throw new NoSuchProductFound("Product with this id " + pid + " Not found");
		}

	}

	@Override
	public Product updateProd(Long pid, Product product) {
		try {
			Product checkProduct = productRepo.findById(pid).orElseThrow(() -> new NoSuchElementException());
			product.setProdId(checkProduct.getProdId());
			product.setQuantity(checkProduct.getQuantity());
			productRepo.save(product);
			return product;
		} catch (NoSuchElementException e) {
			throw new NoSuchProductFound("Product with this id " +pid+ " Not found");
		}

	}

	@Override
	public Page<Product> getAll(Pageable pageable) {
		Page<Product> products = productRepo.findAll(pageable);
		return products;
	}

	@Override
	public Page<Product> getAll(String search, Pageable pageable) {
		Page<Product> products = productRepo.findByNamePartialSearch(search, pageable);
		try {
			if (products.isEmpty())
				throw new NoSuchElementException();
		} catch (NoSuchElementException e) {
			throw new NoSuchProductFound("No Result Found");
		}
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
		List<Long> ids = new ArrayList<Long>();
		List<Long> quantities = new ArrayList<Long>();
		for (Map.Entry<Long, Long> m : stockList.entrySet()) {
			if (m.getKey() < 0 || m.getValue() < 0) {
				throw new NegativeArgumentException("Ids Or Quantity Cannot be Negative");
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

		return "Stock added";
	}

	@Override
	public String removeStock(Map<Long, Long> stockList) {
		List<Long> ids = new ArrayList<Long>();
		List<Long> quantities = new ArrayList<Long>();
		for (Map.Entry<Long, Long> m : stockList.entrySet()) {
			if (m.getKey() < 0 || m.getValue() < 0) {
				throw new NegativeArgumentException("Ids Or Quantity Cannot be Negative");
			}
			ids.add(m.getKey());
			quantities.add(m.getValue());
		}

		for (Long id : ids) {
			try {
				Product checkProduct = productRepo.findById(id).orElseThrow(() -> new NoSuchElementException());
				if (quantities.get(ids.indexOf(id)) > checkProduct.getQuantity()) {
					throw new NegativeArgumentException("Quantity Cannot exceed present Stock");
				}
				checkProduct.setQuantity(checkProduct.getQuantity() - quantities.get(ids.indexOf(id)));

				productRepo.save(checkProduct);
			} catch (NoSuchElementException e) {
				throw new NoSuchProductFound("Product with this id " + id + " Not found");
			}
		}
		return "Stock Updated";
	}

}
