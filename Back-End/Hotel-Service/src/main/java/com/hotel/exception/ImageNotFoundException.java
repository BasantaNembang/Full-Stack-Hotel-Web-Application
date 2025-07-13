package com.hotel.exception;

public class ImageNotFoundException extends RuntimeException{

     public   ImageNotFoundException(){
        super("No Image");
    }

     public ImageNotFoundException(String msg){
        super(msg);
    }


}
