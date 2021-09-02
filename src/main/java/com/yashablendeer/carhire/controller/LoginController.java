package com.yashablendeer.carhire.controller;

import com.yashablendeer.carhire.config.SecurityUtil;
import com.yashablendeer.carhire.model.User;
import com.yashablendeer.carhire.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

/**
 * Controller for login page
 *
 * @author yaroslava
 * @version 1.0
 */

@Controller
@Log4j2
public class LoginController {

    @GetMapping(value={"/", "/login"})
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView();
        if (SecurityUtil.isAuthenticated()) {
            log.info("Logged user trying to visit login page");
            modelAndView.setViewName("redirect:insides/home");
            return modelAndView;
        }
        modelAndView.setViewName("login");
        return modelAndView;
    }

}
