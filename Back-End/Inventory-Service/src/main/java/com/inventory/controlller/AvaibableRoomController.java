package com.inventory.controlller;


import com.inventory.dto.AvaiableRoomDto;
import com.inventory.service.AvaiableRoomImpel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/room-check")
public class AvaibableRoomController {

    @Autowired
    private AvaiableRoomImpel avaiableRoomImpel;

    @PostMapping("/booked")
    public ResponseEntity<AvaiableRoomDto> bookedRoom(@RequestBody AvaiableRoomDto avaiableRooms){
        return ResponseEntity.status(HttpStatus.OK).body(avaiableRoomImpel.bookedRoomForSpecificDate(avaiableRooms));
    }

    @PostMapping("/is-booked")
    public boolean isBooked(@RequestBody AvaiableRoomDto avaiableRooms){
        return avaiableRoomImpel.isRoomBooked(avaiableRooms);
    }


    @DeleteMapping("/delete/{bid}")
    public boolean deleteBooking(@PathVariable("bid") String bid){
        return avaiableRoomImpel.deleteBookings(bid);
    }


}
