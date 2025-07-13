package com.booking.controller;

import com.booking.BookingConfig;
import com.booking.dto.BookingDto;
import com.booking.dto.BookingStatus;
import com.booking.model.Booking;
import com.booking.repository.BookingRepo;
import com.booking.service.BookingImpel;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

import static java.nio.charset.Charset.defaultCharset;
import static org.junit.Assert.*;
import static org.springframework.util.StreamUtils.copyToString;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableConfigurationProperties
@AutoConfigureMockMvc
@ContextConfiguration(classes = {BookingConfig.class})
public class BookingControllerTest {


 @Autowired
 private BookingImpel bookingImpel;

 @Autowired
 private MockMvc mockMvc;

 @Autowired
 private BookingRepo bookingRepo;


 @RegisterExtension
 static WireMockExtension mockExtension =
         WireMockExtension.newInstance()
                 .options(WireMockConfiguration
                         .wireMockConfig()
                         .port(8080))
                 .build();

 private ObjectMapper objectMapper = new ObjectMapper()
         .findAndRegisterModules()
         .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
         .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


 //Test-Container for mongoDB  and kafka-schema-registry;
 private static final Network network = Network.newNetwork();

 @Container
 static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest").withExposedPorts(27017);

 @Container
 static KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.5.0")
         .asCompatibleSubstituteFor("confluentinc/cp-kafka"))
         .withNetwork(network)
         .withNetworkAliases("kafka")
         .withExposedPorts(9092,9093, 9091);

 @Container
 static GenericContainer<?> schemaRegistry = new GenericContainer<>(
         DockerImageName.parse("confluentinc/cp-schema-registry:7.5.0"))
         .withNetwork(network)
         .withExposedPorts(8081)
         .withEnv("SCHEMA_REGISTRY_HOST_NAME", "schema-registry")
         .withEnv("SCHEMA_REGISTRY_LISTENERS", "http://0.0.0.0:8081")
         .withEnv("SCHEMA_REGISTRY_KAFKASTORE_BOOTSTRAP_SERVERS",
                 "PLAINTEXT://" + kafkaContainer.getNetworkAliases().get(0) + ":9092")
         .waitingFor(Wait.forHttp("/subjects").forStatusCode(200));;


 @DynamicPropertySource
 static void containersProperties(DynamicPropertyRegistry registry) {
  registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);

  //kafka
  registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
  registry.add("spring.kafka.producer.properties.schema.registry.url", ()->
          "http://" + schemaRegistry.getHost() + ":" + schemaRegistry.getMappedPort(8081));
 }


 @BeforeAll
 static void beforeALL() {
  mongoDBContainer.start();
  kafkaContainer.start();
  schemaRegistry.start();

}

 @AfterAll
 static void afterALL(){
  mongoDBContainer.stop();
  kafkaContainer.stop();
  schemaRegistry.stop();
 }



 @BeforeEach
 void SetUP_BookedRoom() throws IOException {
   getHotelDTO();

   getISBooked();
   getDoPayment();
   getBookedRoom();

 }


 private void getBookedRoom() throws IOException {
  mockExtension.stubFor(WireMock.post(WireMock.urlMatching("/room-check/booked"))
          .willReturn(WireMock.aResponse()
                  .withStatus(HttpStatus.OK.value())
                  .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                  .withBody(copyToString(
                  BookingControllerTest.class.getClassLoader()
                          .getResourceAsStream("mock/GetAvaiableRoomDto.json"),
                  defaultCharset()))));
 }


 private void getDoPayment() throws IOException {
  mockExtension.stubFor(WireMock.post(WireMock.urlMatching("/payment/do"))
          .willReturn(WireMock.aResponse()
                  .withStatus(HttpStatus.OK.value())
          .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                  .withBody(copyToString(
                          BookingControllerTest.class.getClassLoader()
                                  .getResourceAsStream("mock/GetPaymentDto.json"),
                          defaultCharset()))));
 }

 private void getISBooked() {
   mockExtension.stubFor(WireMock.post(WireMock.urlEqualTo("/room-check/is-booked"))
           .willReturn(WireMock.aResponse()
                   .withStatus(HttpStatus.OK.value())
                   .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                   .withBody("false")
           ));
 }


 private  void getHotelDTO() throws IOException {
  mockExtension.stubFor(WireMock.get(WireMock.urlMatching("/hotel/get-room/.*"))
          .willReturn(WireMock.aResponse()
                  .withStatus(HttpStatus.OK.value())
                  .withHeader("Content-Type", "application/json")
                          .withBody(copyToString(
                                  BookingControllerTest.class.getClassLoader()
                                          .getResourceAsStream("mock/GetHotelDto.json"),
                                  defaultCharset())))
                          );
 }


 private BookingDto bookingDto(){
  return new BookingDto(
          null,
          "Basanta Nembang",
          "basanta@.com",
          LocalDate.of(2025, 7, 10),
          LocalDate.of(2025, 7, 12),
          2,
          1,
          1500,
          3,
          "23RRT",
          "Deluxe",
          null,
          null
  );
 }

 private BookingDto bookingDto_Fail(){
  return new BookingDto(
          null,
          "Basanta Nembang",
          "basanta@.com",
          LocalDate.of(2025, 7, 15),
          LocalDate.of(2025, 7, 18),
          2,
          1,
          1500,
          3,
          null,
          "Deluxe",
          null,
          null
  );
 }


 private com.booking.event.BookedRoomEvent getBookedRoomEventViaTESTING(String topic) throws InterruptedException {
  Map<String, Object> consumerProps = new HashMap<>();
  consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers());
  consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
  consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "notification-service-"  + UUID.randomUUID());
  consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
  consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
  consumerProps.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://" + schemaRegistry.getHost() + ":" + schemaRegistry.getMappedPort(8081));
  consumerProps.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);

 KafkaConsumer<String, com.booking.event.BookedRoomEvent> consumer = new KafkaConsumer<>(consumerProps);
 try {
  consumer.subscribe(Collections.singleton(topic));
  Thread.sleep(1200);
  ConsumerRecords<String, com.booking.event.BookedRoomEvent> poll = consumer.poll(Duration.ofSeconds(62));
  assertFalse("No Kafka events received!", poll.isEmpty());
  return poll.iterator().next().value();
 }
 finally {
  consumer.close();
 }
 }


 private com.booking.event.BookedRoomEvent getBookedRoomEventViaTESTING_FAIL(String topic) throws InterruptedException {
  Map<String, Object> consumerProps = new HashMap<>();
  consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers());
  consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
  consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "notification-service-" + UUID.randomUUID());
  consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
  consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
  consumerProps.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://" + schemaRegistry.getHost() + ":" + schemaRegistry.getMappedPort(8081));
  consumerProps.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);

  KafkaConsumer<String, com.booking.event.BookedRoomEvent> consumer = new KafkaConsumer<>(consumerProps);
  try {
   consumer.subscribe(Collections.singleton(topic));
   Thread.sleep(1200);
   ConsumerRecords<String, com.booking.event.BookedRoomEvent> poll = consumer.poll(Duration.ofSeconds(62));
   assertTrue(poll.isEmpty());
   return null;
  } finally {
   consumer.close();
  }
 }



 private void getISBooked_Fail() {
  mockExtension.stubFor(WireMock.post(WireMock.urlEqualTo("/room-check/is-booked"))
          .willReturn(WireMock.aResponse()
                  .withStatus(HttpStatus.OK.value())
                  .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                  .withBody("true")
          ));
 }

 private List<BookingDto> getALLBookings_SAVE() {
 BookingDto dto = new BookingDto("BIDIIO",
          "Basanta Nembang",
          "basanta@.com",
          LocalDate.of(2025, 7, 15),
          LocalDate.of(2025, 7, 18),
          2,
          1,
          1500,
          3,
          "ROOM101",
          "Deluxe",
          "CONF1234",
          BookingStatus.SUCCESS
 );
  BookingDto bookingDto = bookingImpel.bookRoom(dto);
  return Collections.singletonList(dto);
 }

 private Booking saveBookingForConNumber() {
  Booking booking = new Booking("JIKPLO",
          "Basanta Nembang",
          "basanta1@.com",
          LocalDate.of(2025, 8, 15),
          LocalDate.of(2025, 8, 18),
          2,
          1,
          1500,
          3,
          "ROOM102",
          "Deluxe",
          "CONF12345",
          BookingStatus.SUCCESS);
  bookingRepo.save(booking);
  return booking;
 }


 private BookingDto getALLBooking_SAVE() {
   Booking booking =new Booking(
          "BIDIIO",
          "Basanta Nembang",
          "basanta@.com",
          LocalDate.of(2025, 7, 15),
          LocalDate.of(2025, 7, 18),
          2,
          1,
          1500,
          3,
          "ROOM101",
          "Deluxe",
          "CONF1234",
          BookingStatus.SUCCESS
  );
  bookingRepo.save(booking);
  return new BookingDto(booking.getBid(),
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
          booking.getStatus());
 }

 private void getBoolViaDeletePayment(String id) {
  mockExtension.stubFor(WireMock.delete(WireMock.urlEqualTo("/payment/delete/"+id))
          .willReturn(WireMock.aResponse()
                  .withStatus(HttpStatus.OK.value())
                  .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                  .withBody("true")
          ));
 }

 private com.booking.event.InventoryServiceEvent deleteInventoryServiceKafka(String topic) throws InterruptedException {
  Map<String, Object> consumerProps = new HashMap<>();
  consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers());
  consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
  consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "Inventory-service-"  + UUID.randomUUID());
  consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
  consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
  consumerProps.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://" + schemaRegistry.getHost() + ":" + schemaRegistry.getMappedPort(8081));
  consumerProps.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);

  KafkaConsumer<String, com.booking.event.InventoryServiceEvent> consumer = new KafkaConsumer<>(consumerProps);
  ConsumerRecords<String, com.booking.event.InventoryServiceEvent> poll=null;
  try {
   consumer.subscribe(Collections.singleton(topic));
   System.out.println("Subscribed to topic: " + topic);
   Thread.sleep(5000);
   poll = consumer.poll(Duration.ofSeconds(62));
   System.out.println("_______________________");
   System.out.println(poll);
   System.out.println(poll.isEmpty());
   assertFalse("No Kafka events received!", poll.isEmpty());   //if is empty then exception occur

  } catch (Exception e) {
   System.out.println("_________________________________");
   System.out.println("ATTENTION PLEASE............");
   System.out.println(e.getMessage());
  }
  finally {
   consumer.close();
  }
  return poll.iterator().next().value();
 }



 @DisplayName("Booked Room Success_Case")
 @Test
 void BookedRoomSuccessCase() throws Exception {
  BookingDto booking = bookingDto();

  MvcResult mvcResult = (MvcResult) mockMvc.perform(MockMvcRequestBuilders.post("/bookings/place")
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .content(objectMapper.writeValueAsString(booking)))
          .andExpect(MockMvcResultMatchers.status().isOk())
          .andReturn();

  //test kafka
  com.booking.event.BookedRoomEvent bookedRoomEvent = getBookedRoomEventViaTESTING("booked-room");
  Assert.assertEquals(bookedRoomEvent.getGuest().toString(), bookingDto().email());
  Assert.assertNotNull(bookedRoomEvent.getConformationNumber());


  String response = mvcResult.getResponse().getContentAsString();

  try {
   BookingDto bookingDto = objectMapper.readValue(response, BookingDto.class);
   //System.out.println("The data  "+bookingDto);
   //ASSERTATION
   Assertions.assertEquals(bookingDto().room_price(), booking.room_price());
   Assertions.assertEquals(bookingDto.room_id(), booking.room_id());
   Assertions.assertNotNull(bookingDto.bid());
   Assertions.assertNotNull(bookingDto.confirmation_code());
  } catch (Exception e) {
   System.out.println("The Error  "+e.getMessage());
  }
 }



 @DisplayName("Booked Room Fail_Case_Room_Booked_Allready")
 @Test
 void BookedRoomFailCase() throws Exception {
  BookingDto booking = bookingDto_Fail();

  //
  getHotelDTO();
  getISBooked_Fail();

  MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/bookings/place")
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .content(objectMapper.writeValueAsString(booking)))
          .andExpect(MockMvcResultMatchers.status().isInternalServerError())
          .andReturn();

  //no kafka MSG Send if error occur
  com.booking.event.BookedRoomEvent bookedRoomEvent = getBookedRoomEventViaTESTING_FAIL("booked-room");


  String response = mvcResult.getResponse().getContentAsString();

  Map map = objectMapper.readValue(response, Map.class);
  //System.out.println(map.get("msg"));
  //Assertation
  Assertions.assertEquals("Room has been booked already...", map.get("msg"));
 }




 @DisplayName("Get_All_Bookings")
 @Test
 void Get_All_Bookings() throws Exception {
  //first save and then get
  List<BookingDto> bookingDtos = getALLBookings_SAVE();

  MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/bookings/get-all")
                  .contentType(MediaType.APPLICATION_JSON_VALUE))
          .andExpect(MockMvcResultMatchers.status().isOk())
          .andReturn();

  String response = mvcResult.getResponse().getContentAsString();
  System.out.println("_________");
  System.out.println(response);

  }


 @DisplayName("Get_All_Booking_By_ID_SuccessCASE")
 @Test
 void Get_All_Bookings_By_BID() throws Exception {

  //save the data
  BookingDto bookings = getALLBooking_SAVE();

  MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/bookings/get/BIDIIO")
                  .contentType(MediaType.APPLICATION_JSON_VALUE))
          .andExpect(MockMvcResultMatchers.status().isOk())
          .andReturn();

  String response = mvcResult.getResponse().getContentAsString();
  BookingDto bookingDto = objectMapper.readValue(response, BookingDto.class);
  //Assert
  Assertions.assertEquals("BIDIIO", bookingDto.bid());

 }


 @DisplayName("Get_All_Booking_By_ID_FailCASE")
 @Test
 void Get_All_Bookings_By_BID_FailCASE() throws Exception {

  MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/bookings/get/IJJK")
                  .contentType(MediaType.APPLICATION_JSON_VALUE))
          .andExpect(MockMvcResultMatchers.status().isInternalServerError())
          .andReturn();

  String response = mvcResult.getResponse().getContentAsString();
  Map map = objectMapper.readValue(response, Map.class);
  //Assert
  Assertions.assertEquals("No such Bookings is avaiable....", map.get("msg"));

 }



 @DisplayName("Delete_By_ID_SuccessCASE")
 @Test
 void Delete_By_ID_SuccessCASE() throws Exception {

  //find by ID cuse BIDIIO is already save in top
  Booking booking = bookingRepo.findById("BIDIIO").get();
  //Delete payment-service
  getBoolViaDeletePayment("BIDIIO");

  //now detete
  MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/bookings/delete/BIDIIO")
                  .contentType(MediaType.APPLICATION_JSON_VALUE))
          .andExpect(MockMvcResultMatchers.status().isOk())
          .andReturn();

  //delete via Kafka in inventory service
  com.booking.event.InventoryServiceEvent event = deleteInventoryServiceKafka("inventory-event");
  //checking if correct msg is send to kafka or not
  Assertions.assertEquals(event.getBid().toString(), "BIDIIO");

  String response = mvcResult.getResponse().getContentAsString();
  Booking book = objectMapper.readValue(response, Booking.class);
  Assertions.assertEquals(book.getBid(), "BIDIIO");

}



 @DisplayName("Delete_By_ID_FailCASE")
 @Test
 void Delete_By_ID_failCASE() throws Exception {

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/bookings/delete/BBBUUID")
                  .contentType(MediaType.APPLICATION_JSON_VALUE))
          .andExpect(MockMvcResultMatchers.status().isInternalServerError())
          .andReturn();

 }



@DisplayName("Get_Booking_By_ConfirmationNumber_SuccessCase")
@Test
 void Get_Booking_By_ConfirmationNumber_SuccessCase() throws Exception {

  Booking booking = saveBookingForConNumber();

  MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/bookings/get/confirm-number/CONF12345")
          .contentType(MediaType.APPLICATION_JSON_VALUE))
          .andExpect(MockMvcResultMatchers.status().isOk())
          .andReturn();

 String response = mvcResult.getResponse().getContentAsString();
 BookingDto bookingDto = objectMapper.readValue(response, BookingDto.class);

 Assertions.assertEquals(bookingDto.confirmation_code(), "CONF12345");



 }


 @DisplayName("Get_Booking_By_ConfirmationNumber_FailCase")
 @Test
 void Get_Booking_By_ConfirmationNumber_FailCase() throws Exception {

  Booking booking = saveBookingForConNumber();

  MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/bookings/get/confirm-number/IIPPLIJM")
                  .contentType(MediaType.APPLICATION_JSON_VALUE))
          .andExpect(MockMvcResultMatchers.status().isInternalServerError())
          .andReturn();
 }


}

