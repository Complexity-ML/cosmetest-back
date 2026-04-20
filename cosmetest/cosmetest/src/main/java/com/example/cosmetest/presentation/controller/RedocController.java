package com.example.cosmetest.presentation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class RedocController {

    @GetMapping("/redoc")
    public RedirectView redoc() {
        return new RedirectView("webjars/redoc/2.0.0/redoc.html?url=/v3/api-docs");
    }
}