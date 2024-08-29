package com.example.auth1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("no-auth")
    public String noAuth(){
        return "no auth";
    }
    @GetMapping("authenticated")
    public String authenticated(){
        return "authenticated";
    }
}
