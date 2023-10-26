package com.backend.restapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.restapi.dto.ContractDto;
import com.backend.restapi.dto.RentRoomDto;
import com.backend.restapi.dto.RoomDto;
import com.backend.restapi.security.JWTGenerator;
import com.backend.restapi.service.ContractService;
import com.backend.restapi.service.PropertyService;
import com.backend.restapi.service.RentRoomService;
import com.backend.restapi.service.RoomService;

@RestController
@RequestMapping("/api")
public class RentRoomController {

	@Autowired
	private ContractService contractService;
	private RentRoomService rentRoomService;
	private JWTGenerator jwtGenerator;

	@Autowired
	public RentRoomController(ContractService contractService, RentRoomService rentRoomService,
			JWTGenerator jwtGenerator) {
		this.contractService = contractService;
		this.rentRoomService = rentRoomService;
		this.jwtGenerator = jwtGenerator;
	}

	@PostMapping("/rentRoom/joinRoom")
	public ResponseEntity<Integer> joinRoom(@RequestParam("room_id") int room_id,
			@RequestHeader("Authorization") String authorizationHeader) {
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			String token = authorizationHeader.substring(7);
			int userId = jwtGenerator.getUserIdFromJWT(token);
			ResponseEntity<Integer> response = rentRoomService.joinRoom(userId, room_id);
			return response;
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PostMapping("/rentRoom/joinRoomWithDependent")
	public ResponseEntity<Integer> joinRoomWithDependent(@RequestParam("rentRoom_id") int rentRoom_id,
			@RequestHeader("Authorization") String authorizationHeader) {
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			String token = authorizationHeader.substring(7);
			int userId = jwtGenerator.getUserIdFromJWT(token);
			ResponseEntity<Integer> response = rentRoomService.joinRoom(userId, rentRoom_id);
			return response;
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/MyRoomRented")
	public ResponseEntity<List<RoomDto>> getRoomsRented(@RequestHeader("Authorization") String authorizationHeader) {
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			String token = authorizationHeader.substring(7);
			int userId = jwtGenerator.getUserIdFromJWT(token);
			List<RoomDto> RoomDtos = rentRoomService.getRoomsRented(userId);
			return ResponseEntity.ok(RoomDtos);
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GetMapping("/GetAllRentRooms")
	public ResponseEntity<List<RentRoomDto>> getAllRentRooms(@RequestHeader("Authorization") String authorizationHeader) {
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			String token = authorizationHeader.substring(7);
			int userId = jwtGenerator.getUserIdFromJWT(token);
			List<RentRoomDto> rentRoomDtos = rentRoomService.getAllRentRooms(userId);
			return ResponseEntity.ok(rentRoomDtos);
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/MyRoomRentedDetail")
	public ResponseEntity<RoomDto> getRoomsRentedDetail(@RequestParam("rentRoom_id") int rentRoom_id) {
		RoomDto RoomDto = rentRoomService.getRoomRented(rentRoom_id);
		return ResponseEntity.ok(RoomDto);
	}
}
