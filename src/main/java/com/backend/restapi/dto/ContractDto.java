package com.backend.restapi.dto;

import com.backend.restapi.models.RoomEntity;
import com.backend.restapi.models.UserEntity;

public class ContractDto {
	private int id;
	private String contractCreationDate;
	private String contractEndDate;
	private String durationTime;
	private int room_id;
	private int partyA_id;
	private int partyB_id;
	private String contractLink;
	private String status;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContractCreationDate() {
		return contractCreationDate;
	}

	public void setContractCreationDate(String contractCreationDate) {
		this.contractCreationDate = contractCreationDate;
	}

	public String getContractEndDate() {
		return contractEndDate;
	}

	public void setContractEndDate(String contractEndDate) {
		this.contractEndDate = contractEndDate;
	}

	public String getDurationTime() {
		return durationTime;
	}

	public void setDurationTime(String durationTime) {
		this.durationTime = durationTime;
	}



	public int getRoom_id() {
		return room_id;
	}

	public void setRoom_id(int room_id) {
		this.room_id = room_id;
	}

	public int getPartyA_id() {
		return partyA_id;
	}

	public void setPartyA_id(int partyA_id) {
		this.partyA_id = partyA_id;
	}

	public int getPartyB_id() {
		return partyB_id;
	}

	public void setPartyB_id(int partyB_id) {
		this.partyB_id = partyB_id;
	}

	public String getContractLink() {
		return contractLink;
	}

	public void setContractLink(String contractLink) {
		this.contractLink = contractLink;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
