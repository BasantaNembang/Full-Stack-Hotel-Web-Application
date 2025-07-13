package com.user.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private final JavaMailSender javaMailSender;

    public MailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }


    public boolean sendOPTemail(String heading,String to,  String otp) {
        MimeMessagePreparator preparator = mimeMessage -> {

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            helper.setFrom("nembangbasanta66@gmail.com");
            helper.setSubject(heading);
            helper.setTo(to);
            helper.setText(otp);
        };
        try{
            javaMailSender.send(preparator);
            return true;
        } catch (Exception e) {
            System.out.println("Error  -> "+ e.getMessage() );
            return false;
        }
    }

}
