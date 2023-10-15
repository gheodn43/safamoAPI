package com.backend.restapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.restapi.dto.RoomRoleDto;
import com.backend.restapi.exception.DuplicateRoleException;
import com.backend.restapi.exception.PropertyRoleNotFoundException;
import com.backend.restapi.exception.UnauthorizedException;
import com.backend.restapi.models.RoomRole;
import com.backend.restapi.repository.PropertyRoleRepository;
import com.backend.restapi.repository.RoomRoleRepository;
import com.backend.restapi.service.RoomRoleService;

@RestController
@RequestMapping("/api")
public class RoomRoleController {

	@Autowired
	private PropertyRoleRepository propertyRoleRepository;
	private RoomRoleRepository roomRoleRepository;
	private RoomRoleService roomRoleService;

	@Autowired
	public RoomRoleController(RoomRoleService roomRoleService) {
		this.roomRoleService = roomRoleService;
	}

	@GetMapping("/auth/room_tag/view")
	public List<RoomRoleDto> viewRoomRoles() {
		return roomRoleService.getAll();
	}
	
	@PostMapping("/auth/room_tag/view_all_selected")
	public List<RoomRoleDto> viewAllSelected(@RequestBody List<Integer> roomRoleIds) {
		return roomRoleService.getRoomRolesByIdList(roomRoleIds);
	}

	@GetMapping("auth/room_tag/{roomRoleId}")
	public ResponseEntity<RoomRoleDto> getRoomRole(@PathVariable Integer roomRoleId,
			@AuthenticationPrincipal UserDetails userDetails) {
		if (userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ADMIN"))) {
			RoomRoleDto roomRoleDto = roomRoleService.getRoomRoleById(roomRoleId);
			return ResponseEntity.ok(roomRoleDto);
		} else {
			throw new UnauthorizedException("Access denied");
		}
	}

	@PostMapping("/room_tag/create")
	public ResponseEntity<RoomRoleDto> createRoomRole(@RequestBody RoomRoleDto roomRoleDto,
			@AuthenticationPrincipal UserDetails userDetails) {
		if (userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ADMIN"))) {
			RoomRoleDto createdRoomRole = roomRoleService.createRoomRole(roomRoleDto);
			return ResponseEntity.ok(createdRoomRole);
		} else {
			throw new UnauthorizedException("Access denied");
		}
	}

	@PutMapping("/room_tag/update/{roomRoleId}")
	public ResponseEntity<RoomRoleDto> updateRoomRole(@PathVariable Integer roomRoleId,
			@RequestBody RoomRoleDto roomRoleDto, @AuthenticationPrincipal UserDetails userDetails) {
		if (userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ADMIN"))) {
			RoomRoleDto updatedRoomRole = roomRoleService.updateRoomRole(roomRoleId, roomRoleDto);
			return ResponseEntity.ok(updatedRoomRole);
		} else {
			throw new UnauthorizedException("Access denied");
		}
	}

	@DeleteMapping("/room_tag/delete/{roomRoleId}")
	public ResponseEntity<Void> deleteRoomRole(@PathVariable Integer roomRoleId,
			@AuthenticationPrincipal UserDetails userDetails) {
		if (userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ADMIN"))) {
			roomRoleService.deleteRoomRole(roomRoleId);
			return ResponseEntity.noContent().build();
		} else {
			throw new UnauthorizedException("Access denied");
		}
	}

}
