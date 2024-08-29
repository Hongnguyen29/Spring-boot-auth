package com.example.auth1.jwt;

import com.example.auth1.jwt.dto.JwtRequestDto;
import com.example.auth1.jwt.dto.JwtResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    @PostMapping("/issue")
    public JwtResponseDto issueJwt(
            @RequestBody
            JwtRequestDto dto
    ) {
        // 아이디로 사용자 조회
        UserDetails userDetails;
        try {
            userDetails = userService.loadUserByUsername(dto.getUsername());
        } catch (UsernameNotFoundException ignored) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        // 비밀번호 일치 확인
        if (!passwordEncoder.matches(
                dto.getPassword(), userDetails.getPassword()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        // JWT 발급
        String jwt = tokenUtils.generateToken(userDetails);
        JwtResponseDto response = new JwtResponseDto();
        response.setToken(jwt);
        return response;
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
