package com.backend.restapi.dto;

import com.backend.restapi.models.RentRoom;
import com.backend.restapi.models.UserEntity;

public class RentRoomDto {
	private int id;
	private int user_id;
	private int room_id;
	private int contract_id;
	private int dependent_user_id;
	
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
	public int getDependent_user_id() {
		return dependent_user_id;
	}
	public void setDependent_user_id(int dependent_user_id) {
		this.dependent_user_id = dependent_user_id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

}
