package com.booking.model;


import com.booking.dto.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Booking {

    @Id
    private String bid;
    private String full_name;
    private String email;
    private LocalDate check_in_date;
    private LocalDate check_out_date;
    private int adults;
    private int children;
    private int room_price;
    private int total_guest;
    private String room_id;
    private String room_type;
    @Field("confirmation_code")
    private String confirmationCode;
    BookingStatus status;

}
