package com.example.productmngmt.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.example.productmngmt.constant.Constants;
import com.example.productmngmt.dto.Dtos;
import com.example.productmngmt.dto.ProductDto;
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
	public List<String> create(List<ProductDto> productsDto) {

		List<Product> saveProducts = new ArrayList<>();
		List<String> productNames = productsDto.stream().map(product -> product.getName().toLowerCase())
				.collect(Collectors.toList());
		List<String> messages = new ArrayList<>();
		List<String> checkProducts = productRepo.findAll().stream()
				.filter(name -> productNames.contains(name.getName().toLowerCase()))
				.map(product -> product.getName().toLowerCase()).collect(Collectors.toList());

		if (!checkProducts.isEmpty()) {
			throw new ProductAlreadyExists(Constants.PRODUCT_WITH_NAME + checkProducts + Constants.ALREADY_EXITS);
		}

		for (ProductDto productDto : productsDto) {
			Product product = dtos.dtoToProduct(productDto);
			product.setProdId(genrationService.generateSequence(Product.SEQUENCE_NAME));
			product.setQuantity(0l);
			messages.add(Constants.PRODUCT_ADDED_WITH_ID + product.getProdId());
			saveProducts.add(product);
		}

		productRepo.saveAll(saveProducts);
		return messages;
	}

	@Override
	public Product getProductById(Long pid) {
		Optional<Product> checkProduct = productRepo.findById(pid);
		if (!checkProduct.isPresent()) {
			throw new NoSuchProductFound(Constants.PRODUCT_WITH_ID + pid + Constants.NOT_FOUND);
		} else
			return dtos.optionalToProduct(checkProduct);
	}

	@Override
	public Product updateProd(Long pid, ProductDto productDto) {

		Product getProduct = getProductById(pid);
		Product updateProduct = dtos.dtoToProduct(productDto);
		updateProduct.setProdId(getProduct.getProdId());
		updateProduct.setQuantity(getProduct.getQuantity());
		return productRepo.save(updateProduct);

	}

	@Override
	public Page<Product> getAll(Pageable pageable) {
		return productRepo.findAll(pageable);
	}

	@Override
	public Page<Product> getAll(String search, Pageable pageable) {
		Page<Product> products = productRepo.findByNamePartialSearch(search, pageable);
		if (products.isEmpty())
			throw new NoSuchProductFound(Constants.NO_RESULT_FOUND);
		else
			return products;
	}

	@Override
	public Long deleteProd(Long pid) {
		Product deleteProduct = getProductById(pid);
		productRepo.delete(deleteProduct);
		return pid;
	}

	@Override
	public String addStock(Map<Long, Long> stockList) {
		initProductAndCheckNegativeQuantity(stockList);

		for (Long id : stockList.keySet().stream().collect(Collectors.toList())) {
			Product product = getProductById(id);
			product.setQuantity(product.getQuantity() + stockList.get(id));
			productRepo.save(product);
		}

		return Constants.STOCKS_ADDED;
	}

	@Override
	public String removeStock(Map<Long, Long> stockList) {
		initProductAndCheckNegativeQuantity(stockList);

		for (Long id : stockList.keySet().stream().collect(Collectors.toList())) {
			Product product = getProductById(id);
			if (stockList.get(id) > product.getQuantity()) {
				throw new NegativeArgumentException(Constants.CANNOT_EXCEED_QUANTITY);
			}
			product.setQuantity(product.getQuantity() - stockList.get(id));
			productRepo.save(product);
		}
		return Constants.STOCKS_UPDATE;
	}

	private void initProductAndCheckNegativeQuantity(Map<Long, Long> stockList) {
		for (Map.Entry<Long, Long> m : stockList.entrySet()) {
			if (m.getKey() < 0 || m.getValue() < 0) {
				throw new NegativeArgumentException(Constants.CANNOT_BE_NEGATIVE);
			}
		}
	}
}
