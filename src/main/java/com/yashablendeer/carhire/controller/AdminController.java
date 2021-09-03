package com.yashablendeer.carhire.controller;

import com.yashablendeer.carhire.model.Role;
import com.yashablendeer.carhire.model.User;
import com.yashablendeer.carhire.service.CarService;
import com.yashablendeer.carhire.service.OrderService;
import com.yashablendeer.carhire.service.RepairService;
import com.yashablendeer.carhire.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Controller for admin pages
 *
 * @author yaroslava
 * @version 1.0
 */

@Log4j2
@Controller
public class AdminController {
    @Autowired
    private UserService userService;

    @RequestMapping(value="/insides/allUsers/page/{page}")
    public ModelAndView usersPaginated(@PathVariable("page") int page,
                                       @RequestParam(required=false, name = "sort-field") final String sortField) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        //pagination

        Page<User> userPage = userService.findAllUsersPageable(PageRequest.of(page - 1, 2, Sort.by(Sort.Direction.ASC,
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

    @RequestMapping(value = "/ban_user", method = RequestMethod.GET)
    public ModelAndView handleBanUser(@RequestParam(name="personId")int personId,
                                      @RequestParam(required=false, name = "sort-field") final String sortField,
                                      @RequestParam(required=false, name = "currentPage") final String currentPage) {
        ModelAndView modelAndView = new ModelAndView();
        userService.banHandler(personId);
        modelAndView.setViewName("redirect:insides/allUsers/page/" + currentPage + "?sort-field=" + sortField);
        return modelAndView;
    }

    @RequestMapping(value = "/manager_handler", method = RequestMethod.GET)
    public ModelAndView managerHandler(@RequestParam(name="personId")int personId,
                                       @RequestParam(required=false, name = "sort-field") final String sortField,
                                       @RequestParam(required=false, name = "currentPage") final String currentPage) {
        ModelAndView modelAndView = new ModelAndView();
        userService.setManagerHandler(personId);
        modelAndView.setViewName("redirect:insides/allUsers/page/" + currentPage + "?sort-field=" + sortField);
        return modelAndView;
    }
}
