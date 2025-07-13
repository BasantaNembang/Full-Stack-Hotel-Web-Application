package com.notification.mail;


import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import com.booking.event.BookedRoomEvent;

@Service
public class SendMail {


 private final JavaMailSender javaMailSender;

    public SendMail(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }


    @KafkaListener(topics = "booked-room")
    public void ListenKafka(BookedRoomEvent event){
        MimeMessagePreparator preparator = mimeMessage -> {

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            helper.setFrom("nembangbasanta66@gmail.com");
            helper.setTo(event.getGuest().toString());
            helper.setSubject(event.getSubject().toString());
            helper.setText("Your Room Has Been Booked Successfully "+ event.getConformationNumber());

        };
        try{
            javaMailSender.send(preparator);
            System.out.println("Mail has been send succesfully "+event);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }



    }


}
