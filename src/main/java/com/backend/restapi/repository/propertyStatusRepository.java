package com.backend.restapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.backend.restapi.models.PropertyStatus;

public interface propertyStatusRepository extends JpaRepository<PropertyStatus, Integer>{

}
