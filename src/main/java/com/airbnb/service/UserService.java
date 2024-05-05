package com.airbnb.service;


import com.airbnb.dto.LoginDto;
import com.airbnb.dto.PropertyUserDto;
import com.airbnb.entity.PropertyUser;
import com.airbnb.repository.PropertyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {


    @Autowired
    private PropertyUserRepository userRepository;

    @Autowired
    private JWTService jwtService;

    public PropertyUser addUser(PropertyUserDto propertyUserDto){
        PropertyUser user = new PropertyUser();

        user.setFirstName(propertyUserDto.getFirstName());
        user.setLastName(propertyUserDto.getLastName());
        user.setUsername(propertyUserDto.getUsername());
        user.setEmail(propertyUserDto.getEmail());
        user.setPassword(BCrypt.hashpw(propertyUserDto.getPassword(),BCrypt.gensalt(10)));
        user.setUserRole(propertyUserDto.getUserRole());

        PropertyUser savedUser = userRepository.save(user);

        return savedUser;

    }

    public String verifyLogin(LoginDto loginDto) {

        Optional<PropertyUser> opUser = userRepository.findByUsername(loginDto.getUsername());

        if (opUser.isPresent()) {

            PropertyUser propertyUser = opUser.get();  //password coming in propertyUser is Encrypted password  and password present in logindto is expected or RAW password

            if (BCrypt.checkpw(loginDto.getPassword(), propertyUser.getPassword())) {// comparing raw and encrypted password

                return jwtService.generateToken(propertyUser);
            }
        }
        return null;
    }

}
