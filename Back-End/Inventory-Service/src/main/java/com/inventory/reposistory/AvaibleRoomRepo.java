package com.inventory.reposistory;

import com.inventory.entity.AvaiableRooms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface AvaibleRoomRepo extends JpaRepository<AvaiableRooms, String> {


    boolean existsByRoomIdAndCheckInDateLessThanAndCheckOutDateGreaterThan
            (String RoomId, LocalDate CheckOutDate,
    LocalDate CheckInDate);

    Optional<AvaiableRooms> findByBid(String bid);

}
