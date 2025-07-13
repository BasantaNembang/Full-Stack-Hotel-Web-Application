
package com.payment.dto;

import java.time.Instant;

public record PaymentDto(String pid,
         String bid,
         Instant payment_date,
         Integer total_price,
         PaymentMode paymentMode,
         PaymentStatus paymentStatus) {
}
