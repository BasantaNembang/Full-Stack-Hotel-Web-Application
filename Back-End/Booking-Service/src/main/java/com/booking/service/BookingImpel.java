package com.booking.service;


import com.booking.dto.BookingDto;
import com.booking.dto.BookingStatus;
import com.booking.error.BookingException;
import com.booking.external.client.InventoryServiceImpel;
import com.booking.external.client.PaymentServiceImpel;
import com.booking.external.others.*;
import com.booking.model.Booking;
import com.booking.repository.BookingRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.List;
import java.util.UUID;


@Service
public class BookingImpel implements BookingInterface {

    @Autowired
    private BookingRepo bookingRepo;

    @Autowired
    private InventoryServiceImpel intventoryService;

    @Autowired
    private PaymentServiceImpel paymentService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${spring.inventory.kafka}")
    private String inventoryEvent;

    @Autowired
    private KafkaTemplate<String, com.booking.event.BookedRoomEvent> kafkaTemplate;

    @Autowired
    private KafkaTemplate<String, com.booking.event.InventoryServiceEvent> kafkaTemplate1;

    ObjectMapper objectMapper = new ObjectMapper();

    @Value("${hotel.service.url}")
    private String hotelURL;


    @Override
    @Transactional
    public BookingDto bookRoom(BookingDto bookingDto) {

        String bid = UUID.randomUUID().toString().substring(0, 5);
        Booking booking = new Booking();

        HotelDto hotelDto;
        //System.out.println(hotelURL+"/hotel/get-room/");
        try {
            hotelDto = restTemplate.getForObject(hotelURL+"/hotel/get-room/" + bookingDto.room_id(), HotelDto.class);
           } catch (Exception e) {
            throw new BookingException("No such Room found!...", false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(hotelDto==null){
            throw new BookingException("No such Room found!...", false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        //check room available or not
        AvaiableRoomDto avaiableRoomDto = new AvaiableRoomDto(UUID.randomUUID().toString().substring(0, 5),
                bookingDto.room_id(),
                bookingDto.check_out_date(),
                bookingDto.check_in_date(),
                bid
        );

        boolean aBoolean;
        try{
            aBoolean =   intventoryService.isBooked(avaiableRoomDto);
            } catch (Exception e) {
            throw new BookingException("Room has been booked already...", false, HttpStatus.INTERNAL_SERVER_ERROR);
        }

         if (aBoolean) {
            throw new BookingException("Room has been booked already...", false, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //if not then booked...... after payment is completed

        //do payment
        try {
            PaymentDto paymentDto = new PaymentDto(UUID.randomUUID().toString().substring(0, 5),
                    bid,
                    Instant.now(),
                    bookingDto.room_price(),
                    PaymentMode.CASH,
                    PaymentStatus.PENDING);

            booking.setStatus(BookingStatus.PENDING);
            paymentService.doPayment(paymentDto);

            intventoryService.bookedRoom(avaiableRoomDto);

        } catch (Exception e) {
            booking.setStatus(BookingStatus.FAILED);
            throw new BookingException("something wrong with payment service", false, HttpStatus.INTERNAL_SERVER_ERROR );
        }

        //save to DB
        booking.setStatus(BookingStatus.SUCCESS);

        booking.setBid(bid);
        booking.setFull_name(bookingDto.full_name());
        booking.setEmail(bookingDto.email());
        booking.setCheck_in_date(bookingDto.check_in_date());
        booking.setCheck_out_date(bookingDto.check_out_date());
        booking.setAdults(bookingDto.adults());
        booking.setChildren(bookingDto.children());

        booking.setRoom_price(hotelDto.roomprice());
        booking.setTotal_guest(bookingDto.adults() + bookingDto.children());

        booking.setRoom_id(bookingDto.room_id());
        booking.setRoom_type(hotelDto.roomtype());
        booking.setConfirmationCode(UUID.randomUUID().toString().substring(0, 7));

        bookingRepo.save(booking);


        //send notification
        com.booking.event.BookedRoomEvent event = new com.booking.event.BookedRoomEvent();
        event.setGuest(bookingDto.email());
        event.setSubject("Your Hotel has been booked successfully");
        event.setConformationNumber(booking.getConfirmationCode());
        kafkaTemplate.send("booked-room", event);


        //return back
        BookingDto dto = new BookingDto(booking.getBid(),
                booking.getFull_name(),
                booking.getEmail(),
                booking.getCheck_in_date(),
                booking.getCheck_out_date(),
                booking.getAdults(),
                booking.getChildren(),
                booking.getRoom_price(),
                booking.getTotal_guest(),
                booking.getRoom_id(),
                booking.getRoom_type(),
                booking.getConfirmationCode(),
                booking.getStatus()
        );

        System.out.println("Your Bookings has been saved.....");
        return dto;
    }


    @Override
    public List<BookingDto> getAllBookRooms() {
        List<Booking> booking = bookingRepo.findAll();
        List<BookingDto> bookingdtos = booking.stream()
                .map(bookings -> new BookingDto(bookings.getBid(), bookings.getFull_name(), bookings.getEmail(), bookings.getCheck_in_date(), bookings.getCheck_out_date(),
                        bookings.getAdults(), bookings.getChildren(), bookings.getRoom_price(), bookings.getTotal_guest(),
                        bookings.getRoom_id(), bookings.getRoom_type(), bookings.getConfirmationCode(), bookings.getStatus()))
                .toList();
        return bookingdtos;
    }


    @Override
    public BookingDto getRoomByID(String bid) {
        Booking booking = bookingRepo.findById(bid).orElseThrow(() -> new BookingException("No such Bookings is avaiable....", false, HttpStatus.INTERNAL_SERVER_ERROR));
        return new BookingDto(bid, booking.getFull_name(), booking.getEmail(), booking.getCheck_in_date(), booking.getCheck_out_date(),
                booking.getAdults(), booking.getChildren(), booking.getRoom_price(), booking.getTotal_guest(),
                booking.getRoom_id(), booking.getRoom_type(), booking.getConfirmationCode(), booking.getStatus());
    }


    @Override
    @Transactional
    public BookingDto deleteBookings(String bid) {

        Booking booking = bookingRepo.findById(bid).orElseThrow(() -> new BookingException("No order present....", false, HttpStatus.INTERNAL_SERVER_ERROR));
        Boolean bool = null;
        Boolean bool0 = null;
        //delete from Payment-Service
        try {
            bool = paymentService.deletePayment(bid);
            } catch (Exception e) {
            throw new BookingException(e.getMessage() ,false, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //delete from Inventory-Service
        //use kafka-to delete inventory service to make sure if its is not available at the movement it will delete eventaully
        if (bool==true) {
            try {
                com.booking.event.InventoryServiceEvent event = new com.booking.event.InventoryServiceEvent();
                event.setSubject("Delete Booked Room");
                event.setBid(booking.getBid());
                kafkaTemplate1.send(inventoryEvent, event);
                bool0 = true;
            } catch (Exception e) {
             throw new BookingException("something went wrong while sending kafka-msg to inventory service" ,false, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        if (bool==true && bool0==true) {
            try {
                bookingRepo.delete(booking);
            } catch (Exception e) {
                throw new BookingException("something went wrong while sending kafka-msg to inventory service", false, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new BookingDto(bid, booking.getFull_name(), booking.getEmail(), booking.getCheck_in_date(), booking.getCheck_out_date(),
                booking.getAdults(), booking.getChildren(), booking.getRoom_price(), booking.getTotal_guest(),
                booking.getRoom_id(), booking.getRoom_type(), booking.getConfirmationCode(), booking.getStatus());
    }




    @Override
    public BookingDto getRoomByCID(String cid) {
        Booking booking = bookingRepo.findByConfirmationCode(cid).orElseThrow(() -> new BookingException(
                "no such booking of confirmation data is avaiable", false, HttpStatus.INTERNAL_SERVER_ERROR
        ));

        return new BookingDto(booking.getBid(), booking.getFull_name(), booking.getEmail(), booking.getCheck_in_date(), booking.getCheck_out_date(),
                booking.getAdults(), booking.getChildren(), booking.getRoom_price(), booking.getTotal_guest(),
                booking.getRoom_id(), booking.getRoom_type(), cid, booking.getStatus());
    }

}


