package com.backend.restapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.restapi.models.Request;

@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {
}

