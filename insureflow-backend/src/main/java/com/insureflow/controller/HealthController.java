package com.insureflow.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    @GetMapping("/")
    public String home(){
        return "Insurance Management platform API running...";
    }
    @GetMapping("/health")
    public String health(){
        return "application is healthy";
    }
}
