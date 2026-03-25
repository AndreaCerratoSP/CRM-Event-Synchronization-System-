package com.crm.sync.ingestion.advice;

import jakarta.validation.ConstraintViolationException;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import lombok.extern.slf4j.Slf4j;

import com.crm.sync.ingestion.dto.MessageResponseDTO;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


	// 500 //
    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageResponseDTO> handleAllExceptions(Exception ex) {
    	log.info("Generic Exception: "+ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

	// 400 //
	@ExceptionHandler({ BadRequestException.class,
		                HttpMessageNotReadableException.class,
		                MissingServletRequestParameterException.class,
		                MethodArgumentTypeMismatchException.class,
			            ConstraintViolationException.class})
    public ResponseEntity<MessageResponseDTO> handleBadRequestException(Exception ex) {
		log.info("Bad Request: "+ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
	
	private ResponseEntity<MessageResponseDTO> buildErrorResponse(String message, HttpStatus status) {
		MessageResponseDTO response = new MessageResponseDTO(message);
		return new ResponseEntity<>(response, status);
	}

}
