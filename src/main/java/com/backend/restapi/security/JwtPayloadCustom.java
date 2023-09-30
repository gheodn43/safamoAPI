package com.backend.restapi.security;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;




@JsonSerialize
public class JwtPayloadCustom {
	  private int userId;
	  private String username;
	  private List<String> roles;
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public List<String> getRoles() {
		return roles;
	}
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

}
