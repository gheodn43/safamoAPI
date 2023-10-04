package com.backend.restapi.dto;

import java.util.Date;

import com.backend.restapi.models.GPSAddress;
import com.backend.restapi.models.PropertyRole;

public class PropertyDto {
	private int id;
	private String propertyName;
    private String address;
    private String registrationDate;
    private int unitForRent;
    private String pictureUrl;
    private String propertyRole;
    private String  owner;
    private String ownerEmail;
    private String status;
    private GPSAddress gpsAddress;
    
    
    
    public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	private int user_id;

	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public GPSAddress getGpsAddress() {
		return gpsAddress;
	}
	public void setGpsAddress(GPSAddress gpsAddress) {
		this.gpsAddress = gpsAddress;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getOwnerEmail() {
		return ownerEmail;
	}
	public void setOwnerEmail(String ownerEmail) {
		this.ownerEmail = ownerEmail;
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
	public String getRegistrationDate() {
		return registrationDate;
	}
	public void setRegistrationDate(String registrationDate) {
		this.registrationDate = registrationDate;
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
	public String getPropertyRole() {
		return propertyRole;
	}
	public void setPropertyRole(String propertyRole) {
		this.propertyRole = propertyRole;
	}

    
}
