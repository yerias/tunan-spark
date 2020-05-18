package com.tunan.web.controller;

import com.tunan.web.dao.UserRepository;
import com.tunan.web.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/save")
    public User save(User user) {
        return userRepository.save(user);
    }

    @GetMapping("/find/{id}")
    public User find(@PathVariable("id") Integer id) {
        return userRepository.findById(id).get();
    }

    @GetMapping("/del/{id}")
    public void del(@PathVariable("id") Integer id) {
        userRepository.deleteById(id);
    }

    @GetMapping("/delAll")
    public void delAll() {
        userRepository.deleteAll();
    }



}
