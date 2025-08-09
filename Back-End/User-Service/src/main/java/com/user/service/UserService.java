package com.user.service;

import com.user.dto.UserDto;

import com.user.entity.User;
import com.user.excption.ResourceNotFound;
import com.user.repo.UserRepo;
import com.user.security.JwtService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Random;
import java.util.UUID;

@Service
public class UserService implements  UserServiceInt{

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService service;

    @Autowired
    private MailService mailService;

    @Autowired
    private RedisService redisService;



    @Override
    @Transactional
    public String addUser(UserDto userDto) {

         User user = new User();
         user.setUid(UUID.randomUUID().toString().substring(0,5));
         user.setAddress(userDto.address());
         user.setEmail(userDto.email());
         //encrypt
         BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(10);

         user.setPassword(bCryptPasswordEncoder.encode(userDto.password()));
         user.setNumber(userDto.number());
         user.setFname(userDto.fname());
         user.setLname(userDto.lname());
         user.setUsername(userDto.username());
         user.setGender(userDto.gender());
         user.setRole("ROLE_USER");
         userRepo.save(user);

        return "success";
    }


    @Override
    public UserDto getUserByID(String uid) {
        User user  =  userRepo.findById(uid).orElseThrow(()->new ResourceNotFound());
        UserDto userDto = new UserDto(user.getUid(), user.getFname(), user.getLname(), user.getAddress(), user.getNumber(), user.getEmail(), user.getGender(), user.getUsername(), user.getPassword() );
        return userDto;
    }


    @Override
    public String loginUser(UserDto userDto) {
        Authentication manager = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDto.username(), userDto.password()));
        if(manager.isAuthenticated()){
            System.out.println("I am authenticated");
            return service.getJwtToken(userDto.username());
        }else {
            throw  new ResourceNotFound("User is not Logged_IN");
        }
    }


    @Override
    public UserDto deleteUser(String uid) {
        User user  =  userRepo.findById(uid).orElseThrow(()->new ResourceNotFound());
        userRepo.deleteById(uid);
        UserDto userDto = new UserDto(user.getUid(), user.getFname(), user.getLname(), user.getAddress(), user.getNumber(), user.getEmail(), user.getGender(), user.getUsername(), user.getPassword() );
        return userDto;
    }


    @Override
    public String sendOTP(String username) {

        //check if the given username is present in database or not if not throw exceptions
        User user = userRepo.findByUsername(username);
        if(user==null){
            throw new ResourceNotFound("UserName is not correct or not Sign-IN");
        }
        String otp = new DecimalFormat("000000").format(new Random().nextInt(999999));
        boolean flag  =  mailService.sendOPTemail("OPT to change Password", username, otp);
        if(flag){
            //save the OTP and username in redis for 5 mins
            redisService.saveOTP("otp", otp);
            redisService.saveUser("username", username);
            return "success";
        }else {
            throw new ResourceNotFound("Error while sending mail");
        }

    }

    @Override
    public String checkOTP(String otp) {
        String savedRedis = redisService.getOTPByKEY("otp");
        if(savedRedis.equals(otp)){
            return "success";
        }else{
            throw new ResourceNotFound("OTP not Matched");
        }
    }

    @Override
    @Transactional
    public String updatePassword(String password) {
        String savedRedis = redisService.getOTPByKEY("username");
        //System.out.println("saveRedis   " +  savedRedis);
        User user = userRepo.findByUsername(savedRedis);
        //System.out.println("________________");
        //System.out.println(user);
        if(user ==null){
            throw new ResourceNotFound("Retry again");
        }
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepo.save(user);
        return "success";
    }


}
