package com.backend.restapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.backend.restapi.models.PropertyEntity;
import com.backend.restapi.models.UserEntity;

@Repository
public interface PropertyRepository extends JpaRepository<PropertyEntity, Integer> {
    Optional<PropertyEntity> findByPropertyName(String propertyName);
    boolean existsByPropertyName(String propertyName);
    boolean existsByAddress(String address);
	List<PropertyEntity> findByOwner(UserEntity owner);
}

