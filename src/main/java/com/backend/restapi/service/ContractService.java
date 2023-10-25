package com.backend.restapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.backend.restapi.dto.ContractDto;
import com.backend.restapi.models.Contract;
import com.backend.restapi.models.RentRoom;
import com.backend.restapi.models.RoomEntity;
import com.backend.restapi.models.UserEntity;
import com.backend.restapi.repository.ContractRepository;
import com.backend.restapi.repository.RentRoomRepositoty;
import com.backend.restapi.repository.RoomRepository;
import com.backend.restapi.repository.UserRepository;

@Service
public class ContractService {
	private final RoomRepository roomRepository;
	private final UserRepository userRepository;
	private final RentRoomRepositoty rentRoomRepositoty;
	private final ContractRepository contractRepository;

	
	@Autowired
	public ContractService(RoomRepository roomRepository, UserRepository userRepository, 
			RentRoomRepositoty rentRoomRepositoty, ContractRepository contractRepository) {
		this.roomRepository = roomRepository;
		this.userRepository = userRepository;
		this.rentRoomRepositoty = rentRoomRepositoty;
		this.contractRepository = contractRepository;
	}
	public ResponseEntity<String> generateContract(ContractDto contractDto, int rentRoom_id) {
		RentRoom rentRoom = rentRoomRepositoty.findById(rentRoom_id).orElse(null);
		if (rentRoom != null) {
			Contract contract = new Contract();
			RoomEntity room = roomRepository.findById(contractDto.getRoom_id()).orElse(null);
			UserEntity partyA = userRepository.findById(contractDto.getPartyA_id()).orElse(null);
			UserEntity partyB = userRepository.findById(contractDto.getPartyB_id()).orElse(null);
			contract.setContractCreationDate(contractDto.getContractCreationDate());
			contract.setContractEndDate(contractDto.getContractEndDate());
			contract.setContractLink(contractDto.getContractLink());
			contract.setDurationTime(contractDto.getDurationTime());
			contract.setPartyA(partyA);
			contract.setPartyB(partyB);
			contract.setRoom(room);
			contract.setRentRoom(rentRoom);
			contractRepository.save(contract);
			return new ResponseEntity<>("Chúc mừng bạn đã thuê phòng thành công", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Không tìm thấy phòng đã cho.", HttpStatus.NOT_FOUND);
		}
	}
	
}
