package com.airbnb.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private JWTRequestFilter jwtRequestFilter;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{


        //CSRF = SOMEBODY CAN TRY TO LOGIN FROM DIFFERENT DOMAIN
        http.csrf().disable().cors().disable();
        http.addFilterBefore(jwtRequestFilter, AuthorizationFilter.class);
//        http.authorizeHttpRequests()
//                .requestMatchers("/api/v1/users/addUser","/api/v1/users/login")
//                .permitAll().requestMatchers("/api/v1/countries/addCountry").hasRole("ADMIN")
//               .requestMatchers("/api/v1/users/profile").hasAnyRole("ADMIN","USER")
//                .anyRequest().authenticated();   //HAAP

        http.authorizeHttpRequests().anyRequest().permitAll();
        return http.build();

        //BUILD IS METHOD HELP US TO CREATE OBJECT, OBJECT= http
        //build method put all the info in the object
        //object will go to spring security framework & it study the object and give the url access

    }


}
