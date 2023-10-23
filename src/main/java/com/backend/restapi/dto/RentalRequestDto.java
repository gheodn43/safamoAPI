package com.backend.restapi.dto;

public class RentalRequestDto {
	int id;
	int user_id;
	int room_id;
	String requestRole;
	String username;
	String description;
	String requestStatus;
	String timeStamp;
	String duarationTime;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public int getRoom_id() {
		return room_id;
	}
	public void setRoom_id(int room_id) {
		this.room_id = room_id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getRequestStatus() {
		return requestStatus;
	}
	public void setRequestStatus(String requestStatus) {
		this.requestStatus = requestStatus;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getRequestRole() {
		return requestRole;
	}
	public void setRequestRole(String requestRole) {
		this.requestRole = requestRole;
	}
	public String getDuarationTime() {
		return duarationTime;
	}
	public void setDuarationTime(String duarationTime) {
		this.duarationTime = duarationTime;
	}
	
	
}
