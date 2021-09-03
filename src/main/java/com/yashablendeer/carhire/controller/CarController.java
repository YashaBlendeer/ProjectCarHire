package com.yashablendeer.carhire.controller;

import com.yashablendeer.carhire.model.Car;
import com.yashablendeer.carhire.service.CarService;
import com.yashablendeer.carhire.service.OrderService;
import com.yashablendeer.carhire.service.RepairService;
import com.yashablendeer.carhire.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

/**
 * Controller for work with cars
 *
 * @author yaroslava
 * @version 1.0
 */

@Log4j2
@Controller
public class CarController {

    @Autowired
    private CarService carService;

    @GetMapping(value="/carAddPage")
    public ModelAndView addCar(){
        ModelAndView modelAndView = new ModelAndView();
        Car car = new Car();
        modelAndView.addObject("car", car);
        modelAndView.setViewName("carAddPage");
        return modelAndView;
    }

    @PostMapping(value = "/carAddPage")
    public ModelAndView createNewCar(@Valid Car car, BindingResult bindingResult, RedirectAttributes redirectAttrs) {
        ModelAndView modelAndView = new ModelAndView();
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("carAddPage");
            log.warn("Error during adding car: {}", bindingResult);

        } else {
            carService.saveCar(car);
            redirectAttrs.addFlashAttribute("successMessage", "Car has been registered successfully");
            modelAndView.addObject("car", new Car());
            log.info("A new car was added successfully: {}", car);
            modelAndView.setViewName("redirect:carAddPage");

        }
        return modelAndView;
    }

    @RequestMapping(value = "/deleteCar", method = RequestMethod.GET)
    public ModelAndView deleteCar(@RequestParam(name="carId")int carId,
                                  @RequestParam(required=false, name = "sort-field") final String sortField,
                                  @RequestParam(required=false, name = "currentPage") final String currentPage) {
        ModelAndView modelAndView = new ModelAndView();
        carService.deleteCarById(carId);
        modelAndView.setViewName("redirect:/mainPage/page/" + currentPage + "?sort-field=" + sortField );
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
            log.warn("Error occured during updating car", car);

        } else {
            carService.updateCar(car.getId(), car);
            redirectAttrs.addAttribute("id", id).addFlashAttribute("successMessage", "Car has been updated successfully");
            modelAndView.addObject("car", new Car());
            log.info("A car was updated", car);
            modelAndView.setViewName("redirect:{id}");

        }
        return modelAndView;
    }
}
