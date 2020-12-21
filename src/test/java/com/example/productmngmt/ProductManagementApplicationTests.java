package com.example.productmngmt;

//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import com.example.productmngmt.constant.Constants;
import com.example.productmngmt.dto.Dtos;
import com.example.productmngmt.dto.ProductDto;
import com.example.productmngmt.entity.Product;
import com.example.productmngmt.entity.Roles;
import com.example.productmngmt.entity.Users;
import com.example.productmngmt.exceptionhandler.NegativeArgumentException;
import com.example.productmngmt.exceptionhandler.NoSuchProductFound;
import com.example.productmngmt.exceptionhandler.ProductAlreadyExists;
import com.example.productmngmt.repo.ProductRepo;
import com.example.productmngmt.repo.UserRepo;
import com.example.productmngmt.service.ProductService;
import com.example.productmngmt.service.SequenceGenrationService;

@SpringBootTest
@ActiveProfiles(profiles = "local")
class ProductManagementApplicationTests {
//
//	@Autowired 
//	SequenceGenrationService genrationService;
//	
//	@Autowired
//	PasswordEncoder passwordEncoder;
//	
//	@Autowired
//	ProductService proService;
//
//	@Autowired
//	ProductRepo productRepo;
//
//	@Autowired
//	UserRepo userRepo;
//
//	@Autowired
//	Dtos dtos;
//
//	List<Product> products = new ArrayList<>();
//
//	Map<Long, Long> stockList = new LinkedHashMap<>();
//
//	Product product = null;
//	ProductDto productDto = null;
//	Users user = null;
//	List<Roles> roles = new ArrayList<>();
//
//	@Value("${key}")
//	private String key;
//
//	@BeforeEach
//	void init() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException,
//			BadPaddingException {
//
//		product = new Product(1l, "Earphones", "Samsoong", 10000l, "Wireless", 1l);
//		products.add(product);
//		products.add(new Product(2l, "Mobile Phone", "Samsoong", 45555l, "12 gb ram", 1l));
//		productRepo.saveAll(products);
//		productDto = new ProductDto("Tv2", "Samsoong", 696666l, "88 Oled inch", 0l);
//		roles.add(new Roles("ROLE_ADMIN"));
//		user = new Users(genrationService.generateUserSequence(Users.SEQUENCE_NAME),"tanmay", "shakya", "abc@z.com", "8877996655", "gimb", roles);
//		proService.createUser(Collections.singletonList(user));
//	}
//
//	@Test
//	void duplicateName() {
//		List<ProductDto> duplicateProduct = new ArrayList<>();
//		duplicateProduct.add(new ProductDto("EarPhones", "Sony", 5999l, "Wirless ANC", 0l));
//		Exception exception = assertThrows(ProductAlreadyExists.class, () -> proService.create(duplicateProduct));
//		assertEquals(Constants.PRODUCT_WITH_NAME
//				+ duplicateProduct.stream().map(p -> p.getName().toLowerCase()).collect(Collectors.toList())
//				+ Constants.ALREADY_EXISTS, exception.getMessage());
//
//	}
//
//	@Test
//	void getAll() {
//		Pageable pageable = PageRequest.of(0, 10);
//		Page<Product> productspage = proService.getAll(pageable);
//		assertEquals(products, productspage.getContent());
//	}
//
//	@Test
//	void getAllByPartialSearch() {
//		Pageable pageable = PageRequest.of(0, 10);
//		Page<Product> productspage = proService.getAll("phone", pageable);
//		assertEquals(products, productspage.getContent());
//	}
//
//	@Test
//	void getAllByPartialSearchNotFound() {
//		Pageable pageable = PageRequest.of(0, 10);
//		Exception exception = assertThrows(NoSuchProductFound.class, () -> proService.getAll("tv", pageable));
//		assertEquals(Constants.NO_RESULT_FOUND, exception.getMessage());
//	}
//
//	@Test
//	void getProductById() {
//		assertEquals(product, proService.getProductById(product.getProdId()));
//	}
//
//	@Test
//	void getProductbyIdNotFound() {
//		Exception exception = assertThrows(NoSuchProductFound.class, () -> proService.getProductById(5l));
//		assertEquals(Constants.PRODUCT_WITH_ID + 5l + Constants.NOT_FOUND, exception.getMessage());
//	}
//
//	@Test
//	void updateProduct() {
//		assertEquals(new Product(1l, "Tv2", "Samsoong", 696666l, "88 Oled inch", 1l),
//				proService.updateProd(product.getProdId(), productDto));
//
//	}
//
//	@Test
//	void updateProductNotFound() {
//		Exception exception = assertThrows(NoSuchProductFound.class, () -> proService.updateProd(9999l, productDto));
//		assertEquals(Constants.PRODUCT_WITH_ID + 9999l + Constants.NOT_FOUND, exception.getMessage());
//	}
//
//	@Test
//	void addStock() {
//		stockList.put(1l, 1l);
//		assertEquals(Constants.STOCKS_ADDED, proService.addStock(stockList));
//	}
//
//	@Test
//	void removeStock() {
//		stockList.put(1l, 1l);
//		assertEquals(Constants.STOCKS_UPDATE, proService.removeStock(stockList));
//	}
//
//	@Test
//	void removeStockQuantityExceed() {
//		stockList.put(1l, 5l);
//		Exception exception = assertThrows(NegativeArgumentException.class, () -> proService.removeStock(stockList));
//		assertEquals(Constants.CANNOT_EXCEED_QUANTITY, exception.getMessage());
//	}
//
//	@Test
//	void negativeQuantity() {
//		stockList.put(1l, -1l);
//		stockList.put(-1l, -1l);
//		stockList.put(-1l, 1l);
//		Exception exception = assertThrows(NegativeArgumentException.class, () -> proService.addStock(stockList));
//		assertEquals(Constants.CANNOT_BE_NEGATIVE, exception.getMessage());
//	}
//
//	@Test
//	void deleteProductById() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
//			IllegalBlockSizeException, BadPaddingException {
//		assertEquals(product.getProdId(), proService.deleteProd(product.getProdId()));
//	}
//
//	@Test
//	void deleteProductByIdNotFound() {
//		Exception exception = assertThrows(NoSuchProductFound.class, () -> proService.deleteProd(10l));
//		assertEquals(Constants.PRODUCT_WITH_ID + 10l + Constants.NOT_FOUND, exception.getMessage());
//	}
//
//	@Test
//	void createUser() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
//			IllegalBlockSizeException, BadPaddingException {
//		assertEquals(Constants.USER_ADDED, proService.createUser(Collections.singletonList(new Users(genrationService.generateUserSequence(Users.SEQUENCE_NAME),"hi", "hello",
//				"xyz@a.com", "8877996655", "gimb", Collections.singletonList(new Roles("ROLE_ADMIN"))))));
//
//	}
//
//	@Test
//	void validateUserDetails() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
//			IllegalBlockSizeException, BadPaddingException {
//		Users decryptedUser = dtos.decrypt(userRepo.findByFirstName(dtos.encrypt(user).getFirstName()));
//		if (passwordEncoder.matches(user.getPassword(), decryptedUser.getPassword()))
//			decryptedUser.setPassword(user.getPassword());
//		assertEquals(decryptedUser, user);
//	}
//
//	@AfterEach
//	void delete() {
//		productRepo.deleteAll();
//		userRepo.deleteAll();
//	}
//
}
