package com.booking.dto;

import java.time.LocalDate;

public record BookingDto(
         String bid,
         String full_name,
         String email,
         LocalDate check_in_date,
         LocalDate check_out_date,
         int adults,
         int children,
         int room_price,
         int total_guest,
         String room_id,
         String room_type,
         String confirmation_code,
         BookingStatus bookingStatus
) {
}
