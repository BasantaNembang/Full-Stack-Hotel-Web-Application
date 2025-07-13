package com.user.controler;


import com.user.dto.ApiResponse;
import com.user.excption.ResourceNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHaldler {

    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<ApiResponse> haldelUserNotFoundException(ResourceNotFound notFound){
        ApiResponse response = new ApiResponse();
        response.setBool(false);
        response.setMessage(notFound.getMessage());
        response.setHttpStatus(HttpStatus.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }



}
