package com.backend.restapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.restapi.models.Contract;
import com.backend.restapi.models.ContractStatus;
import com.backend.restapi.models.RoomEntity;
import com.backend.restapi.models.UserEntity;

public interface ContractRepository extends JpaRepository<Contract, Integer> {
	List<Contract> findByContractStatus(ContractStatus contractStatus);
}