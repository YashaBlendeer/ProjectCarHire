package com.yashablendeer.carhire.controller;

import com.yashablendeer.carhire.model.*;
import com.yashablendeer.carhire.service.CarService;
import com.yashablendeer.carhire.service.OrderService;
import com.yashablendeer.carhire.service.RepairService;
import com.yashablendeer.carhire.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.time.LocalDateTime;

/**
 * Controller for work with orders
 *
 * @author yaroslava
 * @version 1.0
 */
@Log4j2
@Controller
public class OrderController {

    @Autowired
    private UserService userService;

    @Autowired
    private CarService carService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RepairService repairService;

    @RequestMapping(value = "carOrderPage/{id}", method = RequestMethod.GET)
    public ModelAndView orderCar(@PathVariable(name = "id") int id) {
        ModelAndView modelAndView = new ModelAndView();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        modelAndView.addObject("userName", user.getName() + " " + user.getLastName());

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

        boolean dateNotAvailable = orderService.checkDateAvailability(carService.findCarById(carId), order.getStartTime(), order.getEndTime());

        redirectAttrs.addAttribute("id", carId);
        if (dateNotAvailable) {
            bindingResult
                    .rejectValue("startTime", "error.order",
                            "This date is not available");
            redirectAttrs.addFlashAttribute("dateNotAvailableMessage", "This date is not available");
        }

        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("redirect:{id}");
            log.warn("Error occured during ordering: {}", order);
        } else {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            User user = userService.findUserByUserName(auth.getName());
            Car car = carService.findCarById(carId);

            orderService.saveBuilder(order, car, user);
            redirectAttrs.addAttribute("id", carId).addFlashAttribute("successMessage",
                                                    "Order was sent for processing successfully");
            modelAndView.addObject("car", new Car());
            log.info("Order was sent: {}", order);

            modelAndView.setViewName("redirect:{id}");

        }
        return modelAndView;
    }

    @RequestMapping(value = "/cancelOrder", method = RequestMethod.GET)
    public ModelAndView cancelOrderHandler(@RequestParam(name="orderId")int orderId,
                                           @RequestParam(required=false, name = "currentPage") final String currentPage) {
        ModelAndView modelAndView = new ModelAndView();
        orderService.deleteOrderById(orderId);
        modelAndView.setViewName("redirect:insides/allOrders/page/" + currentPage);
        return modelAndView;
    }

    @RequestMapping(value = "/acceptOrder", method = RequestMethod.GET)
    public ModelAndView acceptOrderHandler(@RequestParam(name="orderId")int orderId,
                                           @RequestParam(required=false, name = "currentPage") final String currentPage) {
        ModelAndView modelAndView = new ModelAndView();
        orderService.acceptOrder(orderId);
        modelAndView.setViewName("redirect:insides/allOrders/page/" + currentPage);
        return modelAndView;
    }

    @RequestMapping(value = "/payOrder", method = RequestMethod.GET)
    public ModelAndView payOrderHandler(@RequestParam(name="orderId")int orderId,
                                        @RequestParam(required=false, name = "currentPage") final String currentPage) {
        ModelAndView modelAndView = new ModelAndView();
        orderService.payOrder(orderId);
        modelAndView.setViewName("redirect:insides/allOrders/page/" + currentPage);
        return modelAndView;
    }
    @RequestMapping(value = "/payRepair", method = RequestMethod.GET)
    public ModelAndView payRepairHandler(@RequestParam(name="orderId")int orderId,
                                         @RequestParam(required=false, name = "currentPage") final String currentPage) {
        ModelAndView modelAndView = new ModelAndView();
        repairService.payRepair(orderId);
        modelAndView.setViewName("redirect:insides/allOrders/page/" + currentPage);
        return modelAndView;
    }


    @RequestMapping(value = "/finishOrder", method = RequestMethod.GET)
    public ModelAndView finishOrderHandler(@RequestParam(name="orderId")int orderId,
                                           @RequestParam(required=false, name = "currentPage") final String currentPage) {
        ModelAndView modelAndView = new ModelAndView();
        orderService.finishOrder(orderId);
        modelAndView.setViewName("redirect:insides/allOrders/page/" + currentPage);
        return modelAndView;
    }


    @RequestMapping(value = "carRepairPage/{id}", method = RequestMethod.GET)
    public ModelAndView repairCar(@PathVariable(name = "id") int id) {
        ModelAndView modelAndView = new ModelAndView();

        Order currentOrder = orderService.findOrderById(id);
        Car currentCar = carService.findCarById(currentOrder.getCar().getId());
        Repair repair = new Repair();
        modelAndView.addObject("currentOrder", currentOrder);
        modelAndView.addObject("currentCar", currentCar);
        modelAndView.addObject("repair", repair);

        modelAndView.setViewName("carRepairPage");
        return modelAndView;
    }

    @RequestMapping(path = "carRepairPage/{id}", method = RequestMethod.POST)
    public ModelAndView carRepairHandler(@Valid Repair repair, BindingResult bindingResult,
                                         @PathVariable("id") Integer id,
                                         RedirectAttributes redirectAttrs) {
        ModelAndView modelAndView = new ModelAndView();
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("carRepairPage");
            log.warn("Error occured during seeting repair for order #{}", id);
        } else {
            carService.repairHandler(orderService.findOrderById(id).getCar().getId(), id, repair);

            redirectAttrs.addAttribute("id", id).addFlashAttribute("successMessage", "Repair added successfully");
            modelAndView.addObject("car", new Car());
            modelAndView.setViewName("redirect:{id}");
        }
        return modelAndView;
    }


    @RequestMapping(value = "carRejectPage/{id}", method = RequestMethod.GET)
    public ModelAndView returnCar(@PathVariable(name = "id") int id) {
        ModelAndView modelAndView = new ModelAndView();
        Order order = orderService.findOrderById(id);
        modelAndView.addObject("order", order);
        modelAndView.setViewName("carRejectPage");
        return modelAndView;
    }

    @RequestMapping(path = "carRejectPage/{id}", method = RequestMethod.POST)
    public ModelAndView carReturnHandler(Order order, BindingResult bindingResult,
                                         @PathVariable("id") Integer id,
                                         RedirectAttributes redirectAttrs) {
        ModelAndView modelAndView = new ModelAndView();
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("carRejectPage");
        } else {
            orderService.rejectOrder(id, order.getDescription());
            redirectAttrs.addAttribute("id", id).addFlashAttribute("successMessage", "Order was rejected successfully");
            modelAndView.addObject("order", new Order());
            modelAndView.setViewName("redirect:{id}");

        }
        return modelAndView;
    }
}
