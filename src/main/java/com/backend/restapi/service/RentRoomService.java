package com.backend.restapi.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.backend.restapi.dto.RentRoomDto;
import com.backend.restapi.dto.RentRoomDto2;
import com.backend.restapi.dto.RoomDto;
import com.backend.restapi.exception.CustomException;
import com.backend.restapi.models.Contract;
import com.backend.restapi.models.PropertyEntity;
import com.backend.restapi.models.PropertyStatus;
import com.backend.restapi.models.RentRoom;
import com.backend.restapi.models.Role;
import com.backend.restapi.models.RoomEntity;
import com.backend.restapi.models.RoomPicture;
import com.backend.restapi.models.RoomRole;
import com.backend.restapi.models.RoomStatus;
import com.backend.restapi.models.UserEntity;
import com.backend.restapi.repository.ContractRepository;
import com.backend.restapi.repository.RentRoomRepositoty;
import com.backend.restapi.repository.RoomPictureRepository;
import com.backend.restapi.repository.RoomRepository;
import com.backend.restapi.repository.UserRepository;

@Service
public class RentRoomService {
	private final RoomRepository roomRepository;
	private final UserRepository userRepository;
	private final RentRoomRepositoty rentRoomRepositoty;
	private final ContractRepository contaContractRepository;
	private final RoomPictureRepository roomPictureRepository;

	@Autowired
	public RentRoomService(RoomRepository roomRepository, UserRepository userRepository,
			RentRoomRepositoty rentRoomRepositoty, ContractRepository contaContractRepository,
			RoomPictureRepository roomPictureRepository) {
		this.roomRepository = roomRepository;
		this.userRepository = userRepository;
		this.rentRoomRepositoty = rentRoomRepositoty;
		this.contaContractRepository = contaContractRepository;
		this.roomPictureRepository = roomPictureRepository;
	}

	public ResponseEntity<Integer> joinRoom(RentRoomDto dto) {
		RoomEntity room = roomRepository.findById(dto.getRoom_id()).orElse(null);
		UserEntity user = userRepository.findById(dto.getUser_id()).orElse(null);
		Contract contract = contaContractRepository.findById(dto.getContract_id()).orElse(null);
		RentRoom rentRoom = new RentRoom();
		rentRoom.setRoom(room);
		rentRoom.setUser(user);
		rentRoom.setDependent_user(user);
		rentRoom.setContract(contract);
		rentRoomRepositoty.save(rentRoom);
		return new ResponseEntity<>(rentRoom.getId(), HttpStatus.OK);
	}

