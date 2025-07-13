package com.booking.service;

import com.booking.dto.BookingDto;

import java.util.List;

public interface BookingInterface {

    BookingDto bookRoom(BookingDto bookingDto);

    List<BookingDto> getAllBookRooms();

    BookingDto getRoomByID(String bid);

    BookingDto deleteBookings(String bid);

    BookingDto getRoomByCID(String cid);

}


