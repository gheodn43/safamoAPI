package com.backend.restapi.dto;

public class RoomPictureDto {
	private int roomPicture_id;
	private String pictureURL;
	
	
	public String getPictureURL() {
		return pictureURL;
	}
	public void setPictureURL(String pictureURL) {
		this.pictureURL = pictureURL;
	}
	public int getRoomPicture_id() {
		return roomPicture_id;
	}
	public void setRoomPicture_id(int roomPicture_id) {
		this.roomPicture_id = roomPicture_id;
	}

	
	
}
