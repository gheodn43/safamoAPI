package com.backend.restapi.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "room_pictures")
@Data
public class RoomPicture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "pictureURL", length = 255)
    private String pictureURL;

    @ManyToOne
    @JoinColumn(name = "room_id") 
    private RoomEntity roomEntity;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPictureURL() {
		return pictureURL;
	}

	public void setPictureURL(String pictureURL) {
		this.pictureURL = pictureURL;
	}

	public RoomEntity getRoomEntity() {
		return roomEntity;
	}

	public void setRoomEntity(RoomEntity roomEntity) {
		this.roomEntity = roomEntity;
	}
    
    
}


