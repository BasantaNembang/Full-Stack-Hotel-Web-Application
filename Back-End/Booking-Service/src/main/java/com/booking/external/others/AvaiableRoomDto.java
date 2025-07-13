package com.booking.external.others;

import java.time.LocalDate;


public record AvaiableRoomDto(
        String aid,
        String roomId,
        LocalDate checkOutDate,
        LocalDate checkInDate,
        String bid
        ) {
}
