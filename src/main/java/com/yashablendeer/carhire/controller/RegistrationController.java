package com.yashablendeer.carhire.controller;

import com.yashablendeer.carhire.config.SecurityUtil;
import com.yashablendeer.carhire.model.User;
import com.yashablendeer.carhire.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

/**
 * Controller for registration page
 *
 * @author yaroslava
 * @version 1.0
 */

@Controller
@Log4j2
public class RegistrationController {

    @Autowired
    private UserService userService;

    @GetMapping(value="/registration")
    public ModelAndView registration(){
        ModelAndView modelAndView = new ModelAndView();

        if (SecurityUtil.isAuthenticated()) {
            log.trace("Logged user trying to visit registration page");
            modelAndView.setViewName("redirect:insides/home");
            return modelAndView;
        }
        modelAndView.addObject("user", new User());

        modelAndView.setViewName("registration");
        return modelAndView;
    }

    @PostMapping(value = "/registration")
    public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        User userExists = userService.findUserByUserName(user.getUserName());
        if (userExists != null) {
            bindingResult
                    .rejectValue("userName", "error.user",
                            "There is already a user registered with the user name provided");
            log.info("Trying to register the existing user: {}", user);
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("registration");
            log.warn("Error occured during registration");
        } else {
            userService.saveUser(user);
            modelAndView.addObject("successMessage", "User has been registered successfully");
            modelAndView.addObject("user", new User());
            modelAndView.setViewName("registration");
            log.info("User registered successfully: {}", user);
        }
        return modelAndView;
    }

}
