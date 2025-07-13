
package com.booking.external.others;


import java.time.Instant;


public record PaymentDto(String pid,
                         String bid,
                         Instant payment_date,
                         int total_price,
                         PaymentMode paymentMode,
                         PaymentStatus paymentStatus) {
}
