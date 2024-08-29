package com.example.auth1;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("/users")
public class UserController {
    private final JpaUserService service;

    public UserController(JpaUserService service){
        this.service =service;
    }

    @GetMapping("/login")
    public String loginForm(){
        return "log-form";
    }
    @GetMapping("/my-profile")
    public String myProfile(
            Authentication authentication
    ){
        log.info(authentication.getName());

        return "my-profile";
    }
    @GetMapping("/register")
    public String registerForm(){
        return "register-form";
    }
    @PostMapping("/register")
    public String register(
            @RequestParam("username")
            String username,
            @RequestParam("password")
            String password,
            @RequestParam("passwordCheck")
            String passwordCheck
    ){
        service.createUser(username, password, passwordCheck);
        return "redirect:/users/login";
    }

}
