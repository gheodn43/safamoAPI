package com.backend.restapi.dto;

import java.util.List;

public class RoomPrivateOutputDto {
	private int room_id;
	private int property_id;
    private String roomName;
    private String PropertyName;
    private double price;
    private List<String> tags;
    private String status;
    private List<String> picturesURL;
	public int getRoom_id() {
		return room_id;
	}
	public void setRoom_id(int room_id) {
		this.room_id = room_id;
	}
	public int getProperty_id() {
		return property_id;
	}
	public void setProperty_id(int property_id) {
		this.property_id = property_id;
	}
	public String getRoomName() {
		return roomName;
	}
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	public String getPropertyName() {
		return PropertyName;
	}
	public void setPropertyName(String propertyName) {
		PropertyName = propertyName;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public List<String> getTags() {
		return tags;
	}
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<String> getPicturesURL() {
		return picturesURL;
	}
	public void setPicturesURL(List<String> picturesURL) {
		this.picturesURL = picturesURL;
	}
    
    
}
