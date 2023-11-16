package com.backend.restapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.restapi.models.RentRoom;
import com.backend.restapi.models.Request;
import com.backend.restapi.models.RoomEntity;
import com.backend.restapi.models.UserEntity;

public interface RentRoomRepositoty  extends JpaRepository<RentRoom, Integer> {
	List<RentRoom> findByUser(UserEntity user);
	List<RentRoom> findByRoom (RoomEntity room);
}
