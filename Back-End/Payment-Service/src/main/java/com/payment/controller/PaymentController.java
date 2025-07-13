package com.payment.controller;


import com.payment.dto.PaymentDto;
import com.payment.serivce.PaymentImpel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payment")
public class PaymentController {


    @Autowired
    private PaymentImpel paymentImpel;

    @PostMapping("/do")
    public PaymentDto doPayment(@RequestBody PaymentDto paymentDto){
        return paymentImpel.saveDetailsPayment(paymentDto);
    }

    //delete via booking id
    @DeleteMapping("/delete/{bid}")
    public Boolean deletePayment(@PathVariable("bid") String bid){
        return paymentImpel.deleteDetailsPayment(bid);
    }

    @GetMapping("/all")
    public List<PaymentDto> getAllPaymentDetails(){
        return paymentImpel.getALlPayment();
    }


}
