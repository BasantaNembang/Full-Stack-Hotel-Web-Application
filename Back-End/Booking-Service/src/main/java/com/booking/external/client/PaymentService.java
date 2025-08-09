package com.booking.external.client;

import com.booking.external.others.PaymentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


//@FeignClient(name = "PAYMENT-SERVICE")
@FeignClient(name = "payment-service", url = "${payment-service.url}")
public interface PaymentService {

    @PostMapping("/payment/do")
    public PaymentDto doPayment(@RequestBody PaymentDto paymentDto);

    @DeleteMapping("/payment/delete/{bid}")
    public Boolean deletePayment(@PathVariable("bid") String bid);


}
