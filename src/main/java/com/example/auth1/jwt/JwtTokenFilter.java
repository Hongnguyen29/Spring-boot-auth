package com.example.auth1.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Slf4j

public class JwtTokenFilter extends OncePerRequestFilter {
    private final JwtTokenUtils tokenUtils;
    private final UserDetailsService userService;

    public JwtTokenFilter(JwtTokenUtils tokenUtils,
                          UserDetailsService userService){
        this.tokenUtils =tokenUtils;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader =
       //         request.getHeader("Authorization");
        request.getHeader(HttpHeaders.AUTHORIZATION); //hai cach cho kq nhu nhau
        if(authHeader==null){
            filterChain.doFilter(request, response);
            return;
        }
        String[]  headerSplit = authHeader.split(" ");
        if(!(headerSplit.length == 2 &&
        headerSplit[0].equals("Bearer"))){
            filterChain.doFilter(request,response);
            return;
        }
        String jwt = headerSplit[1];
        if(!tokenUtils.validate(jwt)){
            filterChain.doFilter(request,response);
            return;
        }
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        String username = tokenUtils
                .parseClaims(jwt)
                .getSubject();
        UserDetails userDetails = userService.loadUserByUsername(username);
        AbstractAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        userDetails.getPassword(),
                        userDetails.getAuthorities()
                );
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        filterChain.doFilter(request, response);

    }
}
