package com.example.productmngmt;

import static org.junit.Assert.assertEquals;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles(profiles = "local")
public class ProductManagementApplicationTests {

	@Autowired
	SequenceGenrationService genrationService;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	ProductService proService;

	@Autowired
	ProductRepo productRepo;

	@Autowired
	UserRepo userRepo;

	@Autowired
	Dtos dtos;

	List<Product> products = new ArrayList<>();

	Map<Long, Long> stockList = new LinkedHashMap<>();

	Product product = null;
	ProductDto productDto = null;
	Users user = null;
	List<Roles> roles = new ArrayList<>();

	@Value("${key}")
	private String key;

	@Before
	public void init() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException {

		product = new Product(1l, "Earphones", "Samsoong", 10000l, "Wireless", 1l);
		products.add(product);
		products.add(new Product(2l, "Mobile Phone", "Samsoong", 45555l, "12 gb ram", 1l));
		productRepo.saveAll(products);
		productDto = new ProductDto("Tv2", "Samsoong", 696666l, "88 Oled inch", 0l);
		roles.add(new Roles("ROLE_ADMIN"));
		user = new Users(genrationService.generateUserSequence(Users.SEQUENCE_NAME), "tanmay", "shakya", "abc@z.com",
				"8877996655", "gimb", roles);
		proService.createUser(Collections.singletonList(user));
	}

	@Test(expected = ProductAlreadyExists.class)
	public void duplicateName() {
		List<ProductDto> duplicateProduct = new ArrayList<>();
		duplicateProduct.add(new ProductDto("EarPhones", "Sony", 5999l, "Wirless ANC", 0l));
		proService.create(duplicateProduct);
	}

	@Test
	public void getAll() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<Product> productspage = proService.getAll(pageable);
		assertEquals(products, productspage.getContent());
	}

	@Test
	public void getAllByPartialSearch() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<Product> productspage = proService.getAll("phone", pageable);
		assertEquals(products, productspage.getContent());
	}

	@Test(expected = NoSuchProductFound.class)
	public void getAllByPartialSearchNotFound() {
		Pageable pageable = PageRequest.of(0, 10);
		proService.getAll("tv", pageable);
	}

	@Test
	public void getProductById() {
		assertEquals(product, proService.getProductById(product.getProdId()));
	}

	@Test(expected = NoSuchProductFound.class)
	public void getProductbyIdNotFound() {
		proService.getProductById(5l);
	}

	@Test
	public void updateProduct() {
		assertEquals(new Product(1l, "Tv2", "Samsoong", 696666l, "88 Oled inch", 1l),
				proService.updateProd(product.getProdId(), productDto));

	}

	@Test(expected = NoSuchProductFound.class)
	public void updateProductNotFound() {
		proService.updateProd(9999l, productDto);
	}

	@Test
	public void addStock() {
		stockList.put(1l, 1l);
		assertEquals(Constants.STOCKS_ADDED, proService.addStock(stockList));
	}

	@Test
	public void removeStock() {
		stockList.put(1l, 1l);
		assertEquals(Constants.STOCKS_UPDATE, proService.removeStock(stockList));
	}

	@Test(expected = NegativeArgumentException.class)
	public void removeStockQuantityExceed() {
		stockList.put(1l, 5l);
		proService.removeStock(stockList);
	}

	@Test(expected = NegativeArgumentException.class)
	public void negativeQuantity() {
		stockList.put(1l, -1l);
		stockList.put(-1l, -1l);
		stockList.put(-1l, 1l);
		proService.addStock(stockList);
	}

	@Test
	public void deleteProductById() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException {
		assertEquals(product.getProdId(), proService.deleteProd(product.getProdId()));
	}

	@Test(expected = NoSuchProductFound.class)
	public void deleteProductByIdNotFound() {
		proService.deleteProd(10l);
	}

	@Test
	public void createUser() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException {
		assertEquals(Constants.USER_ADDED,
				proService.createUser(Collections.singletonList(new Users(
						genrationService.generateUserSequence(Users.SEQUENCE_NAME), "hi", "hello", "xyz@a.com",
						"8877996655", "gimb", Collections.singletonList(new Roles("ROLE_ADMIN"))))));
		Users user = new Users();
		user.setUuid(100L);
		user.setFirstName("default");
		user.setLastName("users");
		user.setEmail("du@du.com");
		user.setAddress("system");
		user.setPassword("admin");
		user.setPhoneNumber("8855447711");
		user.setRoles(Collections.singletonList(new Roles("ROLE_ADMIN")));
		proService.createUser(Collections.singletonList(user));

	}

	@Test
	public void validateUserDetails() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException {
		Users decryptedUser = dtos.decrypt(userRepo.findByFirstName(dtos.encrypt(user).getFirstName()));
		if (passwordEncoder.matches(user.getPassword(), decryptedUser.getPassword()))
			decryptedUser.setPassword(user.getPassword());
		assertEquals(decryptedUser, user);
	}

	@After
	public void delete() {
		productRepo.deleteAll();
		userRepo.deleteAll();
	}

}
