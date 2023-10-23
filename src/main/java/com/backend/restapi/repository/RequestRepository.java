package com.backend.restapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.restapi.models.PropertyEntity;
import com.backend.restapi.models.Request;
import com.backend.restapi.models.RoomEntity;
import com.backend.restapi.models.UserEntity;

@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {
	List<RoomEntity> findByRoom(RoomEntity room);
	List<Request> findByRoomAndUser(RoomEntity room, UserEntity user);
	List<Request> findByUser(UserEntity user);
}

