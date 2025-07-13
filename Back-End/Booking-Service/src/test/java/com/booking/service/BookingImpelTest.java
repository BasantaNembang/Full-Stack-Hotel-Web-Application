package com.booking.service;

import com.booking.dto.BookingDto;
import com.booking.dto.BookingStatus;
import com.booking.error.BookingException;
import com.booking.event.BookedRoomEvent;
import com.booking.external.client.InventoryServiceImpel;
import com.booking.external.client.PaymentServiceImpel;
import com.booking.external.others.*;
import com.booking.model.Booking;
import com.booking.repository.BookingRepo;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.support.SendResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;


@SpringBootTest
public class BookingImpelTest {

    @Mock
    private BookingRepo bookingRepo;

    @Mock
    private InventoryServiceImpel intventoryService;

    @Mock
    private PaymentServiceImpel paymentService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private KafkaTemplate<String, com.booking.event.BookedRoomEvent> kafkaTemplate;

    @Mock
    private KafkaTemplate<String, com.booking.event.InventoryServiceEvent> kafkaTemplate1;

    @InjectMocks
    BookingInterface bookingService = new BookingImpel();

    //Steps for junit testing-> 1.Mocking 2.Actual Method-Call 3.Verification 4.Assertation

    private List<Booking> getMockBookingList() {
        List<Booking> bookingList =new ArrayList<>();
        Booking b0 = new Booking( "BID123",
                "Basanta ",
                "basanta@example.com",
                LocalDate.of(2025, 7, 5),
                LocalDate.of(2025, 7, 10),
                2,
                1,
                3000,
                3,
                "RID101",
                "Deluxe",
                "CONFIRM123",
                null);
        Booking b1 = new Booking( "BID123",
                "Basanta Nembang",
                "basantanembang@example.com",
                LocalDate.of(2025, 8, 5),
                LocalDate.of(2025, 8, 10),
                2,
                1,
                3000,
                3,
                "RID102",
                "King",
                "CONFIRM1233",
                null);
        bookingList.add(b0);
        bookingList.add(b1);
        return bookingList;
    }


    private com.booking.event.BookedRoomEvent getMockBookedRoomEvent() {
        return new BookedRoomEvent(null, "Guest", "Subject");
    }


    private Booking getMockBooking() {
        return new Booking(null, "Basanta", "basanta@gmail.com", LocalDate.of(2025,8,3), LocalDate.of(2025,8,5),
                2,1,799, 3,"RID", "RTYPE", null, BookingStatus.SUCCESS);
    }


    private PaymentDto getMockPaymentDTO() {
        return new PaymentDto(null, null, Instant.now(), 899, null, null);
    }

    private AvaiableRoomDto getMockAvaiableRoomDTO() {
        return new AvaiableRoomDto(null, "RID", LocalDate.of(2025, 8, 5),
                LocalDate.of(2025, 8, 3), null);
    }


    private HotelDto getMockHotelDto() {
        return new HotelDto(null, "Deluxe", "http//google.com/basanta.jpg", 989, "32UU");
    }


    private BookingDto getMockBookingDTO() {
        BookingDto bookingDto = new BookingDto(
                null,
                "Basanta Nembang",
                "basanta@example.com",
                LocalDate.of(2025, 8, 3),
                LocalDate.of(2025, 8, 5),
                2,
                1,
                5000,
                3,
                "RID",
                "Deluxe",
                null,
                null
        );
        return bookingDto;
    }


