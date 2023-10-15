package com.backend.restapi.service;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.backend.restapi.dto.RoomRoleDto;
import com.backend.restapi.exception.RoomRoleNotFoundException;
import com.backend.restapi.models.RoomRole;
import com.backend.restapi.repository.RoomRoleRepository;

@Service
public class RoomRoleService {
	private final RoomRoleRepository roomRoleRepository;

    @Autowired
    public RoomRoleService(RoomRoleRepository roomRoleRepository) {
        this.roomRoleRepository = roomRoleRepository;
    }

    public List<RoomRoleDto> getAll() {
        List<RoomRole> roomRoles = roomRoleRepository.findAll();
        List<RoomRoleDto> roomRoleDtos = roomRoles.stream()
            .map(roomRole -> {
                RoomRoleDto dto = new RoomRoleDto();
                dto.setRoomRole_id(roomRole.getId());
                dto.setName(roomRole.getName());
                return dto;
            })
            .collect(Collectors.toList());

        return roomRoleDtos;
    }
    
    public RoomRoleDto getRoomRoleById(Integer roomRoleId) {
        RoomRole roomRole = roomRoleRepository.findById(roomRoleId)
            .orElseThrow(() -> new RoomRoleNotFoundException("Không tìm thấy tag với ID: " + roomRoleId));
        RoomRoleDto roomRoleDto = new RoomRoleDto();
        roomRoleDto.setRoomRole_id(roomRole.getId());
        roomRoleDto.setName(roomRole.getName());

        return roomRoleDto;
    }
    
    public RoomRoleDto createRoomRole(RoomRoleDto roomRoleDto) {
        RoomRole roomRole = new RoomRole();
        roomRole.setName(roomRoleDto.getName());

        RoomRole savedRoomRole = roomRoleRepository.save(roomRole);

        RoomRoleDto savedRoomRoleDto = new RoomRoleDto();
        savedRoomRoleDto.setName(savedRoomRole.getName());
        return savedRoomRoleDto;
    }
    
    public List<RoomRoleDto> getRoomRolesByIdList(List<Integer> idList) {
        List<RoomRole> roomRoles = roomRoleRepository.findAllById(idList);
        List<RoomRoleDto> roomRoleDtos = roomRoles.stream()
            .map(roomRole -> {
                RoomRoleDto dto = new RoomRoleDto();
                dto.setRoomRole_id(roomRole.getId());
                dto.setName(roomRole.getName());
                return dto;
            })
            .collect(Collectors.toList());

        return roomRoleDtos;
    }

    public RoomRoleDto updateRoomRole(Integer roomRoleId, RoomRoleDto roomRoleDto) {
        RoomRole roomRole = roomRoleRepository.findById(roomRoleId)
            .orElseThrow(() -> new RoomRoleNotFoundException("Không tìm thấy vai trò phòng với ID: " + roomRoleId));

        roomRole.setName(roomRoleDto.getName());

        RoomRole updatedRoomRole = roomRoleRepository.save(roomRole);

        RoomRoleDto updatedRoomRoleDto = new RoomRoleDto();
        updatedRoomRoleDto.setName(updatedRoomRole.getName());

        return updatedRoomRoleDto;
    }
    
    public void deleteRoomRole(Integer roomRoleId) {
        RoomRole roomRole = roomRoleRepository.findById(roomRoleId)
            .orElseThrow(() -> new RoomRoleNotFoundException("Không tìm thấy vai trò phòng với ID: " + roomRoleId));

        roomRoleRepository.delete(roomRole);
    }


}
