package com.backend.restapi.dto;

import com.backend.restapi.models.RentRoom;
import com.backend.restapi.models.UserEntity;

public class RentRoomDto {
	private int id;
	private int room_id;
	private int contract_id;
	private UserEntity user;
	private int dependentId;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getRoom_id() {
		return room_id;
	}
	public void setRoom_id(int room_id) {
		this.room_id = room_id;
	}
	public int getContract_id() {
		return contract_id;
	}
	public void setContract_id(int contract_id) {
		this.contract_id = contract_id;
	}
	public UserEntity getUser() {
		return user;
	}
	public void setUser(UserEntity user) {
		this.user = user;
	}
	public int getDependentId() {
		return dependentId;
	}
	public void setDependentId(int dependentId) {
		this.dependentId = dependentId;
	}

	
	
}
