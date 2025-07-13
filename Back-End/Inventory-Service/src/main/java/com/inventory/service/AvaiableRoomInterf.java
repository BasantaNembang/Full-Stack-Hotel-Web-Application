package com.inventory.service;

import com.inventory.dto.AvaiableRoomDto;

public interface AvaiableRoomInterf {

    AvaiableRoomDto bookedRoomForSpecificDate(AvaiableRoomDto avaiableRooms);

    boolean isRoomBooked(AvaiableRoomDto avaiableRooms);

    boolean deleteBookings(String bid);


}
