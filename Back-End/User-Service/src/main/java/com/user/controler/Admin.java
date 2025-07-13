package com.user.controler;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/admin")
public class Admin {

    @GetMapping("/base")
    public ResponseEntity<?> getPage(){
        return ResponseEntity.ok("Success");
    }


}
