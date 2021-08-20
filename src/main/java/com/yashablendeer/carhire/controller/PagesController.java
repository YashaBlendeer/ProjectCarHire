package com.yashablendeer.carhire.controller;

import com.yashablendeer.carhire.config.SecurityUtil;
import com.yashablendeer.carhire.model.Car;
import com.yashablendeer.carhire.model.Order;
import com.yashablendeer.carhire.model.Status;
import com.yashablendeer.carhire.model.User;
import com.yashablendeer.carhire.service.CarService;
import com.yashablendeer.carhire.service.OrderService;
import com.yashablendeer.carhire.service.UserService;
import com.yashablendeer.carhire.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class PagesController {
    @Autowired
    private UserService userService;

    @Autowired
    private CarService carService;

    @Autowired
    private OrderService orderService;


    @GetMapping(value="/insides/home")
    public ModelAndView home(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        User user = userService.findUserByUserName(auth.getName());
        modelAndView.addObject("userName", "Welcome " + user.getUserName() + "/" + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        modelAndView.addObject("currentUser", user);
        modelAndView.addObject("showUserNames", "Users' names: " + userService.findAllUsersByName());
        modelAndView.addObject("showUsers", userService.findAllUsers());
        modelAndView.addObject("ordersList", orderService.findAllOrders());
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

    @GetMapping(value="/carPage")
    public ModelAndView addCar(){
        ModelAndView modelAndView = new ModelAndView();
        Car car = new Car();
        modelAndView.addObject("car", car);
        modelAndView.setViewName("carPage");
        return modelAndView;
    }

    @PostMapping(value = "/carPage")
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

    @RequestMapping(value = "mainPage/deleteCar", method = RequestMethod.GET)
    public ModelAndView deleteCar(@RequestParam(name="carId")int carId) {
        ModelAndView modelAndView = new ModelAndView();
        carService.deleteCarById(carId);
        //TODO add success message
        modelAndView.setViewName("redirect:/mainPage");
        return modelAndView;
    }

    @RequestMapping(value = "carUpdatePage/{id}", method = RequestMethod.GET)
    public ModelAndView editCar(@PathVariable(name = "id") int id) {
        ModelAndView modelAndView = new ModelAndView();
        Car car = carService.findCarById(id);
        modelAndView.addObject("car", car);
        modelAndView.setViewName("carUpdatePage");
        return modelAndView;
    }

    @RequestMapping(path = "carUpdatePage/{id}", method = RequestMethod.POST)
    public ModelAndView carUpdateHandler(@Valid Car car, BindingResult bindingResult, @PathVariable("id") Integer id,
            RedirectAttributes redirectAttrs) {
        ModelAndView modelAndView = new ModelAndView();
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("carUpdatePage");
        } else {
            carService.updateCar(car.getId(), car);
            redirectAttrs.addAttribute("id", id).addFlashAttribute("successMessage", "Car has been updated successfully");
            modelAndView.addObject("car", new Car());
            modelAndView.setViewName("redirect:{id}");

        }
        return modelAndView;
    }

    @RequestMapping(value = "carOrderPage/{id}", method = RequestMethod.GET)
    public ModelAndView orderCar(@PathVariable(name = "id") int id) {
        ModelAndView modelAndView = new ModelAndView();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        modelAndView.addObject("userName", user.getName() + " " + user.getLastName());

        //time limiter
        modelAndView.addObject("now", LocalDateTime.now().format(DateUtil.DTF));
        modelAndView.addObject("then", LocalDateTime.now().plusHours(1).format(DateUtil.DTF));

        Car car = carService.findCarById(id);
        modelAndView.addObject("currentCar", car);
        Order order =  new Order();

        modelAndView.addObject("order",order);
        modelAndView.setViewName("carOrderPage");
        return modelAndView;
    }

    @RequestMapping(path = "carOrderPage/{id}", method = RequestMethod.POST)
    public ModelAndView carOrderHandler(@Valid Order order, BindingResult bindingResult,
                                        @PathVariable("id") Integer carId,
                                        RedirectAttributes redirectAttrs) {
        ModelAndView modelAndView = new ModelAndView();
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("carOrderPage");
        } else {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = userService.findUserByUserName(auth.getName());
            Car car = carService.findCarById(carId);

            orderService.saveBuilder(order, car, user);
            redirectAttrs.addAttribute("id", carId).addFlashAttribute("successMessage", "Order was sent for " +
                    "processing successfully");
            modelAndView.addObject("car", new Car());
            modelAndView.setViewName("redirect:{id}");

        }
        return modelAndView;
    }

    @RequestMapping(value = "/cancelOrder", method = RequestMethod.GET)
    public ModelAndView cancelOrderHandler(@RequestParam(name="orderId")int orderId) {
        ModelAndView modelAndView = new ModelAndView();
        orderService.deleteOrderById(orderId);
        modelAndView.setViewName("redirect:insides/home");
        return modelAndView;
    }

    @RequestMapping(value = "/acceptOrder", method = RequestMethod.GET)
    public ModelAndView acceptOrderHandler(@RequestParam(name="orderId")int orderId) {
        ModelAndView modelAndView = new ModelAndView();
        //TODO move from controller
        Order order = orderService.findOrderById(orderId);
        order.setStatus(Status.ACCEPTED);
        orderService.save(order);
        modelAndView.setViewName("redirect:insides/home");
        return modelAndView;
    }

    @RequestMapping(value = "/rejectOrder", method = RequestMethod.GET)
    public ModelAndView rejectOrderHandler(@RequestParam(name="orderId")int orderId) {
        ModelAndView modelAndView = new ModelAndView();
        //TODO move from controller
        Order order = orderService.findOrderById(orderId);
        order.setStatus(Status.REJECTED);
        orderService.save(order);

        modelAndView.addObject("rejectedOrder", new Order());
        modelAndView.setViewName("redirect:insides/home");
        return modelAndView;
    }

    @RequestMapping(value = "/payOrder", method = RequestMethod.GET)
    public ModelAndView payOrderHandler(@RequestParam(name="orderId")int orderId) {
        ModelAndView modelAndView = new ModelAndView();
        //TODO move from controller
        Order order = orderService.findOrderById(orderId);
        order.setPayStatus(Status.PAYED);
        orderService.save(order);
        modelAndView.setViewName("redirect:insides/home");
        return modelAndView;
    }

    @RequestMapping(value = "/finishOrder", method = RequestMethod.GET)
    public ModelAndView finishOrderHandler(@RequestParam(name="orderId")int orderId) {
        ModelAndView modelAndView = new ModelAndView();
        //TODO move from controller
        //TODO add price for car repair
        Order order = orderService.findOrderById(orderId);
        order.setStatus(Status.FINISHED);
        orderService.save(order);
        modelAndView.setViewName("redirect:insides/home");
        return modelAndView;
    }


    @RequestMapping(value = "carReturnPage/{id}", method = RequestMethod.GET)
    public ModelAndView returnCar(@PathVariable(name = "id") int id) {
        ModelAndView modelAndView = new ModelAndView();

        Order currentOrder = orderService.findOrderById(id);
        modelAndView.addObject("currentOrder", currentOrder);

        modelAndView.setViewName("carReturnPage");
        return modelAndView;
    }


}
