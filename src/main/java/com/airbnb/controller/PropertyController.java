package com.airbnb.controller;

import com.airbnb.entity.Property;
import com.airbnb.repository.PropertyRepository;
import com.airbnb.repository.PropertyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/properties")
public class PropertyController {

    @Autowired
    private PropertyRepository propertyRepository;

    @GetMapping("{locationName}")
    public ResponseEntity <List<Property>> findProperty(@PathVariable String locationName){

        List<Property> properties = propertyRepository.findPropertyByLocation(locationName);
        return new ResponseEntity<>(properties, HttpStatus.OK);
    }

}
