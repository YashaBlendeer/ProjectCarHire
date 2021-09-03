package com.yashablendeer.carhire.controller;

import com.yashablendeer.carhire.model.*;
import com.yashablendeer.carhire.service.CarService;
import com.yashablendeer.carhire.service.OrderService;
import com.yashablendeer.carhire.service.RepairService;
import com.yashablendeer.carhire.service.UserService;
import com.yashablendeer.carhire.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Controller for common pages
 *
 * @author yaroslava
 * @version 1.0
 */

@Controller
public class PagesController {

    private UserService userService;
    private CarService carService;
    private OrderService orderService;
    private RepairService repairService;

    /**
     * Constructor to autowire
     *
     * @author yaroslava
     * @version 1.0
     */

    @Autowired
    public PagesController(UserService userService, CarService carService, OrderService orderService, RepairService repairService) {
        this.userService = userService;
        this.carService = carService;
        this.orderService = orderService;
        this.repairService = repairService;
    }

    @RequestMapping(value="/insides/allOrders/page/{page}")
    public ModelAndView ordersPaginated(@PathVariable("page") int page) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());

        //pagination

        Page<Order> orderPage = auth.getAuthorities().stream()
                                    .anyMatch(a -> a.getAuthority().equals("MANAGER")) ?
                orderService.findAllOrdersPageable(PageRequest.of(page-1, 2)) :
                orderService.findAllOrdersByUserPageable(user, PageRequest.of(page-1, 2));

        int totalPages = orderPage.getTotalPages();

        if(totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1,totalPages).boxed().collect(Collectors.toList());
            modelAndView.addObject("pageNumbers", pageNumbers);
        }

        modelAndView.addObject("currentPage", page);
        modelAndView.addObject("ordersList", orderPage.getContent());
        modelAndView.addObject("currentUser", user);
        modelAndView.addObject("repairsList", repairService.findAllrepairs());


        String currentLang = LocaleContextHolder.getLocale() == Locale.forLanguageTag("ua") ? "uk" : "ua";
        modelAndView.addObject("currentLang", currentLang);
        modelAndView.setViewName("insides/allOrders");
        return modelAndView;

    }

    @RequestMapping(value="/insides/home")
    public ModelAndView home(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        User user = userService.findUserByUserName(auth.getName());
        modelAndView.addObject("userName",  user.getUserName() + "/" + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        modelAndView.setViewName("insides/home");
        return modelAndView;
    }

    @GetMapping(value="/mainPage/page/{page}")
    public ModelAndView mainPage(@PathVariable("page") int page,
                                 @RequestParam(required=false, name = "sort-field") final String sortField){
        ModelAndView modelAndView = new ModelAndView();

        Page<Car> carPage = carService.findAllCarsPageable(PageRequest.of(page - 1, 2, Sort.by(Sort.Direction.ASC,
                sortField)));
        int totalPages = carPage.getTotalPages();

        if(totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1,totalPages).boxed().collect(Collectors.toList());
            modelAndView.addObject("pageNumbers", pageNumbers);
        }


        modelAndView.addObject("currentPage", page);
        modelAndView.addObject("sortField", sortField);
        modelAndView.addObject("showCars", carPage.getContent());
        modelAndView.addObject("keyword", new FormView());
        modelAndView.setViewName("mainPage");
        return modelAndView;

    }

    @RequestMapping(value = "/findCar", method = RequestMethod.POST)
    public ModelAndView findCarByQuality(@ModelAttribute(name = "keyword") FormView keyword, BindingResult bindingResult,
                                @RequestParam(required=false, name = "sort-field") String sortField,
                                @RequestParam(required=false, name = "currentPage") int currentPage,
                                @RequestParam(required=false, name = "lang") String lang){
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("currentPage", currentPage);
        modelAndView.addObject("sortField", sortField);

        List<Car> foundCars = keyword.getField().equals("carQuality") ?
                carService.findCarsByCarQuality(keyword.getMessage()) :
                carService.findCarsByCarMark(keyword.getMessage());
        modelAndView.addObject("showCars", foundCars);


        modelAndView.setViewName("mainPage");
            return modelAndView;
    }
}
