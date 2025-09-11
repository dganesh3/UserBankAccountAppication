package com.user.CustomerProfile.Exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.user.CustomerProfile.Dto.ResponseStructure;



@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler 
{
	public ResponseEntity<Object> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

	@ExceptionHandler(IdNotFoundException.class)
	public ResponseEntity<ResponseStructure<String>> handleIdNotFoundException(IdNotFoundException exception) {

		ResponseStructure<String> structure = new ResponseStructure<String>();
		structure.setStatusCode(HttpStatus.NOT_FOUND.value());
		structure.setMessage(PAGE_NOT_FOUND_LOG_CATEGORY);
		structure.setData(exception.getMessage());

		return new ResponseEntity<ResponseStructure<String>>(structure, HttpStatus.NOT_FOUND);

	}
	
	 @ExceptionHandler(UserNotFoundException.class)
	    public ResponseEntity<ResponseStructure<String>> handleUserNotFound(UserNotFoundException ex) {
	        ResponseStructure<String> structure = new ResponseStructure<>();
	        structure.setStatusCode(HttpStatus.NOT_FOUND.value());
	        structure.setMessage("User Not Found");
	        structure.setData(ex.getMessage());

	        return new ResponseEntity<>(structure, HttpStatus.NOT_FOUND);
	    }
	
}
