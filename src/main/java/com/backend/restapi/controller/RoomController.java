package com.backend.restapi.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.restapi.dto.PropertyDto;
import com.backend.restapi.dto.RoomDto;
import com.backend.restapi.dto.RoomPictureDto;
import com.backend.restapi.dto.RoomPrivateOutputDto;
import com.backend.restapi.exception.UnauthorizedException;
import com.backend.restapi.security.JWTGenerator;
import com.backend.restapi.service.PropertyService;
import com.backend.restapi.service.RoomService;

@RestController
@RequestMapping("/api")
public class RoomController {
	@Autowired
	private PropertyService propertyService;
	private RoomService roomService;
	private JWTGenerator jwtGenerator;

	@Autowired
	public RoomController(PropertyService propertyService, RoomService roomService, JWTGenerator jwtGenerator) {
		this.propertyService = propertyService;
		this.roomService = roomService;
		this.jwtGenerator = jwtGenerator;
	}

	@GetMapping("/room/view_all") // cho admin
	public ResponseEntity<List<RoomDto>> getAllRooms(@AuthenticationPrincipal UserDetails userDetails) {
		if (userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ADMIN"))) {
			List<RoomDto> RoomDtos = roomService.getAllRoomsforAdmin();
			return ResponseEntity.ok(RoomDtos);
		} else {
			throw new UnauthorizedException("Access denied");
		}
	}

	@GetMapping("/room/view_detail/{room_id}") // cho admin
	public ResponseEntity<RoomDto> getOneRoomForAdmin(@PathVariable int room_id,
			@AuthenticationPrincipal UserDetails userDetails) {
		if (userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ADMIN"))) {
			RoomDto RoomDtos = roomService.getOneforAdmin(room_id);
			return ResponseEntity.ok(RoomDtos);
		} else {
			throw new UnauthorizedException("Access denied");
		}
	}

	@GetMapping("/my_room/get_all/{property_id}") // cho owner
	public ResponseEntity<List<RoomPrivateOutputDto>> getALlRoomOwner(@PathVariable int property_id) {
		List<RoomPrivateOutputDto> RoomDtos = roomService.getAllForOwner(property_id);
		return ResponseEntity.ok(RoomDtos);

	}

	@GetMapping("/my_room/view_detail/{room_id}") // cho owner
	public ResponseEntity<RoomDto> getOneRoomOwner(@PathVariable int room_id,
			@RequestHeader("Authorization") String authorizationHeader) {
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			String token = authorizationHeader.substring(7);
			int user_id = jwtGenerator.getUserIdFromJWT(token);
			RoomDto RoomDtos = roomService.getOneforPropertyOwner(room_id, user_id);
			return ResponseEntity.ok(RoomDtos);
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/auth/rooms") // cho guest
	public ResponseEntity<List<RoomDto>> getAllRoomsOwner() {
		List<RoomDto> RoomDtos = roomService.getAllRoomsforGuest();
		return ResponseEntity.ok(RoomDtos);
	}

	@GetMapping("/auth/rooms/{room_id}") // cho guest
	public ResponseEntity<RoomDto> getOneRoomOwner(@PathVariable int room_id) {
		RoomDto RoomDtos = roomService.getOneforGuest(room_id);
		return ResponseEntity.ok(RoomDtos);
	}

	@PostMapping("/room/create/{propertyId}")
	public ResponseEntity<String> createRoom(@PathVariable int propertyId, @RequestBody RoomDto roomDto,
			@RequestHeader("Authorization") String authorizationHeader) {
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			String token = authorizationHeader.substring(7);
			int user_id = jwtGenerator.getUserIdFromJWT(token);
			ResponseEntity<String> response = roomService.createRoomforPropertyOwner(roomDto, propertyId, user_id);
			return response;
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PostMapping("/room/create/add_image/{room_id}")
	public ResponseEntity<String> addImageIntoRoom(@PathVariable int room_id,
			@RequestBody List<RoomPictureDto> roomPictureDtos,
			@RequestHeader("Authorization") String authorizationHeader) {
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			String token = authorizationHeader.substring(7);
			int user_id = jwtGenerator.getUserIdFromJWT(token);
			ResponseEntity<String> response = roomService.addPictureIntoRoomForOwner(roomPictureDtos, room_id, user_id);
			return response;
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PostMapping("/room/pause_activity/{roomId}")
	public ResponseEntity<String> pauseActive(@PathVariable int roomId,
			@RequestHeader("Authorization") String authorizationHeader) {
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			String token = authorizationHeader.substring(7);
			int user_id = jwtGenerator.getUserIdFromJWT(token);
			ResponseEntity<String> response = roomService.pauseRoomActivity(roomId, user_id);
			return response;
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PostMapping("/room/resume_activity/{roomId}")
	public ResponseEntity<String> resumeActive(@PathVariable int roomId,
			@RequestHeader("Authorization") String authorizationHeader) {
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			String token = authorizationHeader.substring(7);
			int user_id = jwtGenerator.getUserIdFromJWT(token);
			ResponseEntity<String> response = roomService.resumeRoomActivity(roomId, user_id);
			return response;
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PostMapping("/room/lock/{roomId}") // cho admin
	public ResponseEntity<String> lockRoom(@PathVariable int roomId, @AuthenticationPrincipal UserDetails userDetails) {
		if (userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ADMIN"))) {
			ResponseEntity<String> response = roomService.lockRoomForAdmin(roomId);
			return response;
		} else {
			throw new UnauthorizedException("Access denied");
		}
	}

	@PostMapping("/room/unlock/{roomId}") 
	public ResponseEntity<String> unlockRoom(@PathVariable int roomId,
			@AuthenticationPrincipal UserDetails userDetails) {
		if (userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ADMIN"))) {
			ResponseEntity<String> response = roomService.unlockRoomForAdmin(roomId);
			return response;
		} else {
			throw new UnauthorizedException("Access denied");
		}
	}
	
	@PostMapping("/room/draft-contract/{roomId}") // cho landlord
	public ResponseEntity<String> draftContractForRoom(@PathVariable int roomId,
			@AuthenticationPrincipal UserDetails userDetails) {
		if (userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("LANDLORD"))) {
			ResponseEntity<String> response = roomService.draftContract(roomId);
			return response;
		} else {
			throw new UnauthorizedException("Access denied");
		}
	}
	
	@PostMapping("/room/join/{roomId}") // cho customer
	public ResponseEntity<String> joinRoom(@PathVariable int roomId,
			@AuthenticationPrincipal UserDetails userDetails) {
			ResponseEntity<String> response = roomService.joinRoom(roomId);
			return response;
	}

	@DeleteMapping("/my_room/delete/{roomId}")
	public ResponseEntity<String> deleteRoom(@PathVariable int roomId,
			@RequestHeader("Authorization") String authorizationHeader) {
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			String token = authorizationHeader.substring(7);
			int user_id = jwtGenerator.getUserIdFromJWT(token);
			ResponseEntity<String> response = roomService.deleteRoomForOwner(roomId, user_id);
			return response;
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}
