package com.payment.repository;

import com.payment.enitty.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepo extends JpaRepository<Payment, String> {

    Optional<Payment> findByBid(String bid);


}
