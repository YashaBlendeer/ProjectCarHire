package com.yashablendeer.carhire.controller;

import com.yashablendeer.carhire.config.SecurityUtil;
import com.yashablendeer.carhire.model.Car;
import com.yashablendeer.carhire.model.User;
import com.yashablendeer.carhire.service.CarService;
import com.yashablendeer.carhire.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class PagesController {
    @Autowired
    private UserService userService;

    @Autowired
    private CarService carService;


    @GetMapping(value="/insides/home")
    public ModelAndView home(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        User user = userService.findUserByUserName(auth.getName());
        modelAndView.addObject("userName", "Welcome " + user.getUserName() + "/" + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");

        modelAndView.addObject("showUserNames", "Users' names: " + userService.findAllUsersByName());
        modelAndView.addObject("showUsers", userService.findAllUsers());
        modelAndView.setViewName("insides/home");
        return modelAndView;
    }

    @RequestMapping(value = "/ban_user", method = RequestMethod.GET)
    public ModelAndView handleBanUser(@RequestParam(name="personId")int personId) {
        ModelAndView modelAndView = new ModelAndView();
        userService.banHandler(personId);
        modelAndView.setViewName("redirect:insides/home");
        return modelAndView;
    }

    @RequestMapping(value = "/manager_handler", method = RequestMethod.GET)
    public ModelAndView managerHandler(@RequestParam(name="personId")int personId) {
        ModelAndView modelAndView = new ModelAndView();
        userService.managerUpgrade(personId);
        modelAndView.setViewName("redirect:insides/home");
        return modelAndView;
    }

    @GetMapping(value="/mainPage")
    public ModelAndView mainPage(){
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("showCars", carService.findAllCars());
        modelAndView.setViewName("mainPage");
        return modelAndView;

    }

    @GetMapping(value="/addCar")
    public ModelAndView addCar(){
        ModelAndView modelAndView = new ModelAndView();
        Car car = new Car();
        modelAndView.addObject("car", car);
        modelAndView.setViewName("carPage");
        return modelAndView;
    }

    @PostMapping(value = "/addCar")
    public ModelAndView createNewCar(@Valid Car car, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("carPage");
        } else {
            carService.saveCar(car);
            modelAndView.addObject("successMessage", "Car has been registered successfully");
            modelAndView.addObject("car", new Car());
            modelAndView.setViewName("carPage");

        }
        return modelAndView;
    }

//    @RequestMapping(value = "/updateCar", method = RequestMethod.GET)
//    public ModelAndView updateCar(@RequestParam(name="carId")int carId) {
//        ModelAndView modelAndView = new ModelAndView();
//        userService.banHandler(carId);
//        modelAndView.setViewName("redirect:insides/home");
//        return modelAndView;
//    }

    @RequestMapping(value = "mainPage/deleteCar", method = RequestMethod.GET)
    public ModelAndView updateCar(@RequestParam(name="carId")int carId) {
        ModelAndView modelAndView = new ModelAndView();
        System.out.println("=======================");
        System.out.println("inside");
        System.out.println("=======================");
        carService.deleteCarById(carId);
        //TODO add success message
        modelAndView.setViewName("redirect:/mainPage");
        return modelAndView;
    }

//    @RequestMapping(value = "/carUpdate", method = RequestMethod.POST)
//    public ModelAndView carUpdateHandler(@Valid Car car, BindingResult bindingResult) {
//        ModelAndView modelAndView = new ModelAndView();
//        if (bindingResult.hasErrors()) {
//            modelAndView.setViewName("mainPage");
//        } else {
//            carService.updateCar(car.getId(), car);
////            modelAndView.addObject("successMessage", "Car has been updated successfully");
//            modelAndView.addObject("car", new Car());
//            modelAndView.setViewName("mainPage");
//
//        }
//        return modelAndView;
//    }
}
