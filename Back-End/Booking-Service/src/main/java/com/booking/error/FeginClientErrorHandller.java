package com.booking.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;

import java.io.IOException;

public class FeginClientErrorHandller implements ErrorDecoder {


    @Override
    public Exception decode(String methodKey, Response response) {

        ObjectMapper mapper = new ObjectMapper();

        ErrorResponse errorResponse;
        try {
        errorResponse =    mapper.readValue(response.body().asInputStream(), ErrorResponse.class);
//            System.out.println(response.body().asInputStream());
//            System.out.println(errorResponse);
        return new BookingException(errorResponse.getMsg(), errorResponse.isFlag(), errorResponse.getHttpStatus());

        } catch (IOException e) {
            System.out.println("IO ......"+e.getMessage());
            throw new BookingException(e.getMessage());
        }


    }


}
