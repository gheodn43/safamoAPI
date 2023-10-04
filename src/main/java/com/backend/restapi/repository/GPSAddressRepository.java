package com.backend.restapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.backend.restapi.models.GPSAddress;

@Repository
public interface GPSAddressRepository extends JpaRepository<GPSAddress, Integer> {
	
}
