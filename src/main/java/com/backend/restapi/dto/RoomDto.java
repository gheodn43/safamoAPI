package com.backend.restapi.dto;

import java.util.List;

import com.backend.restapi.models.RoomPicture;
import com.backend.restapi.models.RoomRole;
import com.backend.restapi.models.RoomStatus;

public class RoomDto {
	private int id;
	private int room_id;
	private int property_id;
    private String roomName;
    private String propertyName;
    private String description;
    private double acreage;
    private double price;
    private int maxQuantity;
    private List<RoomRole> tags;
    private RoomStatus status;
    private List<RoomPicture> pictures;
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
		return propertyName;
	}
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public double getAcreage() {
		return acreage;
	}
	public void setAcreage(double acreage) {
		this.acreage = acreage;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getMaxQuantity() {
		return maxQuantity;
	}
	public void setMaxQuantity(int maxQuantity) {
		this.maxQuantity = maxQuantity;
	}
	public List<RoomRole> getTags() {
		return tags;
	}
	public void setTags(List<RoomRole> tags) {
		this.tags = tags;
	}
	public RoomStatus getStatus() {
		return status;
	}
	public void setStatus(RoomStatus status) {
		this.status = status;
	}
	public List<RoomPicture> getPictures() {
		return pictures;
	}
	public void setPictures(List<RoomPicture> pictures) {
		this.pictures = pictures;
	}
    
}
