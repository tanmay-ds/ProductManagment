package com.example.productmngmt.exceptionhandler;

import java.util.Date;
import java.util.Map;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.productmngmt.constant.Constants;
import com.example.productmngmt.model.ResponseModel;

@RestControllerAdvice
public class CustomExceptionsHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(NoSuchProductFound.class)
	public final ResponseEntity<ResponseModel> noProductFound(NoSuchProductFound e, WebRequest request) {
		return internalServerError(e.getLocalizedMessage());
	}

	@ExceptionHandler(ProductAlreadyExists.class)
	public final ResponseEntity<ResponseModel> productHandler(ProductAlreadyExists e, WebRequest request) {
		return internalServerError(e.getLocalizedMessage());
	}

	@ExceptionHandler(DuplicateKeyException.class)
	public final ResponseEntity<ResponseModel> duplicateProductHandler(DuplicateKeyException e, WebRequest request) {
		return internalServerError(
				Constants.PRODUCT_ALREADY_EXISTS);
	}

	@ExceptionHandler(NegativeArgumentException.class)
	public final ResponseEntity<ResponseModel> negativeArgument(NegativeArgumentException e, WebRequest request) {
		return internalServerError(e.getLocalizedMessage());
	}

	@ExceptionHandler(BadCredsException.class)
	public final ResponseEntity<ResponseModel> badCred(BadCredsException e, WebRequest request) {
		return new ResponseEntity<>(new ResponseModel(new Date().toString(), HttpStatus.UNAUTHORIZED, e.getLocalizedMessage()),
				new HttpHeaders(), HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(ExpiredJwtTokenException.class)
	public final ResponseEntity<ResponseModel> expiredJwtToken(ExpiredJwtTokenException e, WebRequest request) {
		return internalServerError(e.getLocalizedMessage());
	}

	private ResponseEntity<ResponseModel> internalServerError(String message) {
		return new ResponseEntity<>(
				new ResponseModel(new Date().toString(), HttpStatus.INTERNAL_SERVER_ERROR, Map.of("message", message)),
				new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);

	}
}
