package com.vst.JwtSpringSecurity.error;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.vst.JwtSpringSecurity.exception.NotAcceptableException;
import com.vst.JwtSpringSecurity.exception.NotFoundException;
import com.vst.JwtSpringSecurity.exception.ValidatorException;

@RestControllerAdvice
public class SecurityError {

	
String message = "error message";
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(NotFoundException.class)
	public Map<String,Object> notFound(NotFoundException ex) {
		
		Map<String,Object> errorMap = new HashMap<>();
		ApiResponse error = new ApiResponse();		
//		error.setCode("404");
		error.setMessage(ex.getMessage());
//		error.setDescription("Details Not Available");
//		error.setTimeStamp(LocalDateTime.now());
//		error.setReason(ex.getMessage());
//		errorMap.put(message, error);
		return errorMap;
	}
	
	
	@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
	@ExceptionHandler(NotAcceptableException.class)
	public Map<String,Object> notAcceptable(NotAcceptableException ex) {
		Map<String,Object> errorMap = new HashMap<>();
		ApiResponse error = new ApiResponse();		
//		error.setCode("400");
		error.setMessage(ex.getMessage());
//		error.setDescription("NOT ACCEPTABLE");
//		error.setTimeStamp(LocalDateTime.now());
//		error.setReason(ex.getMessage());
//		errorMap.put(message, error);
		return errorMap;
		
	
	}
	


	@ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleInvalidArgument(MethodArgumentNotValidException ex) {
        Map<String, String> errorMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->{
            errorMap.put(error.getField(), error.getDefaultMessage());
        });
        return errorMap;
    }
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(ValidatorException.class)
	public Map<String, Object> validatorException(ValidatorException ex) {
	    Map<String, Object> errorMap = new HashMap<>();
	    ApiResponse error = new ApiResponse();
	    error.setReason(ex.getMessage());
	    errorMap.put("message", error);
	    return errorMap;
	    
	}

		
	
	
}
