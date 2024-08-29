package com.example.auth1.config;

import com.example.auth1.jwt.JwtTokenFilter;
import com.example.auth1.jwt.JwtTokenUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
public class WebSecurityConfig {
    private final JwtTokenUtils tokenUtils;
    private final UserDetailsService userService;
    public WebSecurityConfig(JwtTokenUtils tokenUtils,
                             UserDetailsService userService) {
        this.tokenUtils = tokenUtils;
        this.userService = userService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception{

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(
                            "/no-auth",
                                    "/error",
                                    "/users/register"
                                    ,"/token/issue")
                            .permitAll();
                    auth.requestMatchers("/authenticated",
                                    "/users/my-profile")
                            .authenticated();
                    auth.requestMatchers("/user-role")
                                    .hasRole("USER");
                    auth.requestMatchers("/admin-role")
                                    .hasRole("ADMIN");
                    auth.requestMatchers("/read-authority")
                                    .hasAuthority("READ");
                    auth.requestMatchers("/write-authority")
                                    .hasAuthority("WRITE");

                    auth.anyRequest()
                            .authenticated();

                })
                .formLogin(formLogin -> formLogin
                        .loginPage("/users/login")
                        .defaultSuccessUrl("/users/my-profile")
                        .failureUrl("/users/login?/fail")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/users/logout")
                        .logoutSuccessUrl("/users/login")
                )
                .addFilterBefore(
                        new JwtTokenFilter(
                                tokenUtils,
                                userService
                        ),
                        AuthorizationFilter.class
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    // 사용자 정보를 저장하는 객체
   /* @Bean
    public UserDetailsService userDetailsService(
            PasswordEncoder passwordEncoder
    ) {
        UserDetails user1 = User.withUsername("user1")
                .password(passwordEncoder.encode("password"))
                .build();
        UserDetails user2 = User.withUsername("user2")
                .password(passwordEncoder.encode("password"))
                .build();
        return new InMemoryUserDetailsManager(user1,user2);
    }*/

    // 비밀번호의 암호화를 담당하는 객체
   /* @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }*/
}
