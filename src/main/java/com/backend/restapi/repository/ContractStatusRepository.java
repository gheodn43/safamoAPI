package com.backend.restapi.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.restapi.models.ContractStatus;
public interface ContractStatusRepository extends JpaRepository<ContractStatus , Integer>{

}

