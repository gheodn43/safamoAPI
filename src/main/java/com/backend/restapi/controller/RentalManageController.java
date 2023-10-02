package com.backend.restapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.restapi.dto.LandlordRequestDTO;
import com.backend.restapi.dto.UserProfileUpdateDto;
import com.backend.restapi.dto.RequestDto;
import com.backend.restapi.security.JWTGenerator;
import com.backend.restapi.service.AuthorizationService;
import com.backend.restapi.service.RequestService;

@RestController
@RequestMapping("/api/rental_manage")
public class RentalManageController {
	@Autowired
	private final RequestService requestService;
	private final AuthorizationService authorizationService;
	private final LandlordRequestDTO landlordRequestDTO;
	private JWTGenerator jwtGenerator;

	@Autowired
	public RentalManageController(RequestService requestService, LandlordRequestDTO landlordRequestDTO,
			JWTGenerator jwtGenerator, AuthorizationService authorizationService) {
		this.requestService = requestService;
		this.landlordRequestDTO = landlordRequestDTO;
		this.jwtGenerator = jwtGenerator;
		this.authorizationService = authorizationService;
	}

	@PostMapping("/req_landlord/{user_id}")
	public ResponseEntity<String> createRentalRequestForLandlord(@PathVariable("user_id") int userId,
			@RequestBody LandlordRequestDTO landlordRequestDTO) {
		String rentalDetails = landlordRequestDTO.getDescription();

		try {
			requestService.createLandlordRequestForCustomer(userId, rentalDetails);
			return ResponseEntity.ok("Yêu cầu đã được gửi thành công!");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Lỗi: " + e.getMessage());
		}
	}

	@PostMapping("/req_landlord/reject")
	public ResponseEntity<String> rejectLandlordRequest(@RequestParam("requestId") int requestId,
			@RequestHeader("Authorization") String authorizationHeader) {
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			// Lấy token bằng cách loại bỏ phần "Bearer " từ header
			String token = authorizationHeader.substring(7);
			int userId = jwtGenerator.getUserIdFromJWT(token);
			try {
				requestService.rejectLandlordRequest(userId, requestId);
				return ResponseEntity.ok("Yêu cầu đã được từ chối.");
			} catch (RuntimeException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

	}

	@GetMapping("/requests/list")
	public ResponseEntity<List<RequestDto>> getAllRequests(@RequestHeader("Authorization") String authorizationHeader) {
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			String token = authorizationHeader.substring(7);
			if (authorizationService.isAdminUser(token)) {
				List<RequestDto> requestDtos = requestService.getAllRequestDtos();
				return ResponseEntity.ok(requestDtos);
			}
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

	}

}