package com.yashablendeer.carhire.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PagesController {

    @RequestMapping("/api")
    public String mainPage(){
        return "main";
    }

}
