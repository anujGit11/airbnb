package com.airbnb.config;

import com.airbnb.entity.PropertyUser;
import com.airbnb.repository.PropertyUserRepository;
import com.airbnb.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

@Component
public class JWTRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private PropertyUserRepository userRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //With the authenticated request we send the token, this request will come to Do Filter method,
        //do filter request object from the header of the request ,it extracts the token.

        String tokenHeader = request.getHeader("Authorization");
        if(tokenHeader!=null && tokenHeader.startsWith("Bearer ")){
            String token = tokenHeader.substring(8,tokenHeader.length()-1);
            String username = jwtService.getUsername(token);  //get username from the token after verify the token in jwtService


         //after getting username , it checks whether username exists or not in database
            Optional<PropertyUser> opUser = userRepository.findByUsername(username);
            if(opUser.isPresent()){
                PropertyUser user = opUser.get(); //the object we get from db ,we have to create a session for this obj(user)

                //Session will create a unique id for every user and it is permanent
                //




                //to keep track of current user logged in....
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user,null, Collections.singleton(new SimpleGrantedAuthority(user.getUserRole())));
                authentication.setDetails(new WebAuthenticationDetails(request));  //Adani set new WareHouse...
                SecurityContextHolder.getContext().setAuthentication(authentication);



            }
        }

        filterChain.doFilter(request,response);


    }
}
