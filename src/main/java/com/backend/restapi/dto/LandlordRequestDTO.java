package com.backend.restapi.dto;

import org.springframework.stereotype.Component;

import com.backend.restapi.models.UserEntity;
@Component
public class LandlordRequestDTO {
	private String description;

	private UserEntity user;
	
	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
