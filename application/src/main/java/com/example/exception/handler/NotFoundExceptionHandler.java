package com.example.exception.handler;

import com.example.dto.error.ErrorResponse;
import com.example.exception.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class NotFoundExceptionHandler {

    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                "404",
                exception.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

}
