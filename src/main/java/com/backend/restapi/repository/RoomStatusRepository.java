package com.backend.restapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.backend.restapi.models.RoomStatus;

public interface RoomStatusRepository extends JpaRepository<RoomStatus, Integer>{

}
