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
public class CustonException extends ResponseEntityExceptionHandler{
	
	@ExceptionHandler(NoSuchProductFound.class)
	public final ResponseEntity<?> handelAnyException(Exception e,WebRequest request){
		return new ResponseEntity<>(new ResponseMessage(new Date(),HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage()),new HttpHeaders(),HttpStatus.INTERNAL_SERVER_ERROR);
	}
	

}
