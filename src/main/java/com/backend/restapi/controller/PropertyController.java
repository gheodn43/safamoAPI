package com.backend.restapi.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.restapi.dto.PropertyDto;
import com.backend.restapi.dto.RequestDto;
import com.backend.restapi.exception.CustomException;
import com.backend.restapi.exception.DuplicateRoleException;
import com.backend.restapi.exception.UnauthorizedException;
import com.backend.restapi.exception.UserNotFoundException;
import com.backend.restapi.models.PropertyEntity;
import com.backend.restapi.security.JWTGenerator;
import com.backend.restapi.service.PropertyService;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {

	@Autowired
	private PropertyService propertyService;
	private JWTGenerator jwtGenerator;

	@Autowired
	public PropertyController(PropertyService propertyService, JWTGenerator jwtGenerator) {
		this.propertyService = propertyService;
		this.jwtGenerator = jwtGenerator;
	}

	@PostMapping("/create")
	public ResponseEntity<String> createProperty(@RequestBody PropertyDto propertyDto, Principal principal) {
		String username = principal.getName();
		ResponseEntity<String> response = propertyService.createProperty(propertyDto, username);
		return response;
	}

	// cho admin
	@GetMapping("/{propertyId}")
	public ResponseEntity<PropertyDto> getPropertyInfoForAdmin(@PathVariable int propertyId) {
		PropertyDto propertyInfo = propertyService.getPropertyInfoForAdmin(propertyId);

		if (propertyInfo != null) {
			return new ResponseEntity<>(propertyInfo, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	// cho customer - trả về lỗi nếu có gắng truy cập các property bị khóa hoặc đang
	// tạm ngừng hoạt động
	@GetMapping("/preview/{propertyId}")
	public ResponseEntity<PropertyDto> getPropertyInfoForCustomer(@PathVariable int propertyId,
			@RequestHeader("Authorization") String authorizationHeader) {
		PropertyDto propertyInfo = null;

		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			// Lấy token bằng cách loại bỏ phần "Bearer " từ header
			String token = authorizationHeader.substring(7);
			int user_id = jwtGenerator.getUserIdFromJWT(token);
			try {
				propertyInfo = propertyService.getPropertyInfoForCustomer(propertyId, user_id);
				if (propertyInfo != null) {
					return new ResponseEntity<>(propertyInfo, HttpStatus.OK);
				} else {
					return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
				}
			} catch (CustomException e) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}

		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

	}
	@PostMapping("/edit/{propertyId}")
    public ResponseEntity<String> editProperty(@PathVariable int propertyId, @RequestBody PropertyDto updatedPropertyDto,
    		@RequestHeader("Authorization") String authorizationHeader) {
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			String token = authorizationHeader.substring(7);
			int user_id = jwtGenerator.getUserIdFromJWT(token);
            ResponseEntity<String> response = propertyService.editProperty(propertyId,user_id, updatedPropertyDto);
            return response;
        } else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
    }
	@GetMapping("/view_all") //cho admin
	public ResponseEntity<List<PropertyDto>> getAllProperties(@AuthenticationPrincipal UserDetails userDetails) {
		if (userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ADMIN"))) {
			List<PropertyDto> propertyDtos = propertyService.getAllPropertyForAdmin();
			return ResponseEntity.ok(propertyDtos);
		} else {
			throw new UnauthorizedException("Access denied");
		}
	}

	@GetMapping("/my_properties")
	public ResponseEntity<List<PropertyDto>> getAllRequests(
			@RequestHeader("Authorization") String authorizationHeader) {
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			String token = authorizationHeader.substring(7);
			String username = jwtGenerator.getUsernameFromJWT(token);
			List<PropertyDto> propertyDtos = propertyService.getAllPropertyForOwner(username);
			return ResponseEntity.ok(propertyDtos);
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

	}

	@PostMapping("/denie/{propertyId}")
	public ResponseEntity<String> denieProperty(@PathVariable int propertyId,
			@AuthenticationPrincipal UserDetails userDetails) {
		if (userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ADMIN"))) {
			ResponseEntity<String> response = propertyService.adminDeniedProperty(propertyId);
			return response;
		} else {
			throw new UnauthorizedException("Access denied");
		}
	}
	
	@PostMapping("/accept/{propertyId}")
	public ResponseEntity<String> acceptProperty(@PathVariable int propertyId,
			@AuthenticationPrincipal UserDetails userDetails) {
		if (userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ADMIN"))) {
			ResponseEntity<String> response = propertyService.adminAcceptProperty(propertyId);
			return response;
		} else {
			throw new UnauthorizedException("Access denied");
		}
	}
	
	@PostMapping("/block/{propertyId}")
	public ResponseEntity<String> blockProperty(@PathVariable int propertyId,
			@AuthenticationPrincipal UserDetails userDetails) {
		if (userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ADMIN"))) {
			ResponseEntity<String> response = propertyService.adminBlockProperty(propertyId);
			return response;
		} else {
			throw new UnauthorizedException("Access denied");
		}
	}
	
	@PostMapping("/unblock/{propertyId}")
	public ResponseEntity<String> unblockProperty(@PathVariable int propertyId,
			@AuthenticationPrincipal UserDetails userDetails) {
		if (userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ADMIN"))) {
			ResponseEntity<String> response = propertyService.adminUnblockProperty(propertyId);
			return response;
		} else {
			throw new UnauthorizedException("Access denied");
		}
	}

}
