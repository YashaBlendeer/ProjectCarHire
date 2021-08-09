package com.yashablendeer.carhire.controller;

import com.yashablendeer.carhire.model.User;
import com.yashablendeer.carhire.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PagesController {
    @Autowired
    private UserService userService;


    @GetMapping(value="/insides/home")
    public ModelAndView home(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        User user = userService.findUserByUserName(auth.getName());
        modelAndView.addObject("userName", "Welcome " + user.getUserName() + "/" + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");

        //        modelAndView.addObject("adminMessage","Content Available Only for Users with Admin Role");

        modelAndView.addObject("showUserNames", "Users' names: " + userService.findAllUsersByName());
        modelAndView.addObject("showUsers", userService.findAllUsers());
        modelAndView.setViewName("insides/home");
        return modelAndView;
    }
}
