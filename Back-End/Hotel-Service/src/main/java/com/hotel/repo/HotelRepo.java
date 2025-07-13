package com.hotel.repo;

import com.hotel.entity.Hotel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HotelRepo extends JpaRepository<Hotel, String> {

    List<Hotel> findByRoomtype(Pageable pageable, String type);

}
