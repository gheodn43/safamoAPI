package com.backend.restapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.restapi.models.PropertyEntity;
import com.backend.restapi.models.RoomEntity;

public interface RoomRepository extends JpaRepository<RoomEntity, Integer> {
    Optional<RoomEntity> findByRoomName(String roomName);
    boolean existsByRoomName(String roomName);
	List<RoomEntity> findByProperty(PropertyEntity property);
}
