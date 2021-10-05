package com.example.travelsjavaapi.service;

import java.util.ArrayList;

import com.example.travelsjavaapi.dao.UserDao;
import com.example.travelsjavaapi.model.User;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.selectUserByUsername(username);
        org.springframework.security.core.userdetails.User springUser = null;

        if (user == null) {
            throw new UsernameNotFoundException("User with email: " + username + " not found");
        } else {
            springUser = new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                    new ArrayList<>());
        }

        return springUser;
    }
}
