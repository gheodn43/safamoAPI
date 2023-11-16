package com.backend.restapi.dto;

public class RentRoomDto2 {
private String fullname;
private String NTNS;
private String phone_number;
private String CCCD;
private String user_id;
private boolean isDependent;
public String getFullname() {
	return fullname;
}
public void setFullname(String fullname) {
	this.fullname = fullname;
}
public String getNTNS() {
	return NTNS;
}
public void setNTNS(String nTNS) {
	NTNS = nTNS;
}
public String getPhone_number() {
	return phone_number;
}
public void setPhone_number(String phone_number) {
	this.phone_number = phone_number;
}
public String getCCCD() {
	return CCCD;
}
public void setCCCD(String cCCD) {
	CCCD = cCCD;
}

public String getUser_id() {
	return user_id;
}
public void setUser_id(String user_id) {
	this.user_id = user_id;
}
public boolean isDependent() {
	return isDependent;
}
public void setDependent(boolean isDependent) {
	this.isDependent = isDependent;
}


}
