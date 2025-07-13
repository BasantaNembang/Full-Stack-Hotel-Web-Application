package com.booking.repository;


import com.booking.model.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookingRepo extends MongoRepository<Booking, String> {

    Optional<Booking> findByConfirmationCode(String cid);

}
