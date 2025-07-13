package com.inventory.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AvaiableRooms {

    @Id
    private String aid;

    @Column(name = "room_id")
    private String roomId;

    @Column(name = "check_out_date")
    private LocalDate checkOutDate;


    @Column(name = "check_in_date")
    private LocalDate checkInDate;


    private String bid;

}
