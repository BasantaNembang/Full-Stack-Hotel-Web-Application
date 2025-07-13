package com.inventory.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {

    @Autowired
    private AvaiableRoomImpel avaiableRoomImpel;

    @KafkaListener(topics = "inventory-event", groupId = "inventory-service")
    public void deleteViaKafka(ConsumerRecord<String, com.booking.event.InventoryServiceEvent> record){
      var event = record.value();
        //boolean bool =  avaiableRoomImpel.deleteBookings(String.valueOf(event.getBid()));
        boolean bool =  avaiableRoomImpel.deleteBookings(event.getBid().toString());
        System.out.println(event.getSubject()+"  "+bool);
    }

}
