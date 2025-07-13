package com.user.helper;

import com.user.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserPrinciple implements UserDetails {


    private User user;

    public UserPrinciple(User user) {
        this.user=user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(user.getRole()));
    }

    @Override
    public String getPassword() {
        System.out.println("getPass  "+user.getPassword());
        return user.getPassword();
    }


    @Override
    public String getUsername() {
        System.out.println("getUser : "+user.getUsername());
        return user.getUsername();
    }



}