package com.booking.error;

import org.springframework.http.HttpStatus;

public class BookingException extends RuntimeException{

    boolean flag;
    HttpStatus httpStatus;

    public  BookingException(String msg, boolean flag, HttpStatus httpStatus){
        super(msg);
        this.flag=flag;
        this.httpStatus=httpStatus;
    }

    public BookingException(){
        super("Something went wrong......");
    }

    public BookingException(String message) {
        super(message);
    }


    public boolean isFlag() {
        return flag;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }


}