	public ResponseEntity<String> joinRoomWithDependent(int user_id, int rentRoom_id) {
		RentRoom rentRoomDependent = rentRoomRepositoty.findById(rentRoom_id).orElse(null);
		UserEntity user = userRepository.findById(user_id).orElse(null);
		if (rentRoomDependent != null && user != null) {
			RentRoom rentRoom = new RentRoom();
			rentRoom.setRoom(rentRoomDependent.getRoom());
			rentRoom.setUser(user);
			rentRoom.setDependent_user(rentRoomDependent.getDependent_user());
			rentRoomRepositoty.save(rentRoom);
			return new ResponseEntity<>("Chúc mừng bạn đã gia nhap phòng thành công", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Không tìm thấy phòng với ID đã cho.", HttpStatus.NOT_FOUND);
		}
	}

	public List<RoomDto> getRoomsRented(int user_id) {
		UserEntity owner = userRepository.findById(user_id).orElse(null);
		if (owner == null) {
			return new ArrayList<>();
		}
		List<RentRoom> rentRooms = rentRoomRepositoty.findByUser(owner);

		List<RoomDto> roomDtos = new ArrayList<>();

		for (RentRoom rentRoom : rentRooms) {
			int room_id = rentRoom.getRoom().getId();
			RoomEntity roomEntity = roomRepository.findById(room_id).orElse(null);
			if (roomEntity != null) {
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
				dto.setGpsAddress(roomEntity.getProperty().getGpsAddress());
				List<Integer> tagsId = new ArrayList<>();
				List<String> tagsName = new ArrayList<>();
				List<String> pictureURLs = new ArrayList<>();
				List<RoomPicture> roomPictures = roomPictureRepository.findByRoomEntity(roomEntity);
				List<RoomRole> tags = roomEntity.getRoles();
				for (RoomRole tag : tags) {
					int TagId = tag.getId();
					String TagName = tag.getName();
					tagsId.add(TagId);
					tagsName.add(TagName);
				}
				for (RoomPicture picture : roomPictures) {
					String pictureURL = picture.getPictureURL();
					pictureURLs.add(pictureURL);
				}
				dto.setTagIds(tagsId);
				dto.setTags(tagsName);
				dto.setPicturesURL(pictureURLs);
				roomDtos.add(dto);
			}
		}

		return roomDtos;
	}

	public List<RoomDto> getAllRentRooms(int user_id) {
	    UserEntity owner = userRepository.findById(user_id).orElse(null);
	    if (owner == null) {
	        return new ArrayList<>();
	    }
	    List<RentRoom> rentRooms = rentRoomRepositoty.findByUser(owner);
	    List<RoomDto> roomDtos = new ArrayList<>();
	    for (RentRoom rentRoom : rentRooms) {
	    	RoomDto dto = new RoomDto();
	    	RoomEntity roomEntity = roomRepository.findById(rentRoom.getRoom().getId()).orElse(null);
	    	dto.setRoom_id(roomEntity.getId());
			dto.setRoomName(roomEntity.getRoomName());
			dto.setPropertyName(roomEntity.getProperty().getPropertyName());
			dto.setPrice(roomEntity.getPrice());
			List<Integer> tagsId = new ArrayList<>();
			List<String> tagsName = new ArrayList<>();
			List<String> pictureURLs = new ArrayList<>();
			List<RoomPicture> roomPictures = roomPictureRepository.findByRoomEntity(roomEntity);
			List<RoomRole> tags = roomEntity.getRoles();
			for (RoomRole tag : tags) {
				int TagId = tag.getId();
				String TagName = tag.getName();
				tagsId.add(TagId);
				tagsName.add(TagName);
			}
			for (RoomPicture picture : roomPictures) {
				String pictureURL = picture.getPictureURL();
				pictureURLs.add(pictureURL);
			}
			dto.setTagIds(tagsId);
			dto.setTags(tagsName);
			dto.setPicturesURL(pictureURLs);
			
			roomDtos.add(dto);
	    }
	    return roomDtos;
	}
	
	public RoomDto getRoomRented(int room_id) {
		RoomEntity roomEntity = roomRepository.findById(room_id).orElse(null);
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
		dto.setGpsAddress(roomEntity.getProperty().getGpsAddress());
		List<Integer> tagsId = new ArrayList<>();
		List<String> tagsName = new ArrayList<>();
		List<String> pictureURLs = new ArrayList<>();
		List<RoomPicture> roomPictures = roomPictureRepository.findByRoomEntity(roomEntity);
		List<RoomRole> tags = roomEntity.getRoles();
		for (RoomRole tag : tags) {
			int TagId = tag.getId();
			String TagName = tag.getName();
			tagsId.add(TagId);
			tagsName.add(TagName);
		}
		for (RoomPicture picture : roomPictures) {
			String pictureURL = picture.getPictureURL();
			pictureURLs.add(pictureURL);
		}
		dto.setTagIds(tagsId);
		dto.setTags(tagsName);
		dto.setPicturesURL(pictureURLs);
		return dto;
	}
	
//	public RentRoomDto getone(int room_id) {
//		RoomEntity roomEntity = roomRepository.findById(room_id).orElse(null);
//		RentRoom rentRoom = rentRoomRepositoty.findByRoom(roomEntity);
//		if(rentRoom == null) return null;
//		RentRoomDto dto = new RentRoomDto();
//		dto.setUser_id(rentRoom.getUser().getUser_id());
//		dto.setRoom_id(room_id);
//		dto.setContract_id(rentRoom.getContract().getId());
//		dto.setDependent_user_id(rentRoom.getDependent_user().getUser_id());
//		
//		return dto;
//	}
	
	public List<RentRoomDto2> getRentRooms(int room_id) {
		RoomEntity roomEntity = roomRepository.findById(room_id).orElse(null);
		List<RentRoom> rentRooms = rentRoomRepositoty.findByRoom(roomEntity);
		
		List<RentRoomDto2> dtos = new ArrayList<RentRoomDto2>();
		for (RentRoom rentRoom : rentRooms) {
			RentRoomDto2 dto = new RentRoomDto2();
			UserEntity user = rentRoom.getUser();
			dto.setFullname(user.getFullname());
			dto.setNTNS(user.getBirthdate());
			dto.setCCCD(user.getCccd());
			dto.setPhone_number(user.getPhone_number());
			if(rentRoom.getDependent_user().equals(user)) 
				dto.setDependent(true);
			else dto.setDependent(false);
			dtos.add(dto);
		}
		return dtos;
	}

}