    @DisplayName("Place Order Success Case")
    @Test
    void BookRoom_Success(){

        BookingDto bookingDto = getMockBookingDTO();

        //Mocking
        Mockito.when(restTemplate.getForObject("http://HOTEL-SERVICE/hotel/get-room/" +bookingDto.room_id(), HotelDto.class))
                .thenReturn(getMockHotelDto());

        Mockito.when(intventoryService.isBooked(Mockito.any(AvaiableRoomDto.class)))
                        .thenReturn(false);

        Mockito.when(paymentService.doPayment(Mockito.any(PaymentDto.class)))
                        .thenReturn(getMockPaymentDTO());

        Mockito.when(intventoryService.bookedRoom(Mockito.any(AvaiableRoomDto.class)))
                        .thenReturn(new ResponseEntity<AvaiableRoomDto>(getMockAvaiableRoomDTO(), HttpStatus.OK));

       Mockito.when(bookingRepo.save(Mockito.any(Booking.class)))
               .thenReturn(getMockBooking());


        com.booking.event.BookedRoomEvent bookedRoomEvent = getMockBookedRoomEvent();
        Mockito.when(kafkaTemplate.send(Mockito.anyString(), Mockito.any(BookedRoomEvent.class)))
                .thenReturn(new CompletableFuture<SendResult<String, BookedRoomEvent>>());


        //Actual-Call
        BookingDto responseBookingDto = bookingService.bookRoom(bookingDto);

        //Verification
        Mockito.verify(restTemplate, Mockito.times(1))
                .getForObject("http://HOTEL-SERVICE/hotel/get-room/" +bookingDto.room_id(), HotelDto.class);

        Mockito.verify(intventoryService, Mockito.times(1))
                        .isBooked(Mockito.any(AvaiableRoomDto.class)); //error here

        Mockito.verify(paymentService, Mockito.times(1))
                .doPayment(Mockito.any(PaymentDto.class));

        Mockito.verify(intventoryService, Mockito.times(1))
                .bookedRoom(Mockito.any(AvaiableRoomDto.class));

        //Assertation
        Assertions.assertNotNull(responseBookingDto);
        Assertions.assertEquals(responseBookingDto.room_id(), bookingDto.room_id());

    }


