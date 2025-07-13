package com.user.controler;

import com.user.dto.UserDto;
import com.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService service;


    @PostMapping("/sign-up")
    public ResponseEntity<String> addUser(@RequestBody UserDto userDto){
        return ResponseEntity.ok((service.addUser(userDto)));
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserDto userDto){
        return ResponseEntity.ok((service.loginUser(userDto)));
    }

    @GetMapping("/get/{uid}")
    public UserDto getUser(@PathVariable("uid") String uid){
        return ResponseEntity.ok((service.getUserByID(uid))).getBody();
    }

    @DeleteMapping("/delete/{uid}")
    public UserDto deleteUser(@PathVariable("uid") String uid){
        return ResponseEntity.ok((service.deleteUser(uid))).getBody();
    }

    //update password
    @PostMapping("/send-otp/{username}")
    public String sendOTP(@PathVariable("username") String username){
       return ResponseEntity.ok((service.sendOTP(username))).getBody();
    }

    @PostMapping("/check-otp/{otp}")
    public String checkOTP(@PathVariable("otp") String otp){
        return ResponseEntity.ok((service.checkOTP(otp))).getBody();
    }

    @PutMapping("/update-password/{password}")
    public String updatePassword(@PathVariable("password") String password){
        return ResponseEntity.ok((service.updatePassword(password))).getBody();
    }

}
