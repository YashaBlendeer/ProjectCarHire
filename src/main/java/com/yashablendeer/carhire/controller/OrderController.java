package com.yashablendeer.carhire.controller;

import com.yashablendeer.carhire.model.*;
import com.yashablendeer.carhire.service.CarService;
import com.yashablendeer.carhire.service.OrderService;
import com.yashablendeer.carhire.service.RepairService;
import com.yashablendeer.carhire.service.UserService;
import com.yashablendeer.carhire.util.DateUtil;
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

@Controller
public class OrderController {

//    TODO autowired through constructor

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


//    TODO make code look better
    @RequestMapping(path = "carOrderPage/{id}", method = RequestMethod.POST)
    public ModelAndView carOrderHandler(@Valid Order order, BindingResult bindingResult,
                                        @PathVariable("id") Integer carId,
                                        RedirectAttributes redirectAttrs) {
        ModelAndView modelAndView = new ModelAndView();

        boolean dateNotAvailable = orderService.checkDateAvailability(carService.findCarById(carId), order.getStartTime(), order.getEndTime());

        System.out.println("===============");
        System.out.println(dateNotAvailable);
        System.out.println("===============");

        redirectAttrs.addAttribute("id", carId);
        if (dateNotAvailable) {
            bindingResult
                    .rejectValue("startTime", "error.order",
                            "This date is not available");
            redirectAttrs.addFlashAttribute("dateNotAvailableMessage", "This date is not available");
        }

        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("redirect:{id}");
        } else {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

//            TODO move user and car straight to orderService.saveBuilder?
            User user = userService.findUserByUserName(auth.getName());
            Car car = carService.findCarById(carId);

            orderService.saveBuilder(order, car, user);
            redirectAttrs.addAttribute("id", carId).addFlashAttribute("successMessage",
                                                    "Order was sent for processing successfully");
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
    @RequestMapping(value = "/payRepair", method = RequestMethod.GET)
    public ModelAndView payRepairHandler(@RequestParam(name="orderId")int orderId) {
        ModelAndView modelAndView = new ModelAndView();
        //TODO move from controller
        Repair repair = repairService.findRepairByOrderId(orderId);
        repair.setPayStatus(Status.PAYED);
        repair.getOrder().getCar().setStatus(Status.READY);
        repairService.save(repair);
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
        } else {
            carService.repairHandler(orderService.findOrderById(id).getCar().getId());
            repair.setOrder(orderService.findOrderById(id));
            repair.setPayStatus(Status.UNPAYED);
            repairService.save(repair);
            redirectAttrs.addAttribute("id", id).addFlashAttribute("successMessage", "Car has been updated successfully");
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
