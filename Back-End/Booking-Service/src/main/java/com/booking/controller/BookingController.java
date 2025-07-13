package com.booking.controller;


import com.booking.dto.BookingDto;
import com.booking.service.BookingImpel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingImpel bookingImpel;


    @PostMapping("/place")
    public ResponseEntity<BookingDto> bookRooms(@RequestBody BookingDto bookingDto){
       return ResponseEntity.status(HttpStatus.OK).body(bookingImpel.bookRoom(bookingDto));
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<BookingDto>> getAllBookRooms(){
       return ResponseEntity.status(HttpStatus.OK).body(bookingImpel.getAllBookRooms());
   }


    @GetMapping("/get/{bid}")
    public ResponseEntity<BookingDto> getBookingByID(@PathVariable("bid") String  bid){
        return ResponseEntity.status(HttpStatus.OK).body(bookingImpel.getRoomByID(bid));
    }

    @DeleteMapping("/delete/{bid}")
    public ResponseEntity<BookingDto> deleteBookings(@PathVariable("bid") String  bid){
        return ResponseEntity.status(HttpStatus.OK).body(bookingImpel.deleteBookings(bid));
    }

    @GetMapping("/get/confirm-number/{cid}")
    public ResponseEntity<BookingDto> getBookingViaCID(@PathVariable("cid") String  cid){
        return ResponseEntity.status(HttpStatus.OK).body(bookingImpel.getRoomByCID(cid));
    }



}

