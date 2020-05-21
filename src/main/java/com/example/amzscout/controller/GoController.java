package com.example.amzscout.controller;

import com.example.amzscout.Throttled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class GoController {

    @Throttled
    @GetMapping("/go")
    public String go(HttpServletRequest request) {
        return "";
    }
}