    @DisplayName("Booked_Room Fail Case")
    @Test
    void BookRoom_Fail(){
        BookingDto bookingDto = getMockBookingDTO();

        //Mocking
        Mockito.when(restTemplate.getForObject("http://HOTEL-SERVICE/hotel/get-room/" +bookingDto.room_id(), HotelDto.class))
                .thenReturn(null);

        BookingException bookingException = Assertions.assertThrows(BookingException.class, ()->{
            bookingService.bookRoom(getMockBookingDTO());
        });

        //verification
        Mockito.verify(restTemplate, Mockito.times(1))
                .getForObject("http://HOTEL-SERVICE/hotel/get-room/" +bookingDto.room_id(), HotelDto.class);

        //assertation
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, bookingException.getHttpStatus());
        Assertions.assertEquals("No such Room found!...", bookingException.getMessage());

    }



    @DisplayName("Get_All_BookedRoom_Success_Case")
    @Test
    void Get_All_BookedRoom_Success(){
        //Mock
        Mockito.when(bookingRepo.findAll())
                .thenReturn(getMockBookingList());
        //Actual Method call
        List<BookingDto> booking =  bookingService.getAllBookRooms();
        //Verification
        Mockito.verify(bookingRepo, Mockito.times(1))
                .findAll();
        //Assertation
        Assertions.assertNotNull(booking);

    }



    @DisplayName("Get Booking Room By ID Success_Case")
    @Test
    void Get_BookingRoomIdSuccess_Case(){
      //Mocking
      Mockito.when(bookingRepo.findById("MWP1"))
                .thenReturn(Optional.of(getMockBooking()));

      //actual method call
      BookingDto bookingDto = bookingService.getRoomByID("MWP1");

      //Verification
      Mockito.verify(bookingRepo, Mockito.times(1))
              .findById("MWP1");

      //Assertation
      Assertions.assertNotNull(bookingDto);
      Assertions.assertEquals("MWP1", bookingDto.bid());

    }



    @DisplayName("Get Booking RoomByID Fail_Case")
    @Test
    void Get_BookingRoomIdFail_Case(){
        //Mocking
        Mockito.when(bookingRepo.findById(Mockito.anyString()))
                .thenReturn(Optional.ofNullable(null));

        BookingException bookingException = Assertions.assertThrows(BookingException.class,
                ()->{bookingService.getRoomByID("MWP1");});

        //Verification
        Mockito.verify(bookingRepo, Mockito.times(1))
                .findById("MWP1");

        //Assertation
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, bookingException.getHttpStatus());
        Assertions.assertEquals("No such Bookings is avaiable....", bookingException.getMessage());
    }



    @DisplayName("Delete Booking Success_Case")
    @Test
    void Delete_BookedRoomSuccess_Case(){
        //Mocking
        Mockito.when(bookingRepo.findById(Mockito.anyString()))
                .thenReturn(Optional.of(getMockBooking()));

        Mockito.when(paymentService.deletePayment(Mockito.anyString()))
                        .thenReturn(false);

        Mockito.when(kafkaTemplate1.send(Mockito.anyString(), Mockito.any(com.booking.event.InventoryServiceEvent.class)))
                .thenReturn(new CompletableFuture<SendResult<String, com.booking.event.InventoryServiceEvent>>());
        Mockito.doNothing().when(bookingRepo).delete(Mockito.any(Booking.class));

        //Actual method-call
        BookingDto bookingDto  =  bookingService.deleteBookings("MWP1");

        //Verification
        Mockito.verify(bookingRepo, Mockito.times(1))
                .findById("MWP1");
        Mockito.verify(paymentService, Mockito.times(1))
                .deletePayment("MWP1");
        Mockito.verify(bookingRepo, Mockito.times(1))
                .findById("MWP1");

        //Assertation
        Assertions.assertNotNull(bookingDto);
        Assertions.assertEquals("MWP1", bookingDto.bid());

    }



    @DisplayName("Delete Booking Fail_Case")
    @Test
    void Delete_BookedRoomFail_Case(){
     //Mocking
     Mockito.when(bookingRepo.findById(Mockito.anyString()))
             .thenReturn(Optional.ofNullable(null));

     BookingException exception = Assertions.assertThrows(BookingException.class,()->{
             bookingService.deleteBookings("MWP1");
             })  ;
     //Verification
     Mockito.verify(bookingRepo, Mockito.times(1))
             .findById("MWP1");
     //Assertion
     Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getHttpStatus());
     Assertions.assertEquals("No order present....", exception.getMessage());

    }


    @DisplayName("Get BookingByCID Success_Case")
    @Test
    void Get_RoomByCID_Success(){
        //Mocking
        Mockito.when(bookingRepo.findByConfirmationCode(Mockito.anyString()))
                .thenReturn(Optional.of(getMockBooking()));
        //Actual Method-call
        BookingDto bookingDto = bookingService.getRoomByCID("MWP1");
        //Verification
        Mockito.verify(bookingRepo, Mockito.times(1))
                .findByConfirmationCode("MWP1");
        //Assertion
        Assertions.assertNotNull(bookingDto);
        Assertions.assertEquals("MWP1", bookingDto.confirmation_code());
    }


    @DisplayName("Get BookingByCID Fail_Case")
    @Test
    void Get_RoomByCID_Fail(){
        //Mocking
        Mockito.when(bookingRepo.findByConfirmationCode(Mockito.anyString()))
                .thenReturn(Optional.ofNullable(null));

        BookingException exception = Assertions.assertThrows(BookingException.class, ()->{
            bookingService.getRoomByCID("MWP1");
        });
        //Verification
        Mockito.verify(bookingRepo, Mockito.times(1))
                .findByConfirmationCode("MWP1");
        //Assertion
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getHttpStatus());
        Assertions.assertEquals("no such booking of confirmation data is avaiable", exception.getMessage());
    }



}
//use anyValue in mocking
//and realValue in actual method call and verify

