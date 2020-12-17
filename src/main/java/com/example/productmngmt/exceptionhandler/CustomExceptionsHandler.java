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
		return internalServerError(Map.of(Constants.MESSAGE_KEY,e.getLocalizedMessage()));
	}

	@ExceptionHandler(ProductAlreadyExists.class)
	public final ResponseEntity<ResponseModel> productHandler(ProductAlreadyExists e, WebRequest request) {
		return internalServerError(Map.of(Constants.MESSAGE_KEY,e.getLocalizedMessage()));
	}

//	@ExceptionHandler(DuplicateKeyException.class)
//	public final ResponseEntity<ResponseModel> duplicateProductHandler(DuplicateKeyException e, WebRequest request) {
//		return internalServerError(Map.of(Constants.MESSAGE_KEY,Constants.PRODUCT_WITH_NAME + e.getLocalizedMessage().substring(e.getLocalizedMessage().indexOf("name: ") + 7,
//				e.getLocalizedMessage().indexOf("}") - 2)+ Constants.ALREADY_EXITS));
//	}

	@ExceptionHandler(NegativeArgumentException.class)
	public final ResponseEntity<ResponseModel> negativeArgument(NegativeArgumentException e, WebRequest request) {
		return internalServerError(Map.of(Constants.MESSAGE_KEY,e.getLocalizedMessage()));
	}

	@ExceptionHandler(BadCredsException.class)
	public final ResponseEntity<ResponseModel> badCred(BadCredsException e, WebRequest request) {
		return new ResponseEntity<>(new ResponseModel(new Date(), HttpStatus.UNAUTHORIZED, Map.of(Constants.MESSAGE_KEY,e.getLocalizedMessage())),
				new HttpHeaders(), HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(ExpiredJwtTokenException.class)
	public final ResponseEntity<ResponseModel> expiredJwtToken(ExpiredJwtTokenException e, WebRequest request) {
		return internalServerError(Map.of(Constants.MESSAGE_KEY,e.getLocalizedMessage()));
	}

	private ResponseEntity<ResponseModel> internalServerError(Object message) {
		return new ResponseEntity<>(new ResponseModel(new Date(), HttpStatus.INTERNAL_SERVER_ERROR, message),
				new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);

	}
}
