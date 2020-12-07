package com.example.productmngmt.exceptionhandler;

import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionsHandler extends ResponseEntityExceptionHandler{
	
	@ExceptionHandler(NoSuchProductFound.class)
	public final ResponseEntity<ResponseMessage> noProductFound(NoSuchProductFound e,WebRequest request){
		return new ResponseEntity<>(new ResponseMessage(new Date(),HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage()),new HttpHeaders(),HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(ProductAlreadyExists.class)
	public final ResponseEntity<ResponseMessage> productHandler(ProductAlreadyExists e,WebRequest request){
		return new ResponseEntity<>(new ResponseMessage(new Date(),HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage()),new HttpHeaders(),HttpStatus.INTERNAL_SERVER_ERROR);		
	}
	
	@ExceptionHandler(NegativeArgumentException.class)
	public final ResponseEntity<ResponseMessage> negativeArgument(NegativeArgumentException e,WebRequest request){
		return new ResponseEntity<>(new ResponseMessage(new Date(),HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage()),new HttpHeaders(),HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
