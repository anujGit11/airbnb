package com.airbnb.controller;


import com.airbnb.dto.LoginDto;
import com.airbnb.dto.PropertyUserDto;
import com.airbnb.dto.TokenResponse;
import com.airbnb.entity.PropertyUser;
import com.airbnb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {


    @Autowired
    private UserService userService;


    //http://localhost:8080/api/v1/users/addUser
    @PostMapping("/addUser")
    public ResponseEntity<String> addUser(@RequestBody PropertyUserDto propertyUserDto){
        PropertyUser propertyUser = userService.addUser(propertyUserDto);
        if(propertyUser!= null){
            return new ResponseEntity<>("Registration is successfull", HttpStatus.CREATED);
        }

        return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto){

//        while login, request will come from client(Postman) to Controller
//        In controller, it takes json content into loginDto with the help of @RequestBody,
//        now from controller it will go to userService ,
//        userService will gets the actual record from Db and it compares the raw and encrypted password
//        if it trues then it will generates the token in JWTSERVICE
        //JWT Service will return the token to userService and it returns to Controller

        String token = userService.verifyLogin(loginDto);
        if(token!=null){
            TokenResponse tokenResponse = new TokenResponse();
            tokenResponse.setToken(token);
            return new ResponseEntity<>(tokenResponse,HttpStatus.OK);
        }

        return new ResponseEntity<>("invalid credentials",HttpStatus.UNAUTHORIZED);


    }

    @GetMapping("/profile")
    public PropertyUser getCurrentUserProfile(@AuthenticationPrincipal PropertyUser user){

        return user;
    }
}
