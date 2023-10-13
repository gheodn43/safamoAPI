package com.backend.restapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.restapi.models.RoomEntity;
import com.backend.restapi.models.RoomPicture;


public interface RoomPictureRepository extends JpaRepository<RoomPicture, Integer>{
	List<RoomPicture> findByRoomEntity(RoomEntity roomEntity);
}
