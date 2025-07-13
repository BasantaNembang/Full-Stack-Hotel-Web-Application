package com.booking.external.client;

import com.booking.error.BookingException;
import com.booking.external.others.PaymentDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpel {

    private final PaymentService paymentService;

    public PaymentServiceImpel(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Retry(name = "external", fallbackMethod = "failPayment")
    public PaymentDto doPayment(PaymentDto paymentDto){
        return paymentService.doPayment(paymentDto);
    }

    @CircuitBreaker(name = "external", fallbackMethod = "faildeletePayment")
    public Boolean deletePayment(String bid){
        return paymentService.deletePayment(bid);
    }


    public PaymentDto failPayment(PaymentDto paymentDto, Throwable throwable){
        System.out.println("Error is :  "+throwable.getMessage());
        throw new BookingException("Error is  "+throwable.getMessage());
    }

    public Boolean faildeletePayment(String bid, Throwable throwable){
        System.out.println("Error is :  "+throwable.getMessage());
        throw new BookingException("Error is  "+throwable.getMessage());

    }


}
