package com.backend.restapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.backend.restapi.models.RoomRole;

public interface RoomRoleRepository extends JpaRepository<RoomRole, Integer>{
	boolean existsByName(String name);
	Optional<RoomRole> findByName(String name);

}
