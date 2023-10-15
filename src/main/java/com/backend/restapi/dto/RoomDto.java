package com.backend.restapi.dto;

import java.math.BigDecimal;
import java.util.List;

import com.backend.restapi.models.GPSAddress;
import com.backend.restapi.models.RoomPicture;
import com.backend.restapi.models.RoomRole;
import com.backend.restapi.models.RoomStatus;

public class RoomDto {
	private int id;
	private int room_id;
	private int property_id;
    private String roomName;
    private String propertyName;
    private String address;
    private String description;
    private double acreage;
    private double price;
    private int maxQuantity;
    private List<Integer> tagIds;
    private List<String> tags;
    private String status;
    private List<String> picturesURL;
    private GPSAddress gpsAddress;
    
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	
	public GPSAddress getGpsAddress() {
		return gpsAddress;
	}
	public void setGpsAddress(GPSAddress gpsAddress) {
		this.gpsAddress = gpsAddress;
	}
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
	
	public List<Integer> getTagIds() {
		return tagIds;
	}
	public void setTagIds(List<Integer> tagIds) {
		this.tagIds = tagIds;
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
	public List<String> getTags() {
		return tags;
	}
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
    
}
