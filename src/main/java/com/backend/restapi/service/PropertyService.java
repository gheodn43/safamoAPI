package com.backend.restapi.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.backend.restapi.exception.CustomException;
import com.backend.restapi.models.GPSAddress;
import com.backend.restapi.models.PropertyEntity;
import com.backend.restapi.models.PropertyRole;
import com.backend.restapi.models.PropertyStatus;
import com.backend.restapi.models.Request;
import com.backend.restapi.models.Role;
import com.backend.restapi.models.UserEntity;
import com.backend.restapi.repository.GPSAddressRepository;
import com.backend.restapi.repository.PropertyRepository;
import com.backend.restapi.repository.PropertyRoleRepository;
import com.backend.restapi.repository.UserRepository;
import com.backend.restapi.repository.propertyStatusRepository;

@Service
public class PropertyService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PropertyRepository propertyRepository;

	@Autowired
	private propertyStatusRepository propStatusRepository;

	@Autowired
	private PropertyRoleRepository propertyRoleRepository;

	@Autowired
	private GPSAddressRepository gpsAddressRepository;

	public ResponseEntity<String> createProperty(PropertyDto propertyDto, String username) {
		Optional<UserEntity> ownerOptional = userRepository.findByUsername(username);
		GPSAddress gpsAddress = propertyDto.getGpsAddress();
		gpsAddressRepository.save(gpsAddress);
		String propertyType = propertyDto.getPropertyRole();

		if (ownerOptional.isEmpty()) {
			return new ResponseEntity<>("Không tìm thấy user", HttpStatus.BAD_REQUEST);
		}
		UserEntity owner = ownerOptional.get();

		if (propertyRepository.existsByPropertyName(propertyDto.getPropertyName())) {
			return new ResponseEntity<>("Tên đã được đăng ký", HttpStatus.BAD_REQUEST);
		}

		if (propertyRepository.existsByAddress(propertyDto.getAddress())) {
			return new ResponseEntity<>("Địa chỉ này đã tồn tại", HttpStatus.BAD_REQUEST);
		}
		PropertyStatus status = propStatusRepository.findById(1).orElse(null);
		if (status == null) {
			return new ResponseEntity<>("Không tìm thấy trạng thái hiện tại!", HttpStatus.BAD_REQUEST);
		}

		PropertyRole propertyRole = propertyRoleRepository.findByTypeName(propertyType);
		if (propertyRole == null) {
			return new ResponseEntity<>("Loại tài sản không hợp lệ", HttpStatus.BAD_REQUEST);
		}

		PropertyEntity propertyEntity = new PropertyEntity();
		propertyEntity.setPropertyName(propertyDto.getPropertyName());
		propertyEntity.setGpsAddress(gpsAddress);
		propertyEntity.setPropertyRole(propertyRole);
		propertyEntity.setAddress(propertyDto.getAddress());
		propertyEntity.setRegistrationDate(new Date());
		propertyEntity.setUnitForRent(propertyDto.getUnitForRent());
		propertyEntity.setPictureUrl(propertyDto.getPictureUrl());
		propertyEntity.setOwner(owner);
		propertyEntity.setStatus(status);

		propertyRepository.save(propertyEntity);
		return new ResponseEntity<>("Property created successfully!", HttpStatus.OK);
	}

	public PropertyDto getPropertyInfoForAdmin(int propertyId) {
		Optional<PropertyEntity> propertyOptional = propertyRepository.findById(propertyId);
		if (propertyOptional.isPresent()) {
			PropertyEntity property = propertyOptional.get();
			PropertyDto propertyInfo = new PropertyDto();
			UserEntity ownerInfo = propertyOptional.get().getOwner();
			PropertyStatus statusInfo = propertyOptional.get().getStatus();

			propertyInfo.setPropertyName(property.getPropertyName());
			propertyInfo.setAddress(property.getAddress());
			propertyInfo.setRegistrationDate(property.getRegistrationDate().toString());
			propertyInfo.setUnitForRent(property.getUnitForRent());
			propertyInfo.setPictureUrl(property.getPictureUrl());
			propertyInfo.setOwner(ownerInfo.getFullname());
			propertyInfo.setOwnerEmail(ownerInfo.getEmail());
			propertyInfo.setStatus(statusInfo.getStatus_name());
			propertyInfo.setGpsAddress(property.getGpsAddress());
			propertyInfo.setPropertyRole(property.getPropertyRole().getTypeName());
			return propertyInfo;
		} else {
			return null;
		}
	}

	public PropertyDto getPropertyInfoForCustomer(int propertyId, int user_id) {
		Optional<PropertyEntity> propertyOptional = propertyRepository.findById(propertyId);
		if (propertyOptional.isPresent()) {
			PropertyEntity property = propertyOptional.get();
			PropertyDto propertyInfo = new PropertyDto();
			UserEntity ownerInfo = propertyOptional.get().getOwner();
			PropertyStatus statusInfo = propertyOptional.get().getStatus();
			//test
			List<Role> roles = ownerInfo.getRoles();
			Boolean isCustomer = false;

			for (Role role : roles) {
			    if ("CUSTOMER".equals(role.getName())) {
			        isCustomer = true;
			        break; 
			    }
			}
			if (isCustomer && user_id != ownerInfo.getUser_id()) {
				if (!statusInfo.getStatus_name().equals("Đang hoạt động")) {
					throw new CustomException("Bạn không thể truy cập thông tin tài sản này.");
				}
			}
			propertyInfo.setPropertyName(property.getPropertyName());
			propertyInfo.setAddress(property.getAddress());
			propertyInfo.setRegistrationDate(property.getRegistrationDate().toString());
			propertyInfo.setUnitForRent(property.getUnitForRent());
			propertyInfo.setPictureUrl(property.getPictureUrl());
			propertyInfo.setOwner(ownerInfo.getFullname());
			propertyInfo.setOwnerEmail(ownerInfo.getEmail());
			propertyInfo.setStatus(statusInfo.getStatus_name());
			propertyInfo.setGpsAddress(property.getGpsAddress());
			propertyInfo.setPropertyRole(property.getPropertyRole().getTypeName());
			propertyInfo.setUser_id(ownerInfo.getUser_id());

			return propertyInfo;
		} else {
			return null;
		}
	}
	
	public List<PropertyDto> getAllPropertyForOwner(String username) {
	    Optional<UserEntity> ownerOptional = userRepository.findByUsername(username);
	    if (ownerOptional.isEmpty()) {
	        return new ArrayList<>();
	    }
	    UserEntity owner = ownerOptional.get();
	    List<PropertyEntity> propertiesOwnedByUser = propertyRepository.findByOwner(owner);

	    // Chuyển đổi PropertyEntity thành PropertyDto và thêm vào danh sách propertyDtos
	    List<PropertyDto> propertyDtos = propertiesOwnedByUser.stream()
	        .map(property -> {
	            PropertyDto propertyInfo = new PropertyDto();
	            propertyInfo.setId(property.getPropertyId());
	            propertyInfo.setPropertyName(property.getPropertyName());
	            propertyInfo.setAddress(property.getAddress());
	            propertyInfo.setRegistrationDate(property.getRegistrationDate().toString());
	            propertyInfo.setUnitForRent(property.getUnitForRent());
	            propertyInfo.setStatus(property.getStatus().getStatus_name());
	            
	            PropertyRole propertyRole = property.getPropertyRole();
	            if (propertyRole != null) {
	                propertyInfo.setPropertyRole(propertyRole.getTypeName());
	            } else {
	                propertyInfo.setPropertyRole("Không xác định");
	            }
	            return propertyInfo;
	        })
	        .collect(Collectors.toList());

	    sortByPropertyIdDescending(propertyDtos);

	    return propertyDtos;
	}

	public static void sortByPropertyIdDescending(List<PropertyDto> propertyDtos) {
	    Comparator<PropertyDto> idComparator = (propertyDtos1, propertyDtos2) -> {
	        return propertyDtos2.getId() - propertyDtos1.getId();
	    };
	    Collections.sort(propertyDtos, idComparator);
	}


}
