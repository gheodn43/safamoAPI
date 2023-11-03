package com.backend.restapi.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.backend.restapi.dto.ContractDto;
import com.backend.restapi.models.Contract;
import com.backend.restapi.models.ContractStatus;
import com.backend.restapi.models.RentRoom;
import com.backend.restapi.models.RoomEntity;
import com.backend.restapi.models.UserEntity;
import com.backend.restapi.repository.ContractRepository;
import com.backend.restapi.repository.ContractStatusRepository;
import com.backend.restapi.repository.RentRoomRepositoty;
import com.backend.restapi.repository.RoomRepository;
import com.backend.restapi.repository.UserRepository;

@Service
public class ContractService {
	private final RoomRepository roomRepository;
	private final UserRepository userRepository;
	private final RentRoomRepositoty rentRoomRepositoty;
	private final ContractRepository contractRepository;
	private final ContractStatusRepository contractStatusRepository;

	@Autowired
	public ContractService(RoomRepository roomRepository, UserRepository userRepository,
			RentRoomRepositoty rentRoomRepositoty, ContractRepository contractRepository,
			ContractStatusRepository contractStatusRepository) {
		this.roomRepository = roomRepository;
		this.userRepository = userRepository;
		this.rentRoomRepositoty = rentRoomRepositoty;
		this.contractRepository = contractRepository;
		this.contractStatusRepository = contractStatusRepository;
	}

	public ResponseEntity<Integer> generateContract(ContractDto contractDto) {
		Contract contract = new Contract();
		RoomEntity room = roomRepository.findById(contractDto.getRoom_id()).orElse(null);
		UserEntity partyA = userRepository.findById(contractDto.getPartyA_id()).orElse(null);
		UserEntity partyB = userRepository.findById(contractDto.getPartyB_id()).orElse(null);
		ContractStatus status = contractStatusRepository.findById(1).orElse(null);
		contract.setContractCreationDate(contractDto.getContractCreationDate());
		contract.setContractEndDate(contractDto.getContractEndDate());
		contract.setContractLink(contractDto.getContractLink());
		contract.setDurationTime(contractDto.getDurationTime());
		contract.setPartyA(partyA);
		contract.setPartyB(partyB);
		contract.setRoom(room);
		contract.setContractStatus(status);
		contractRepository.save(contract);
		return new ResponseEntity<>(contract.getId(), HttpStatus.OK);
	}

	public ResponseEntity<ContractDto> getContractFromUserAndRoom(int user_id, int room_id) {
		ContractDto contractDto = new ContractDto();
		UserEntity user = userRepository.findById(user_id).orElse(null);
		RoomEntity room = roomRepository.findById(room_id).orElse(null);
		ContractStatus status = contractStatusRepository.findById(1).orElse(null);
		List<Contract>  contracts = new ArrayList<>();
		contracts = contractRepository.findByContractStatus(status);
		for (Contract contract : contracts) {
			if(contract.getPartyB().equals(user) && contract.getRoom().equals(room)) {
				contractDto.setContractLink(contract.getContractLink());
				contractDto.setDurationTime(contract.getDurationTime());
				contractDto.setRoom_id(contract.getRoom().getId());
			}
		}
		return new ResponseEntity<>(contractDto, HttpStatus.OK);
	}

}
