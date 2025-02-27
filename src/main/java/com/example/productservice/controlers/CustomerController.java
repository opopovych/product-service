package com.example.productservice.controlers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomerController {
    @GetMapping("/viewL")
    public String openVewLPage(){
        return "viewL";
    }
    @GetMapping("/viewM")
    public String openVewMPage(){
        return "viewM";
    }
    @GetMapping("/viewS")
    public String openVewSPage(){
        return "viewS";
    }
    @GetMapping("/index")
    public String openIndexPage(){
        return "index";
    }

}
