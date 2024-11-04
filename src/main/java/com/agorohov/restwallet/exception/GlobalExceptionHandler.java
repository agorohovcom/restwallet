package com.agorohov.restwallet.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(WalletNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleWalletNotFoundException(WalletNotFoundException e) {
        Map<String, String> response = new HashMap<>();
        response.put("error", e.getMessage());

        logger.error(e.getMessage());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotEnoughBalanceException.class)
    public ResponseEntity<Map<String, String>> handleNotEnoughBalanceException(NotEnoughBalanceException e) {
        Map<String, String> response = new HashMap<>();
        response.put("error", e.getMessage());

        logger.error(e.getMessage());

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(TooLargeBalanceException.class)
    public ResponseEntity<Map<String, String>> handleTooLargeBalanceException(TooLargeBalanceException e) {
        Map<String, String> response = new HashMap<>();
        response.put("error", e.getMessage());

        logger.error(e.getMessage());

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({
            HttpMessageNotReadableException.class,
            MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<Map<String, String>> handleInvalidArgumentExceptions(Exception e) {
        Map<String, String> response = new HashMap<>();
        String message = "Запрос не может быть прочитан, проверьте передаваемые аргументы";
        response.put("error", message);

        logger.error(message);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> response = new LinkedHashMap<>();
        String message = "Ошибка валидации";
        response.put("error", message);
        e.getBindingResult().getFieldErrors().forEach(error ->
                response.put(error.getField(), error.getDefaultMessage()));

        logger.error("{}: {}", message, response);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
