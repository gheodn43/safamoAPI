package com.backend.restapi.dto;

public class OutputOwnerOfProperty {
	private int owner_id;
	private String owner_username;
	private String owner_fullname;
	private String owner_email;
	private String owner_NTNS;
	public int getOwner_id() {
		return owner_id;
	}
	public void setOwner_id(int owner_id) {
		this.owner_id = owner_id;
	}
	public String getOwner_username() {
		return owner_username;
	}
	public void setOwner_username(String owner_username) {
		this.owner_username = owner_username;
	}
	public String getOwner_fullname() {
		return owner_fullname;
	}
	public void setOwner_fullname(String owner_fullname) {
		this.owner_fullname = owner_fullname;
	}
	public String getOwner_email() {
		return owner_email;
	}
	public void setOwner_email(String owner_email) {
		this.owner_email = owner_email;
	}
	public String getOwner_NTNS() {
		return owner_NTNS;
	}
	public void setOwner_NTNS(String owner_NTNS) {
		this.owner_NTNS = owner_NTNS;
	}
	
	
}
