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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

@Controller
public class PagesController {

//    TODO autowired through constructor
    @Autowired
    private UserService userService;

    @Autowired
    private CarService carService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RepairService repairService;

    @RequestMapping(value="/insides/allUsers/page/{page}")
    public ModelAndView usersPaginated(@PathVariable("page") int page,
                                       @RequestParam(required=false, name = "sort-field") final String sortField) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        //pagination

//        PageRequest pageable = PageRequest.of(page - 1, 2);
//        Page<User> userPage = userService.findAllUsers(pageable);

        System.out.println("===================");
        System.out.println(sortField);
        System.out.println("===================");
        Page<User> userPage = userService.findAllUsers(PageRequest.of(page - 1, 2, Sort.by(Sort.Direction.ASC,
                sortField)));
        int totalPages = userPage.getTotalPages();

        if(totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1,totalPages).boxed().collect(Collectors.toList());
            modelAndView.addObject("pageNumbers", pageNumbers);
        }

        modelAndView.addObject("currentPage", page);
        modelAndView.addObject("sortField", sortField);
        modelAndView.addObject("showUsers", userPage.getContent());
        modelAndView.setViewName("insides/allUsers");
        return modelAndView;

    }

//    @RequestMapping(value = "/sortByName", method = RequestMethod.GET)
//    public ModelAndView sortByName() {
//        ModelAndView modelAndView = new ModelAndView();
//        userService.banHandler(personId);
//        modelAndView.setViewName("redirect:insides/allUsers/page/1");
//        return modelAndView;
//    }

    @RequestMapping(value="/insides/home")
    public ModelAndView home(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();



        User user = userService.findUserByUserName(auth.getName());
        modelAndView.addObject("userName", "Welcome " + user.getUserName() + "/" + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        modelAndView.addObject("currentUser", user);
//        modelAndView.addObject("showUserNames", "Users' names: " + userService.findAllUsersByName());
//        modelAndView.addObject("showUsers", userService.findAllUsers());
        modelAndView.addObject("ordersList", orderService.findAllOrders());
        modelAndView.addObject("repairsList", repairService.findAllrepairs());


        String currentLang = LocaleContextHolder.getLocale() == Locale.forLanguageTag("ua") ? "uk" : "ua";
        modelAndView.addObject("currentLang", currentLang);

        modelAndView.setViewName("insides/home");
        return modelAndView;
    }

    @RequestMapping(value = "/ban_user", method = RequestMethod.GET)
    public ModelAndView handleBanUser(@RequestParam(name="personId")int personId) {
        ModelAndView modelAndView = new ModelAndView();
        userService.banHandler(personId);
        modelAndView.setViewName("redirect:insides/allUsers/page/1");
        return modelAndView;
    }

    @RequestMapping(value = "/manager_handler", method = RequestMethod.GET)
    public ModelAndView managerHandler(@RequestParam(name="personId")int personId) {
        ModelAndView modelAndView = new ModelAndView();
        userService.managerUpgrade(personId);
        modelAndView.setViewName("redirect:insides/allUsers/page/1");
        return modelAndView;
    }

    @GetMapping(value="/mainPage")
    public ModelAndView mainPage(){
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("showCars", carService.findAllCars());
        modelAndView.setViewName("mainPage");
        return modelAndView;

    }



}
