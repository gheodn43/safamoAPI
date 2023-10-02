package com.backend.restapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.restapi.models.RequestStatus;

public interface RequestStatusRepository extends JpaRepository<RequestStatus, Integer>{

}
