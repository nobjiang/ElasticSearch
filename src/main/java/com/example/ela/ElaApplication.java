package com.example.ela;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
public class ElaApplication {
    public static void main(String[] args) {
        SpringApplication.run(ElaApplication.class, args);
    }

    @GetMapping("/")
    public String index(){
        return "index";
    }

}
