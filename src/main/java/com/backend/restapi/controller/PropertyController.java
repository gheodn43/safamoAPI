package com.backend.restapi.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.restapi.dto.PropertyDto;
import com.backend.restapi.models.PropertyEntity;
import com.backend.restapi.service.PropertyService;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    @Autowired
    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createProperty(@RequestBody PropertyDto propertyDto, Principal principal) {
        String username = principal.getName();
        ResponseEntity<String> response = propertyService.createProperty(propertyDto, username);
        return response;
    }
    
    
    @GetMapping("/{propertyId}")
    public ResponseEntity<PropertyDto> getPropertyInfo(@PathVariable int propertyId) {
        // Gọi phương thức service để lấy thông tin property
    	PropertyDto propertyInfo = propertyService.getPropertyInfo(propertyId);
        
        if (propertyInfo != null) {
            return new ResponseEntity<>(propertyInfo, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    } 
}
