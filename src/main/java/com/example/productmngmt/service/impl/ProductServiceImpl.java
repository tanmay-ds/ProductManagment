package com.example.productmngmt.service.impl;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.productmngmt.constant.Constants;
import com.example.productmngmt.dto.Dtos;
import com.example.productmngmt.dto.ProductDto;
import com.example.productmngmt.entity.BlackListedToken;
import com.example.productmngmt.entity.Product;
import com.example.productmngmt.entity.Users;
import com.example.productmngmt.exceptionhandler.BadCredsException;
import com.example.productmngmt.exceptionhandler.NegativeArgumentException;
import com.example.productmngmt.exceptionhandler.NoSuchProductFound;
import com.example.productmngmt.exceptionhandler.ProductAlreadyExists;
import com.example.productmngmt.repo.BlackListedTokenRepo;
import com.example.productmngmt.repo.ProductRepo;
import com.example.productmngmt.repo.UserRepo;
import com.example.productmngmt.security.jwt.AuthRequest;
import com.example.productmngmt.security.jwt.JwtTokenProvider;
import com.example.productmngmt.security.jwt.JwtUtil;
import com.example.productmngmt.service.ProductService;
import com.example.productmngmt.service.SequenceGenrationService;
import com.example.productmngmt.util.CryptoUtil;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	SequenceGenrationService sequenceGenrationService;

	@Autowired
	MyUserDetailsServiceImpl myUserDetailsService;

	@Autowired
	BlackListedTokenRepo blackListedTokenRepo;

	@Autowired
	JwtTokenProvider jwtTokenProvider;

	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	ProductRepo productRepo;

	@Autowired
	CryptoUtil cryptoUtil;

	@Autowired
	UserRepo userRepo;

	@Autowired
	JwtUtil jwtUtil;

	@Autowired
	Dtos dtos;

	@Override
	public String authenticate(AuthRequest authRequest) {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
		} catch (BadCredentialsException e) {
			throw new BadCredsException("Incorrect username or password");
		}
		final UserDetails userDetails = myUserDetailsService.loadUserByUsername(authRequest.getUsername());
		return "Bearer " + jwtTokenProvider.genrateToken(userDetails);
	}

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
			throw new ProductAlreadyExists(Constants.PRODUCT_WITH_NAME + checkProducts + Constants.ALREADY_EXISTS);
		}

		for (ProductDto productDto : productsDto) {
			Product product = dtos.dtoToProduct(productDto);
			product.setProdId(sequenceGenrationService.generateProductSequence(Product.SEQUENCE_NAME));
			product.setQuantity(0l);
			messages.add(Constants.PRODUCT_ADDED_WITH_ID + product.getProdId());
			saveProducts.add(product);
		}
		productRepo.saveAll(saveProducts);
		return messages;
	}

	@Override
	public Product getProductById(Long pid) {
		Optional<Product> checkProduct = productRepo.findByProdId(pid);
		if (!checkProduct.isPresent()) {
			throw new NoSuchProductFound(Constants.PRODUCT_WITH_ID + pid + Constants.NOT_FOUND);
		} else
			return dtos.optionalToProduct(checkProduct);
	}

	@Override
	public Product updateProd(Long pid, ProductDto productDto) {
		Product getProduct = getProductById(pid);
		Product updateProduct = dtos.dtoToProduct(productDto);
		updateProduct.setId(getProduct.getId());
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

	@Override
	public String createUser(List<Users> users) throws InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		List<Users> encryptedUsers = new ArrayList<>();
		for (Users user : users) {
			Users encrypteduser = dtos.encrypt(user);
			encrypteduser.setUuid(sequenceGenrationService.generateUserSequence(Users.SEQUENCE_NAME));
			encryptedUsers.add(encrypteduser);
		}
		userRepo.saveAll(encryptedUsers);
		return Constants.USER_ADDED;
	}

	@Override
	public String logoutUser() {
		doTokenBlackList();
		return "Logout success";
	}

	private void doTokenBlackList() {
		Long uuid = JwtUtil.getUuidFromToken();
		BlackListedToken blackListedToken = new BlackListedToken();
		blackListedToken.setUuid(uuid);
		blackListedToken.setAccesToken(jwtUtil.getToken());
		blackListedTokenRepo.save(blackListedToken);
	}

}
