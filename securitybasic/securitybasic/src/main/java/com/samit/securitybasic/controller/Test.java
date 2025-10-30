package com.samit.securitybasic.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class Test {

    @GetMapping("/samit")
    public String hello(){
        return "Samit Verma";
    }
}
