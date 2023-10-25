package com.backend.restapi.models;

import jakarta.persistence.*;


@Entity
@Table(name = "contract")
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "party_A_id")
    private UserEntity partyA;

    @ManyToOne
    @JoinColumn(name = "party_B_id")
    private UserEntity partyB;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private RoomEntity room;
    
    @OneToOne(mappedBy = "contract") 
    private RentRoom rentRoom; 

    private String contractCreationDate;

    private String contractEndDate;
    
    private String durationTime;
    
    private String contractLink;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public UserEntity getPartyA() {
		return partyA;
	}

	public void setPartyA(UserEntity partyA) {
		this.partyA = partyA;
	}

	public UserEntity getPartyB() {
		return partyB;
	}

	public void setPartyB(UserEntity partyB) {
		this.partyB = partyB;
	}

	public RoomEntity getRoom() {
		return room;
	}

	public void setRoom(RoomEntity room) {
		this.room = room;
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

	public RentRoom getRentRoom() {
		return rentRoom;
	}

	public void setRentRoom(RentRoom rentRoom) {
		this.rentRoom = rentRoom;
	}

	public String getContractLink() {
		return contractLink;
	}

	public void setContractLink(String contractLink) {
		this.contractLink = contractLink;
	}

    
}

