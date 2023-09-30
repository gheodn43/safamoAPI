package com.backend.restapi.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.backend.restapi.dto.PropertyDto;
import com.backend.restapi.models.PropertyEntity;
import com.backend.restapi.models.PropertyStatus;
import com.backend.restapi.models.UserEntity;
import com.backend.restapi.repository.PropertyRepository;
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

    public ResponseEntity<String> createProperty(PropertyDto propertyDto, String username) {
        // Lấy thông tin người tạo property từ username
        Optional<UserEntity> ownerOptional = userRepository.findByUsername(username);
        if (ownerOptional.isEmpty()) {
            return new ResponseEntity<>("User not found!", HttpStatus.BAD_REQUEST);
        }
        UserEntity owner = ownerOptional.get();

        // Kiểm tra xem propertyName đã tồn tại chưa
        if (propertyRepository.existsByPropertyName(propertyDto.getPropertyName())) {
            return new ResponseEntity<>("Property with this name already exists!", HttpStatus.BAD_REQUEST);
        }

        // Kiểm tra xem address đã tồn tại chưa
        if (propertyRepository.existsByAddress(propertyDto.getAddress())) {
            return new ResponseEntity<>("Property with this address already exists!", HttpStatus.BAD_REQUEST);
        }

        PropertyStatus status = propStatusRepository.findById(1).orElse(null);

        if (status == null) {
            return new ResponseEntity<>("Property status not found!", HttpStatus.BAD_REQUEST);
        }

        // Tạo đối tượng Property từ PropertyDto
        PropertyEntity propertyEntity = new PropertyEntity();
        propertyEntity.setPropertyName(propertyDto.getPropertyName());
        propertyEntity.setAddress(propertyDto.getAddress());
        propertyEntity.setRegistrationDate(new Date()); // Tự động sinh ngày đăng ký
        propertyEntity.setUnitForRent(propertyDto.getUnitForRent());
        propertyEntity.setPictureUrl(propertyDto.getPictureUrl());

        // Thiết lập owner và status cho property mới
        propertyEntity.setOwner(owner);
        propertyEntity.setStatus(status);

        propertyRepository.save(propertyEntity);
        return new ResponseEntity<>("Property created successfully!", HttpStatus.OK);
    }

    
    public PropertyDto getPropertyInfo(int propertyId) {
        // Lấy thông tin property từ repository
        Optional<PropertyEntity> propertyOptional = propertyRepository.findById(propertyId);

        if (propertyOptional.isPresent()) {
            PropertyEntity property = propertyOptional.get();
            PropertyDto propertyInfo = new PropertyDto();
            propertyInfo.setPropertyName(property.getPropertyName());
            propertyInfo.setAddress(property.getAddress());
            propertyInfo.setRegistrationDate(property.getRegistrationDate());
            propertyInfo.setUnitForRent(property.getUnitForRent());
            propertyInfo.setPictureUrl(property.getPictureUrl());
            
            // Lấy thông tin owner từ property và set cho propertyInfo
            UserEntity ownerInfo = new UserEntity();
            ownerInfo.setUsername(property.getOwner().getUsername());
            ownerInfo.setFullname(property.getOwner().getFullname());

            propertyInfo.setOwner(ownerInfo);
            
            // Lấy thông tin propertyStatus từ property và set cho propertyInfo
            PropertyStatus statusInfo = new PropertyStatus();
            statusInfo.setStatus_name(property.getStatus().getStatus_name());
            // Set thêm thông tin statusInfo khác nếu cần
            
            propertyInfo.setStatus(statusInfo);
            
            return propertyInfo;
        } else {
            return null;
        }
    }
}

