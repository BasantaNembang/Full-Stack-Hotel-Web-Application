package com.booking.external.client;

import com.booking.error.BookingException;
import com.booking.external.others.AvaiableRoomDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class InventoryServiceImpel {

    private final IntventoryService intventoryService;

    public InventoryServiceImpel(IntventoryService intventoryService) {
        this.intventoryService = intventoryService;
    }

    @CircuitBreaker(name = "external", fallbackMethod = "failedBookedRoom")
    public ResponseEntity<AvaiableRoomDto> bookedRoom(AvaiableRoomDto avaiableRooms){
       return  intventoryService.bookedRoom(avaiableRooms);
    }


    @CircuitBreaker(name = "external", fallbackMethod = "failedIsBooked")
    public boolean isBooked(AvaiableRoomDto avaiableRooms){
        return intventoryService.isBooked(avaiableRooms);
    }



    public ResponseEntity<AvaiableRoomDto> failedBookedRoom(AvaiableRoomDto avaiableRooms, Throwable throwable){
        System.out.println("Error is :  "+throwable.getMessage());
        throw new BookingException("Error is  "+throwable.getMessage());
    }

    public boolean failedIsBooked(AvaiableRoomDto avaiableRooms, Throwable throwable){
        System.out.println("Error is :  "+throwable.getMessage());
        throw new BookingException("Error is  "+throwable.getMessage());
    }


}
