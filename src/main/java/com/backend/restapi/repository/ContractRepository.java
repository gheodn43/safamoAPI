package com.backend.restapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.restapi.models.Contract;

public interface ContractRepository extends JpaRepository<Contract, Integer> {
    
}