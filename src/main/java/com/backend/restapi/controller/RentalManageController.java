package com.backend.restapi.controller;

import java.util.ArrayList;
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
import com.backend.restapi.dto.RentalRequestDto;
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
	public ResponseEntity<String> createLandlordRequest(@PathVariable("user_id") int userId,
			@RequestBody LandlordRequestDTO landlordRequestDTO) {
		String rentalDetails = landlordRequestDTO.getDescription();

		try {
			requestService.createLandlordRequestForCustomer(userId, rentalDetails);
			return ResponseEntity.ok("Yêu cầu đã được gửi thành công!");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Lỗi: " + e.getMessage());
		}
	}

	@PostMapping("/req_rental/{user_id}/{room_id}")
	public ResponseEntity<String> createRentalRequest(@PathVariable("user_id") int userId,
			@PathVariable("room_id") int roomId) {
		try {
			requestService.createRentalRequestForCustomer(userId, roomId);
			return ResponseEntity.ok("Yêu cầu thuê đã được gửi thành công!");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Lỗi: " + e.getMessage());
		}
	}

	@PostMapping("/req_rental/get_all")
	public ResponseEntity<List<RentalRequestDto>> getRentalRequestForRoom(
	    @RequestParam("room_id") int roomId,
	    @RequestHeader("Authorization") String authorizationHeader) {
	    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
	        String token = authorizationHeader.substring(7);
	        int userId = jwtGenerator.getUserIdFromJWT(token);
	            List<RentalRequestDto> requestDtos = requestService.getAllRentalRequestForRoom(roomId, userId);
	            return ResponseEntity.ok(requestDtos);
	    } else {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}

	@PostMapping("/request/get_all")
	public ResponseEntity<List<RentalRequestDto>> getAllRequestForSender(
	    @RequestHeader("Authorization") String authorizationHeader) {
	    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
	        String token = authorizationHeader.substring(7);
	        int userId = jwtGenerator.getUserIdFromJWT(token);
	            List<RentalRequestDto> requestDtos = requestService.getAllRequestForSender(userId);
	            return ResponseEntity.ok(requestDtos);
	    } else {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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

	@PostMapping("/req_rental/reject")
	public ResponseEntity<String> rejectRentalRequest(@RequestParam("requestId") int requestId) {
		try {
			requestService.rejectRentalRequest(requestId);
			return ResponseEntity.ok("Yêu cầu đã được từ chối.");
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PostMapping("/req_landlord/accept")
	public ResponseEntity<String> acceptLandlordRequest(@RequestParam("requestId") int requestId,
			@RequestHeader("Authorization") String authorizationHeader) {
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			String token = authorizationHeader.substring(7);
			int userId = jwtGenerator.getUserIdFromJWT(token);
			try {
				requestService.acceptLandlordRequest(userId, requestId);
				return ResponseEntity.ok("Yêu cầu đã được thông qua.");
			} catch (RuntimeException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PostMapping("/req_rental/accept")
	public ResponseEntity<String> acceptRentalRequest(@RequestParam("requestId") int requestId) {
		try {
			requestService.acceptRentalRequest(requestId);
			return ResponseEntity.ok("Yêu cầu đã được thông qua.");
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
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

	@GetMapping("/req_rentals")
	public ResponseEntity<List<RentalRequestDto>> rentalReqForPropertyOwner(
			@RequestHeader("Authorization") String authorizationHeader) {
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			String token = authorizationHeader.substring(7);
			int userId = jwtGenerator.getUserIdFromJWT(token);
			List<RentalRequestDto> requestDtos = requestService.getAllRentalRequestForPropertyOwner(userId);
			return ResponseEntity.ok(requestDtos);
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}