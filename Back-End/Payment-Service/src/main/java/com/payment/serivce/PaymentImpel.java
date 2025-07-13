package com.payment.serivce;


import com.payment.dto.PaymentDto;
import com.payment.dto.PaymentStatus;
import com.payment.enitty.Payment;
import com.payment.repository.PaymentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public class PaymentImpel implements PaymentServiceInterF {

    @Autowired
    private PaymentRepo paymentRepo;

    @Override
    @Transactional
    public PaymentDto saveDetailsPayment(PaymentDto paymentDto) {
       // System.out.println("Date  "+  paymentDto.total_price());
        Payment payment = new Payment(
                UUID.randomUUID().toString().substring(0,5),
                paymentDto.bid(),
                Instant.now(),
                paymentDto.total_price(),
                paymentDto.paymentMode(),
                PaymentStatus.SUCCESS
        );
        paymentRepo.save(payment);
        return paymentDto;
    }

    @Override
    public Boolean deleteDetailsPayment(String bid) {
        Payment payment =  paymentRepo.findByBid(bid).orElseThrow(()->new RuntimeException("No such Bookings available..."));
        paymentRepo.delete(payment);
        return true;
    }

    @Override
    public List<PaymentDto> getALlPayment() {
        List<Payment> payments = paymentRepo.findAll();
        return payments.stream()
                .map(payment->new PaymentDto(payment.getPid(), payment.getBid(), payment.getPayment_date(), payment.getTotal_price(), payment.getPaymentMode(), payment.getPaymentStatus()))
                .toList();
    }
}
