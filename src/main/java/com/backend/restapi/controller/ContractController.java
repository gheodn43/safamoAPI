package com.backend.restapi.controller;

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

import com.backend.restapi.dto.ContractDto;
import com.backend.restapi.security.JWTGenerator;
import com.backend.restapi.service.ContractService;
import com.backend.restapi.service.RentRoomService;

@RestController
@RequestMapping("/api")
public class ContractController {
	@Autowired
	private ContractService contractService;
	private JWTGenerator jwtGenerator;
	
	
	@Autowired
	public ContractController(ContractService contractService, JWTGenerator jwtGenerator) {
		this.contractService = contractService;
		this.jwtGenerator = jwtGenerator;
	}
	
	@PostMapping("/contract/generate")
	public Integer generateContract(
			@RequestBody ContractDto contractDto) {
			Integer contractId = contractService.generateContract(contractDto);
			return contractId;
	}
	
	@GetMapping("/auth/contract")
	public ResponseEntity<ContractDto> getOneContract(@RequestParam("user_id") int userId, @RequestParam("room_id") int roomId) {
		ResponseEntity<ContractDto> contract = contractService.getContractFromUserAndRoom(userId, roomId);
			return contract;
	}

}
