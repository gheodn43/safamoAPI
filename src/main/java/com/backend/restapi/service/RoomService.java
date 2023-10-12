package com.backend.restapi.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.backend.restapi.dto.PropertyDto;
import com.backend.restapi.dto.RoomDto;
import com.backend.restapi.dto.RoomRoleDto;
import com.backend.restapi.models.GPSAddress;
import com.backend.restapi.models.PropertyEntity;
import com.backend.restapi.models.PropertyRole;
import com.backend.restapi.models.PropertyStatus;
import com.backend.restapi.models.RoomEntity;
import com.backend.restapi.models.RoomRole;
import com.backend.restapi.models.RoomStatus;
import com.backend.restapi.models.UserEntity;
import com.backend.restapi.repository.PropertyRepository;
import com.backend.restapi.repository.RoomRepository;
import com.backend.restapi.repository.RoomRoleRepository;
import com.backend.restapi.repository.RoomStatusRepository;
import com.backend.restapi.repository.UserRepository;
import com.backend.restapi.repository.propertyStatusRepository;

@Service
public class RoomService {
	private final RoomRepository roomRepository;
	private final RoomRoleRepository roomRoleRepository;
	private final RoomStatusRepository roomStatusRepository;
	private final UserRepository userRepository;
	private final propertyStatusRepository propStatusRepository;
	private final PropertyRepository propertyRepository;
    @Autowired
    public RoomService(RoomRepository roomRepository, RoomRoleRepository roomRoleRepository,
    		RoomStatusRepository roomStatusRepository, UserRepository userRepository, 
    		propertyStatusRepository propStatusRepository,
    		PropertyRepository propertyRepository
    		) {
    	this.roomRepository = roomRepository;
        this.roomRoleRepository = roomRoleRepository;
        this.roomStatusRepository = roomStatusRepository;
        this.userRepository = userRepository;
        this.propStatusRepository = propStatusRepository;
        this.propertyRepository = propertyRepository;
    }
    
    public List<RoomDto> getAllRoomsforAdmin() {
        List<RoomEntity> roomEntities = roomRepository.findAll();
        List<RoomDto> roomDtos = roomEntities.stream()
            .map(roomEntity -> {
                RoomDto dto = new RoomDto();
                dto.setRoom_id(roomEntity.getId());
                dto.setRoomName(roomEntity.getRoomName());
                dto.setDescription(roomEntity.getDescription());
                dto.setProperty_id(roomEntity.getProperty().getPropertyId());
                dto.setAcreage(roomEntity.getAcreage());
                dto.setPrice(roomEntity.getPrice());
                dto.setMaxQuantity(roomEntity.getMaxQuantity());
                dto.setStatus(roomEntity.getStatus());
                dto.setPictures(roomEntity.getPictures());
                return dto;
            })
            .collect(Collectors.toList());
        return roomDtos;
    }
    
    public ResponseEntity<String> createRoomforPropertyOwner(RoomDto roomDto, int propertyId) {
		Optional<PropertyEntity> propertyOptional = propertyRepository.findById(propertyId);
		RoomEntity roomEntity = new RoomEntity();
		PropertyStatus currentPropertyStatus = propertyOptional.get().getStatus();
		PropertyStatus statusReadyForRent = propStatusRepository.findById(3).orElse(null);
		
		RoomStatus status = roomStatusRepository.findById(1).orElse(null);
		if (status == null) {
			return new ResponseEntity<>("Không tìm thấy trạng thái hiện tại!", HttpStatus.BAD_REQUEST);
		}
		
		if (statusReadyForRent == null) {
			return new ResponseEntity<>("Không tìm thấy trạng thái hiện tại!", HttpStatus.BAD_REQUEST);
		}
		if (!statusReadyForRent.getStatus_name().equals(currentPropertyStatus.getStatus_name())) {
			return new ResponseEntity<>("Bất động sản này chưa được cấp phép cho thuê", HttpStatus.BAD_REQUEST);
		}
		roomEntity.setRoomName(roomDto.getRoomName());
		roomEntity.setDescription(roomDto.getDescription());
		roomEntity.setAcreage(roomDto.getAcreage());
		roomEntity.setPrice(roomDto.getPrice());
		roomEntity.setMaxQuantity(roomDto.getMaxQuantity());
		roomEntity.setStatus(status);
		
		
		roomRepository.save(roomEntity);
		return new ResponseEntity<>("room created successfully!", HttpStatus.OK);
	}

    
}
