package com.inventory.service;


import com.inventory.dto.AvaiableRoomDto;
import com.inventory.entity.AvaiableRooms;
import com.inventory.reposistory.AvaibleRoomRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public class AvaiableRoomImpel implements AvaiableRoomInterf{

    @Autowired
    private  AvaibleRoomRepo avaibleRoomRepo;

    @Autowired
    SchedullingTask schedullingTask;



    @Transactional
    public AvaiableRoomDto bookedRoomForSpecificDate(AvaiableRoomDto avaiableRooms) {

        String aid = UUID.randomUUID().toString().substring(0,5);
        AvaiableRooms rooms =  new AvaiableRooms(aid,
                avaiableRooms.roomId(),
                avaiableRooms.checkOutDate(),
                avaiableRooms.checkInDate(),
                avaiableRooms.bid()
        );
        avaibleRoomRepo.save(rooms);
        //task for shedulling
        schedullingTask.SchedulerTask(new Runnable() {
            @Override
            public void run() {
                deleteBookings(aid);
            }
        }, avaiableRooms.checkOutDate());

        return avaiableRooms;
    }


    @Override
    public boolean isRoomBooked(AvaiableRoomDto avaiableRooms) {

        boolean flag = avaibleRoomRepo.existsByRoomIdAndCheckInDateLessThanAndCheckOutDateGreaterThan(avaiableRooms.roomId(),
                avaiableRooms.checkOutDate(), avaiableRooms.checkInDate());
        return flag;
    }


    @Override
    public boolean deleteBookings(String bid) {
       AvaiableRooms rooms =  avaibleRoomRepo.findByBid(bid).orElseThrow(()->new RuntimeException("No such bookings available..."));
       avaibleRoomRepo.delete(rooms);
       System.out.println(rooms);
       return true;

    }


}
