package com.willy.clouldcustomerdao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.willy.clouldcustomerdao.listener.AppListener;
import com.willy.clouldcustomerdao.repository.User;
import com.willy.clouldcustomerdao.repository.UserRepository;

/**
 * @author EdisonZhou
 */
@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "findUserInfoByID/{id}")
    public User findUserInfoByID(@PathVariable Long id){
    	System.out.println("由port : "+AppListener.getPort()+"\t的Service為您提供服務");
        User result = userRepository.findById(id).orElse(new User());
        return result;
    }
}
