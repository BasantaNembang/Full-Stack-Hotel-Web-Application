package com.hotel.dto;

import org.springframework.http.HttpStatus;

public class ApiResponse {

    private String message;
    private boolean bool;
    private HttpStatus httpStatus;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isBool() {
        return bool;
    }

    public void setBool(boolean bool) {
        this.bool = bool;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "message='" + message + '\'' +
                ", bool=" + bool +
                ", httpStatus=" + httpStatus +
                '}';
    }


}
