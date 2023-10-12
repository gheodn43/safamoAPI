package com.backend.restapi.exception;

public class RoomRoleNotFoundException extends RuntimeException {
	public RoomRoleNotFoundException(String message) {
		super(message);
	}
}
