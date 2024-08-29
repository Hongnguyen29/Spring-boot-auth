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
    @GetMapping("/user-role")
    public String userRole(){
        return "user-role";

    }
    @GetMapping("/admin-role")
    public String adminRole(){
        return "admin-role";
    }
    @GetMapping("/read-authority")
    public String readAuthority(){
        return "read-authority";

    }
    @GetMapping("/write-authority")
    public String writeAuthority(){
        return "write-authority";
    }
}
