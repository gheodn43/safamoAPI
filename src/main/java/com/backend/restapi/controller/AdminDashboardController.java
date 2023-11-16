package com.backend.restapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.restapi.dto.RoomDto;
import com.backend.restapi.dto.adminRequestDto;
import com.backend.restapi.service.AdminDashboardService;

@RestController
@RequestMapping("/api")
public class AdminDashboardController {

	private AdminDashboardService adminDashboardService;

	@Autowired
	public AdminDashboardController(AdminDashboardService adminDashboardService) {
		this.adminDashboardService = adminDashboardService;
	}

	@GetMapping("/auth/request-type")
	public List<adminRequestDto> requestType() {
		List<adminRequestDto> requestTypes = adminDashboardService.requestType();
		return requestTypes;
	}
}
