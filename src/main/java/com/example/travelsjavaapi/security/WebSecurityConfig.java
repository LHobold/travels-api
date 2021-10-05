package com.example.travelsjavaapi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.http.HttpMethod;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.Override;

import com.example.travelsjavaapi.exceptions.FilterChainExceptionHandler;
import com.example.travelsjavaapi.service.MyUserDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private FilterChainExceptionHandler filterChainExceptionHandler;

    @Autowired
    private RestAccessDeniedHandler restAccessDeniedHandler;

    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable().authorizeRequests().antMatchers("/api/v1/stats").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll().anyRequest().authenticated().and()
                .exceptionHandling()
                // Filter 401
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                // Filter authenticated users -> not implemented
                .accessDeniedHandler(restAccessDeniedHandler).and()
                // Login req filter
                .addFilterBefore(new JWTLoginFilter("/api/v1/auth/login", authenticationManager()),
                        UsernamePasswordAuthenticationFilter.class)

                // Filter requisitions and search for Bearer header
                .addFilterBefore(new JWTAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(filterChainExceptionHandler, LogoutFilter.class);

        // Registers userDetailsService
        httpSecurity.userDetailsService(myUserDetailsService);

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // UserDetailsService with BCrypt Password Encoder
        auth.userDetailsService(myUserDetailsService).passwordEncoder(passwordEncoder());

    }

}
