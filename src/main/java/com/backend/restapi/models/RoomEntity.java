package com.backend.restapi.models;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "rooms")
@Data
public class RoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "roomName", columnDefinition = "NVARCHAR(255)")
    private String roomName;
    private double acreage;
    private double price;
    @Column(name = "description", columnDefinition = "NVARCHAR(255)")
    private String description;
    private int maxQuantity;
    
    @ManyToMany
    @JoinTable(
        name = "room_entity_roles",
        joinColumns = @JoinColumn(name = "room_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<RoomRole> roles;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private RoomStatus status;

    @OneToMany(mappedBy = "roomEntity", cascade = CascadeType.ALL)
    private List<RoomPicture> pictures;

    @ManyToOne
    @JoinColumn(name = "property_id")
    private PropertyEntity property;
    
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<Request> requests;
    
    @Column(name = "starRating", columnDefinition = "DECIMAL(3, 1)")
    private double starRating;
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getRoomName() {
		return roomName;
	}
	public void setRoomName(String roomName) {
		this.roomName = roomName;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getMaxQuantity() {
		return maxQuantity;
	}
	public void setMaxQuantity(int maxQuantity) {
		this.maxQuantity = maxQuantity;
	}
	public List<RoomRole> getRoles() {
		return roles;
	}
	public void setRoles(List<RoomRole> roles) {
		this.roles = roles;
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
	public PropertyEntity getProperty() {
		return property;
	}
	public void setProperty(PropertyEntity property) {
		this.property = property;
	}
	public List<Request> getRequests() {
		return requests;
	}
	public void setRequests(List<Request> requests) {
		this.requests = requests;
	}
	public double getStarRating() {
		return starRating;
	}
	public void setStarRating(double starRating) {
		this.starRating = starRating;
	}
	
	
    
}
