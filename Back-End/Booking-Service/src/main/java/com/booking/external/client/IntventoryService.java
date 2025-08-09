package com.booking.external.client;

import com.booking.external.others.AvaiableRoomDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

//@FeignClient(name = "INVENTORY-SERVICE")
@FeignClient(name = "inventory-service", url = "${inventory-service.url}")
public interface IntventoryService {


    @PostMapping("/room-check/booked")
    public ResponseEntity<AvaiableRoomDto> bookedRoom(@RequestBody AvaiableRoomDto avaiableRooms);

    @PostMapping("/room-check/is-booked")
    public boolean isBooked(@RequestBody AvaiableRoomDto avaiableRooms);


}

