package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
public class HeyController {

    @Autowired
    private UserService service;

    @GetMapping("/hey")
    public String sayHey () {
        return "hey";
    }

}
