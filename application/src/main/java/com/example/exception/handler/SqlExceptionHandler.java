package com.example.exception.handler;

import com.example.dto.error.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
@RequiredArgsConstructor
public class SqlExceptionHandler {

    private final Map<String, String> violationsMap;

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(
            DataIntegrityViolationException exception) {

        var message = violationsMap.entrySet().stream()
                .filter(entry -> exception.getMessage().contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .findAny()
                .orElse("Unexpected exception acquired");

        ErrorResponse errorResponse = new ErrorResponse(
                "400",
                message
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
