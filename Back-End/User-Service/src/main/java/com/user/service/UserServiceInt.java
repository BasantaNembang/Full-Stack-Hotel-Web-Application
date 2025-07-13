package com.user.service;

import com.user.dto.UserDto;

public interface UserServiceInt {

    String addUser(UserDto userDto);

    UserDto getUserByID(String uid);

    String loginUser(UserDto userDto);

    UserDto deleteUser(String uid);


    //forgot password
    String sendOTP(String username);

    String checkOTP(String otp);

    String updatePassword(String password);
}
