package com.backend.restapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.restapi.dto.LandlordRequestDTO;
import com.backend.restapi.dto.RentalRequestDto;
import com.backend.restapi.security.JWTGenerator;
import com.backend.restapi.service.AuthorizationService;
import com.backend.restapi.service.RequestService;

@RestController
@RequestMapping("/api/user_request")
public class UserReqManageController {

	@Autowired
	private final RequestService requestService;
	private final AuthorizationService authorizationService;
	private final LandlordRequestDTO landlordRequestDTO;
	private JWTGenerator jwtGenerator;

	@Autowired
	public UserReqManageController(RequestService requestService, LandlordRequestDTO landlordRequestDTO,
			JWTGenerator jwtGenerator, AuthorizationService authorizationService) {
		this.requestService = requestService;
		this.landlordRequestDTO = landlordRequestDTO;
		this.jwtGenerator = jwtGenerator;
		this.authorizationService = authorizationService;
	}

	@PostMapping("/cancel_request")
	public ResponseEntity<String> cancelRentalRequest(@RequestHeader("Authorization") String authorizationHeader,
			@RequestParam("requestId") int requestId) {
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			String token = authorizationHeader.substring(7);
			int userId = jwtGenerator.getUserIdFromJWT(token);
			try {
				requestService.cancelRequest(userId, requestId);
				return ResponseEntity.ok("Yêu cầu đã được hủy thành công.");
			} catch (RuntimeException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}
