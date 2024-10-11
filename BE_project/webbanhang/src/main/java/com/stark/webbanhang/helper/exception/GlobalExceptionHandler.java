package com.stark.webbanhang.helper.exception;

import com.stark.webbanhang.helper.base.response.ResponseObject;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Xử lý ngoại lệ khi dữ liệu không hợp lệ
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseObject<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((org.springframework.validation.FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ResponseObject<Map<String, String>> response = new ResponseObject<>("Validation failed", HttpStatus.BAD_REQUEST.value(), errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Xử lý ngoại lệ khi thực thể không được tìm thấy
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ResponseObject<String>> handleEntityNotFoundException(EntityNotFoundException ex) {
        ResponseObject<String> response = new ResponseObject<>(ex.getMessage(), HttpStatus.NOT_FOUND.value(), null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // Xử lý ngoại lệ khi tham số không hợp lệ
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseObject<String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        ResponseObject<String> response = new ResponseObject<>(ex.getMessage(), HttpStatus.BAD_REQUEST.value(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Xử lý ngoại lệ chung
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseObject<String>> handleGenericException(Exception ex) {
        ResponseObject<String> response = new ResponseObject<>("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Xử lý ngoại lệ AccessDeniedException
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponseObject<String>> handlingAccessDeniedException(AccessDeniedException exception) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        ResponseObject<String> response = new ResponseObject<>(errorCode.getMessage(), errorCode.getCode(), null);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    // Xử lý ngoại lệ AppException
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ResponseObject<String>> handlingAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        ResponseObject<String> response = new ResponseObject<>(errorCode.getMessage(), errorCode.getCode(), null);
        return new ResponseEntity<>(response, HttpStatus.valueOf(errorCode.getStatusCode().value()));
    }
}
