package com.booking.controller;

import com.booking.error.BookingException;
import com.booking.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionConteroller {

@ExceptionHandler(BookingException.class)
public ResponseEntity<ErrorResponse> handelHotelError(BookingException exception){
    ErrorResponse response = new ErrorResponse();
    response.setFlag(exception.isFlag());
    response.setMsg(exception.getMessage());
    response.setHttpStatus(exception.getHttpStatus());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(response);
}
}


