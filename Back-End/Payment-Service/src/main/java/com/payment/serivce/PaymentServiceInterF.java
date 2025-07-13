package com.payment.serivce;

import com.payment.dto.PaymentDto;

import java.util.List;

public interface PaymentServiceInterF {

    PaymentDto saveDetailsPayment(PaymentDto paymentDto);

    Boolean deleteDetailsPayment(String pid);

    List<PaymentDto> getALlPayment();
}
