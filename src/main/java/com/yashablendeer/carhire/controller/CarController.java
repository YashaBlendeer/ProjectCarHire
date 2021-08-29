package com.yashablendeer.carhire.controller;

import com.yashablendeer.carhire.model.Car;
import com.yashablendeer.carhire.service.CarService;
import com.yashablendeer.carhire.service.OrderService;
import com.yashablendeer.carhire.service.RepairService;
import com.yashablendeer.carhire.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class CarController {

//    TODO autowired through constructor
    @Autowired
    private UserService userService;

    @Autowired
    private CarService carService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RepairService repairService;

    @GetMapping(value="/carAddPage")
    public ModelAndView addCar(){
        ModelAndView modelAndView = new ModelAndView();
        Car car = new Car();
        modelAndView.addObject("car", car);
        modelAndView.setViewName("carAddPage");
        return modelAndView;
    }

    @PostMapping(value = "/carAddPage")
    public ModelAndView createNewCar(@Valid Car car, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("carAddPage");
        } else {
            carService.saveCar(car);
            modelAndView.addObject("successMessage", "Car has been registered successfully");
            modelAndView.addObject("car", new Car());
            modelAndView.setViewName("carAddPage");

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
}