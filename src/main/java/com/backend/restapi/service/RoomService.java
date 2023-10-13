package com.backend.restapi.service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
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
import com.backend.restapi.exception.CustomException;
import com.backend.restapi.models.GPSAddress;
import com.backend.restapi.models.PropertyEntity;
import com.backend.restapi.models.PropertyRole;
import com.backend.restapi.models.PropertyStatus;
import com.backend.restapi.models.Role;
import com.backend.restapi.models.RoomEntity;
import com.backend.restapi.models.RoomPicture;
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
			propertyStatusRepository propStatusRepository, PropertyRepository propertyRepository) {
		this.roomRepository = roomRepository;
		this.roomRoleRepository = roomRoleRepository;
		this.roomStatusRepository = roomStatusRepository;
		this.userRepository = userRepository;
		this.propStatusRepository = propStatusRepository;
		this.propertyRepository = propertyRepository;
	}

	public List<RoomDto> getAllRoomsforAdmin() {
		List<RoomEntity> roomEntities = roomRepository.findAll();
		List<RoomDto> roomDtos = roomEntities.stream().map(roomEntity -> {
			RoomDto dto = new RoomDto();
			dto.setRoom_id(roomEntity.getId());
			dto.setRoomName(roomEntity.getRoomName());
			dto.setDescription(roomEntity.getDescription());
			dto.setProperty_id(roomEntity.getProperty().getPropertyId());
			dto.setAddress(roomEntity.getProperty().getAddress());
			dto.setPropertyName(roomEntity.getProperty().getPropertyName());
			dto.setAcreage(roomEntity.getAcreage());
			dto.setPrice(roomEntity.getPrice());
			dto.setMaxQuantity(roomEntity.getMaxQuantity());
			dto.setStatus(roomEntity.getStatus().getName());
			dto.setPictures(roomEntity.getPictures());
			BigDecimal[] newGpsAddress = { roomEntity.getProperty().getGpsAddress().getLat(),
					roomEntity.getProperty().getGpsAddress().getLng() };
			dto.setGpsAddress(newGpsAddress);
			return dto;
		}).collect(Collectors.toList());
		return roomDtos;
	}

	public RoomDto getOneforAdmin(int roomId) {
		Optional<RoomEntity> roomOptional = roomRepository.findById(roomId);
		if (roomOptional.isPresent()) {
			RoomEntity roomEntity = roomOptional.get();
			RoomDto roomDto = new RoomDto();
			roomDto.setRoom_id(roomEntity.getId());
			roomDto.setRoomName(roomEntity.getRoomName());
			roomDto.setDescription(roomEntity.getDescription());
			roomDto.setProperty_id(roomEntity.getProperty().getPropertyId());
			roomDto.setAddress(roomEntity.getProperty().getAddress());
			roomDto.setPropertyName(roomEntity.getProperty().getPropertyName());
			roomDto.setAcreage(roomEntity.getAcreage());
			roomDto.setPrice(roomEntity.getPrice());
			roomDto.setMaxQuantity(roomEntity.getMaxQuantity());
			roomDto.setStatus(roomEntity.getStatus().getName());
			roomDto.setPictures(roomEntity.getPictures());
			BigDecimal[] newGpsAddress = { roomEntity.getProperty().getGpsAddress().getLat(),
					roomEntity.getProperty().getGpsAddress().getLng() };
			roomDto.setGpsAddress(newGpsAddress);
			return roomDto;
		} else {
			return null;
		}
	}

	public RoomDto getOneforPropertyOwner(int roomId, int user_id) {
		Optional<RoomEntity> roomOptional = roomRepository.findById(roomId);
		if (roomOptional.isPresent()) {
			UserEntity ownerInfo = roomOptional.get().getProperty().getOwner();
			List<Role> roles = ownerInfo.getRoles();
			Boolean isCustomer = false;

			for (Role role : roles) {
				if ("CUSTOMER".equals(role.getName())) {
					isCustomer = true;
					break;
				}
			}
			if (isCustomer && user_id != ownerInfo.getUser_id()) {
				throw new CustomException("Bạn không thể truy cập thông tin này.");
			}
			RoomEntity roomEntity = roomOptional.get();
			RoomDto roomDto = new RoomDto();
			roomDto.setRoom_id(roomEntity.getId());
			roomDto.setRoomName(roomEntity.getRoomName());
			roomDto.setDescription(roomEntity.getDescription());
			roomDto.setProperty_id(roomEntity.getProperty().getPropertyId());
			roomDto.setAddress(roomEntity.getProperty().getAddress());
			roomDto.setPropertyName(roomEntity.getProperty().getPropertyName());
			roomDto.setAcreage(roomEntity.getAcreage());
			roomDto.setPrice(roomEntity.getPrice());
			roomDto.setMaxQuantity(roomEntity.getMaxQuantity());
			roomDto.setStatus(roomEntity.getStatus().getName());
			roomDto.setPictures(roomEntity.getPictures());
			BigDecimal[] newGpsAddress = { roomEntity.getProperty().getGpsAddress().getLat(),
					roomEntity.getProperty().getGpsAddress().getLng() };
			roomDto.setGpsAddress(newGpsAddress);
			return roomDto;
		} else {
			return null;
		}
	}

	public List<RoomDto> getAllRoomsforGuest() {
		RoomStatus status = roomStatusRepository.findById(1).orElse(null);
		List<RoomEntity> roomEntities = roomRepository.findByStatus(status);
		List<RoomDto> roomDtos = roomEntities.stream().map(roomEntity -> {
			RoomDto dto = new RoomDto();
			dto.setRoom_id(roomEntity.getId());
			dto.setRoomName(roomEntity.getRoomName());
			dto.setDescription(roomEntity.getDescription());
			dto.setProperty_id(roomEntity.getProperty().getPropertyId());
			dto.setAddress(roomEntity.getProperty().getAddress());
			dto.setPropertyName(roomEntity.getProperty().getPropertyName());
			dto.setAcreage(roomEntity.getAcreage());
			dto.setPrice(roomEntity.getPrice());
			dto.setMaxQuantity(roomEntity.getMaxQuantity());
			dto.setStatus(roomEntity.getStatus().getName());
			dto.setPictures(roomEntity.getPictures());
			BigDecimal[] newGpsAddress = { roomEntity.getProperty().getGpsAddress().getLat(),
					roomEntity.getProperty().getGpsAddress().getLng() };
			dto.setGpsAddress(newGpsAddress);
			return dto;
		}).collect(Collectors.toList());
		return roomDtos;
	}	
	
	public RoomDto getOneforGuest(int roomId) {
		Optional<RoomEntity> roomOptional = roomRepository.findById(roomId);
		if (roomOptional.isPresent()) {
			RoomEntity roomEntity = roomOptional.get();
			
			if(roomEntity.getStatus().getId()!=1) {
				return null;
			}
			RoomDto roomDto = new RoomDto();
			roomDto.setRoom_id(roomEntity.getId());
			roomDto.setRoomName(roomEntity.getRoomName());
			roomDto.setDescription(roomEntity.getDescription());
			roomDto.setProperty_id(roomEntity.getProperty().getPropertyId());
			roomDto.setAddress(roomEntity.getProperty().getAddress());
			roomDto.setPropertyName(roomEntity.getProperty().getPropertyName());
			roomDto.setAcreage(roomEntity.getAcreage());
			roomDto.setPrice(roomEntity.getPrice());
			roomDto.setMaxQuantity(roomEntity.getMaxQuantity());
			roomDto.setStatus(roomEntity.getStatus().getName());
			roomDto.setPictures(roomEntity.getPictures());
			BigDecimal[] newGpsAddress = { roomEntity.getProperty().getGpsAddress().getLat(),
					roomEntity.getProperty().getGpsAddress().getLng() };
			roomDto.setGpsAddress(newGpsAddress);
			return roomDto;
		} else {
			return null;
		}
	}
	
	public ResponseEntity<String> createRoomforPropertyOwner(RoomDto roomDto, int propertyId, int user_id) {
		Optional<PropertyEntity> propertyOptional = propertyRepository.findById(propertyId);
		if (propertyOptional.isPresent()) {
			RoomEntity roomEntity = new RoomEntity();
			UserEntity ownerInfo = propertyOptional.get().getOwner();
			PropertyStatus currentPropertyStatus = propertyOptional.get().getStatus();

			List<Role> roles = ownerInfo.getRoles();
			Boolean isCustomer = false;

			for (Role role : roles) {
				if ("CUSTOMER".equals(role.getName())) {
					isCustomer = true;
					break;
				}
			}
			if (isCustomer && user_id != ownerInfo.getUser_id()) {
				throw new CustomException("Bạn không thể truy cập thông tin này.");
			}
			RoomStatus status = roomStatusRepository.findById(1).orElse(null);
			if (status == null) {
				return new ResponseEntity<>("Không tìm thấy trạng thái hiện tại!", HttpStatus.BAD_REQUEST);
			}
			PropertyStatus statusReadyForRent = propStatusRepository.findById(3).orElse(null);
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
			roomEntity.setProperty(propertyOptional.get());
			roomRepository.save(roomEntity);
			return new ResponseEntity<>("room created successfully!", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Không tìm thấy tài sản với ID đã cho.", HttpStatus.NOT_FOUND);
		}
	}

	public ResponseEntity<String> pauseRoomActivity(int roomId, int user_id) {
		Optional<RoomEntity> roomOptional = roomRepository.findById(roomId);
		if (roomOptional.isPresent()) {
			UserEntity ownerInfo = roomOptional.get().getProperty().getOwner();
			List<Role> roles = ownerInfo.getRoles();
			Boolean isCustomer = false;

			for (Role role : roles) {
				if ("CUSTOMER".equals(role.getName())) {
					isCustomer = true;
					break;
				}
			}
			if (isCustomer && user_id != ownerInfo.getUser_id()) {
				throw new CustomException("Bạn không thể truy cập thông tin này.");
			}
			RoomEntity room = roomOptional.get();
			// Kiểm tra xem trạng thái hiện tại của phòng có phải là "Đang hoạt động" (Id:
			// 1) hay không
			if (room.getStatus().getId() == 1) {
				// Lấy trạng thái "Tạm ngừng hoạt động" (Id: 5) từ cơ sở dữ liệu
				RoomStatus pausedStatus = roomStatusRepository.findById(5).orElse(null);
				if (pausedStatus == null) {
					return new ResponseEntity<>("Không tìm thấy trạng thái 'Tạm ngừng hoạt động'",
							HttpStatus.BAD_REQUEST);
				}
				// Cập nhật trạng thái của phòng thành "Tạm ngừng hoạt động"
				room.setStatus(pausedStatus);
				roomRepository.save(room);
				return new ResponseEntity<>("Phòng đã được tạm ngừng hoạt động", HttpStatus.OK);
			} else {
				return new ResponseEntity<>("Phòng không ở trạng thái 'Đang hoạt động'", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Không tìm thấy phòng với ID đã cho", HttpStatus.NOT_FOUND);
		}
	}

	public ResponseEntity<String> resumeRoomActivity(int roomId, int user_id) {
		Optional<RoomEntity> roomOptional = roomRepository.findById(roomId);
		if (roomOptional.isPresent()) {
			UserEntity ownerInfo = roomOptional.get().getProperty().getOwner();
			List<Role> roles = ownerInfo.getRoles();
			Boolean isCustomer = false;

			for (Role role : roles) {
				if ("CUSTOMER".equals(role.getName())) {
					isCustomer = true;
					break;
				}
			}
			if (isCustomer && user_id != ownerInfo.getUser_id()) {
				throw new CustomException("Bạn không thể truy cập thông tin này.");
			}
			RoomEntity room = roomOptional.get();
			if (room.getStatus().getId() == 5) {
				RoomStatus pausedStatus = roomStatusRepository.findById(1).orElse(null);
				if (pausedStatus == null) {
					return new ResponseEntity<>("Không tìm thấy trạng thái 'Đang hoạt động'", HttpStatus.BAD_REQUEST);
				}
				room.setStatus(pausedStatus);
				roomRepository.save(room);
				return new ResponseEntity<>("Phòng đã được hoạt động hoạt động trở lại", HttpStatus.OK);
			} else {
				return new ResponseEntity<>("Phòng không ở trạng thái Ngừng hoạt động", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Không tìm thấy phòng với ID đã cho", HttpStatus.NOT_FOUND);
		}
	}

	public ResponseEntity<String> lockRoomForAdmin(int roomId) {
		Optional<RoomEntity> roomOptional = roomRepository.findById(roomId);
		if (roomOptional.isPresent()) {
			RoomEntity room = roomOptional.get();
			if (room.getStatus().getId() == 1) {
				// Lấy trạng thái "Khóa phòng" từ cơ sở dữ liệu
				RoomStatus lockedStatus = roomStatusRepository.findById(4).orElse(null);
				if (lockedStatus == null) {
					return new ResponseEntity<>("Không tìm thấy trạng thái 'Khóa phòng'", HttpStatus.BAD_REQUEST);
				}
				room.setStatus(lockedStatus);
				roomRepository.save(room);
				return new ResponseEntity<>("Phòng đã bị khóa", HttpStatus.OK);
			} else {
				return new ResponseEntity<>("Phòng không ở trạng thái 'Đang hoạt động'", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Không tìm thấy phòng với ID đã cho", HttpStatus.NOT_FOUND);
		}
	}

	public ResponseEntity<String> unlockRoomForAdmin(int roomId) {
		Optional<RoomEntity> roomOptional = roomRepository.findById(roomId);
		if (roomOptional.isPresent()) {
			RoomEntity room = roomOptional.get();
			// Kiểm tra xem phòng có đang ở trạng thái "Khóa phòng"
			if (room.getStatus().getId() == 4) {
				// Lấy trạng thái "Đang hoạt động" từ cơ sở dữ liệu
				RoomStatus activeStatus = roomStatusRepository.findById(1).orElse(null);
				if (activeStatus == null) {
					return new ResponseEntity<>("Không tìm thấy trạng thái 'Đang hoạt động'", HttpStatus.BAD_REQUEST);
				}
				// Cập nhật trạng thái của phòng thành "Đang hoạt động"
				room.setStatus(activeStatus);
				roomRepository.save(room);
				return new ResponseEntity<>("Phòng đã được mở khóa", HttpStatus.OK);
			} else {
				return new ResponseEntity<>("Phòng không ở trạng thái 'Khóa phòng'", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Không tìm thấy phòng với ID đã cho", HttpStatus.NOT_FOUND);
		}
	}

	public ResponseEntity<String> deleteRoomForOwner(int roomId, int user_id) {
	    Optional<RoomEntity> roomOptional = roomRepository.findById(roomId);
	    if (roomOptional.isPresent()) {
	        UserEntity ownerInfo = roomOptional.get().getProperty().getOwner();
	        if (user_id == ownerInfo.getUser_id()) {
	            roomRepository.delete(roomOptional.get());
	            return new ResponseEntity<>("Phòng đã được xóa", HttpStatus.OK);
	        } else {
	            throw new CustomException("Bạn không có quyền xóa phòng này.");
	        }
	    } else {
	        return new ResponseEntity<>("Không tìm thấy phòng với ID đã cho", HttpStatus.NOT_FOUND);
	    }
	}

	
	public static void sortByPropertyIdDescending(List<RoomDto> roomDtos) {
	    Comparator<RoomDto> idComparator = (roomDtos1, roomDtos2) -> {
	        return roomDtos2.getId() - roomDtos1.getId();
	    };
	    Collections.sort(roomDtos, idComparator);
	}
}
