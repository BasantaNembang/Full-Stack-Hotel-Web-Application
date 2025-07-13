package com.hotel.controler;


import com.hotel.dto.ApiResponse;
import com.hotel.exception.ImageNotFoundException;
import com.hotel.exception.ResourceNotFoundExcp;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHaldler {

    @ExceptionHandler(ResourceNotFoundExcp.class)
    public ResponseEntity<ApiResponse> handlerExceptions(ResourceNotFoundExcp excp) {
        ApiResponse response = new ApiResponse();
        response.setBool(false);
        response.setMessage(excp.getMessage());
        response.setHttpStatus(HttpStatus.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }



    @ExceptionHandler(ImageNotFoundException.class)
    public ResponseEntity<ApiResponse> handlerImageNotFoundExceptions(ImageNotFoundException exception){
        ApiResponse response = new ApiResponse();
        response.setBool(false);
        response.setMessage(exception.getMessage());
        response.setHttpStatus(HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }


}
