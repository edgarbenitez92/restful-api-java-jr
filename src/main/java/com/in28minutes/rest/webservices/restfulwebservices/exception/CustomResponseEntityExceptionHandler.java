package com.in28minutes.rest.webservices.restfulwebservices.exception;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.in28minutes.rest.webservices.restfulwebservices.user.UserNotFoundException;

@ControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<ErrorDetails> handleServerException(Exception ex, WebRequest request) throws Exception {
		LocalDateTime date = LocalDateTime.now();
		ErrorDetails errorDetails = new ErrorDetails(date, ex.getMessage(), request.getDescription(false));
		ResponseEntity<ErrorDetails> internalServerError = new ResponseEntity(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
		return internalServerError;
	}

	@ExceptionHandler(UserNotFoundException.class)
	public final ResponseEntity<ErrorDetails> handleUserNotFoundException(Exception ex, WebRequest request) throws Exception {
		LocalDateTime date = LocalDateTime.now();
		ErrorDetails errorDetails = new ErrorDetails(date, ex.getMessage(), request.getDescription(false));
		ResponseEntity<ErrorDetails> internalServerError = new ResponseEntity(errorDetails, HttpStatus.NOT_FOUND);
		return internalServerError;
	}
}
