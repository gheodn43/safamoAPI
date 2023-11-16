package com.backend.restapi.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.restapi.dto.adminRequestDto;
import com.backend.restapi.models.Request;
import com.backend.restapi.repository.RequestRepository;
import com.backend.restapi.repository.RequestRoleRepository;

@Service
public class AdminDashboardService {
    private final RequestRepository requestRepository;
    private final RequestRoleRepository requestRoleRepository;
    
    @Autowired
    public AdminDashboardService(RequestRepository requestRepository, RequestRoleRepository requestRoleRepository) {
        this.requestRepository = requestRepository;
        this.requestRoleRepository = requestRoleRepository;
    }
    
    public List<adminRequestDto> requestType() {
        List<adminRequestDto> dtos = new ArrayList<>();
        List<Request> allRequests = requestRepository.findAll();
        HashMap<String, Integer> requestMap = new HashMap<>();
        
        for (Request request : allRequests) {
            String requestType = request.getRequestRole().getName();
            requestMap.put(requestType, requestMap.getOrDefault(requestType, 0) + 1);
        }
        
        for (String type : requestMap.keySet()) {
        	adminRequestDto dto = new adminRequestDto();
            dto.setRequest_name(type);
            dto.setRequest_count(requestMap.get(type));
            dtos.add(dto);
        }
        return dtos;
    }
}

