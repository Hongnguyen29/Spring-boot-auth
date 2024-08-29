package com.example.auth1.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/token")
public class JwtTokenController {
    private final JwtTokenUtils tokenUtils;
    private final UserDetailsService userService;
    private final PasswordEncoder passwordEncoder;

    public JwtTokenController(
            JwtTokenUtils tokenUtils,
            UserDetailsService userService,
            PasswordEncoder passwordEncoder) {
        this.tokenUtils = tokenUtils;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("test")
    public String test(){
        UserDetails userDetails =
                userService.loadUserByUsername("alex");
        String token = tokenUtils.generateToken(userDetails);
        log.info(token);
        log.info(tokenUtils.parseClaims(token).toString());
        return token;
    }
}
