package com.backend.restapi.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.restapi.security.JWTGenerator;

@Service
public class AuthorizationService {
    private final JWTGenerator jwtGenerator;

    @Autowired
    public AuthorizationService(JWTGenerator jwtGenerator) {
        this.jwtGenerator = jwtGenerator;
    }

    public boolean isAdminUser(String token) {
        List<String> rolesList = jwtGenerator.getRolesFromJWT(token);
        String[] roles = rolesList.toArray(new String[0]); // Convert List<String> to String[]
        for (String role : roles) {
            if ("ADMIN".equals(role)) { // Use equals to compare strings
                return true;
            }
        }
        return false;
    }
}
