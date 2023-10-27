package com.backend.restapi.dto;
import java.util.List;
import com.backend.restapi.models.RoomEntity;
import com.backend.restapi.models.UserEntity;

public class PropertyForCusDto {
    private int id;
    private String propertyName;
    private String address;
    private int unitForRent;
    private int unitForRentIsValid;
    private String pictureUrl;
    private String status;
    private String propertyRole;
    private int ownerId;
    private String ownerName;
    private List<Integer> roomIds;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPropertyName() {
		return propertyName;
	}
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getUnitForRent() {
		return unitForRent;
	}
	public void setUnitForRent(int unitForRent) {
		this.unitForRent = unitForRent;
	}
	public String getPictureUrl() {
		return pictureUrl;
	}
	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPropertyRole() {
		return propertyRole;
	}
	public void setPropertyRole(String propertyRole) {
		this.propertyRole = propertyRole;
	}

	public int getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public List<Integer> getRoomIds() {
		return roomIds;
	}
	public void setRoomIds(List<Integer> roomIds) {
		this.roomIds = roomIds;
	}
	public int getUnitForRentIsValid() {
		return unitForRentIsValid;
	}
	public void setUnitForRentIsValid(int unitForRentIsValid) {
		this.unitForRentIsValid = unitForRentIsValid;
	}
    
    
}
