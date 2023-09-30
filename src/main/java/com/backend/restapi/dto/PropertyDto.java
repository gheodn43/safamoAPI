package com.backend.restapi.dto;

import java.util.Date;

import com.backend.restapi.models.PropertyRole;
import com.backend.restapi.models.PropertyStatus;
import com.backend.restapi.models.UserEntity;

public class PropertyDto {
	private String propertyName;
    private String address;
    private Date registrationDate;
    private int unitForRent;
    private String pictureUrl;
    private PropertyRole propertyRole;
    private UserEntity owner;
    private UserProfileUpdateDto user;
    private PropertyStatus status;
    
	public PropertyStatus getStatus() {
		return status;
	}
	public void setStatus(PropertyStatus status) {
		this.status = status;
	}
	public UserEntity getOwner() {
		return owner;
	}
	public void setOwner(UserEntity owner) {
		this.owner = owner;
	}
	
	public UserProfileUpdateDto getUser() {
		return user;
	}
	public void setUser(UserProfileUpdateDto user) {
		this.user = user;
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
	public Date getRegistrationDate() {
		return registrationDate;
	}
	public void setRegistrationDate(Date registrationDate) {
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
	public PropertyRole getPropertyRole() {
		return propertyRole;
	}
	public void setPropertyRole(PropertyRole propertyRole) {
		this.propertyRole = propertyRole;
	}

    
}
