package com.hotel.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundExcp extends RuntimeException {

    boolean flag;
    HttpStatus httpStatus;

    public  ResourceNotFoundExcp(String msg, boolean flag, HttpStatus httpStatus){
        super(msg);
  }

    public ResourceNotFoundExcp(){
        super("Something went wrong......");
    }

    public ResourceNotFoundExcp(String message) {
        super(message);
    }


    public boolean isFlag() {
        return flag;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }


}
