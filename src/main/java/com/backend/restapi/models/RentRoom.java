package com.backend.restapi.models;

import jakarta.persistence.*;

@Entity
@Table(name = "rent_room")
public class RentRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "dependent__id")
    private RentRoom dependent;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private RoomEntity room;

    @ManyToOne
    @JoinColumn(name = "contract_id")
    private Contract contract;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public RentRoom getDependent() {
		return dependent;
	}

	public void setDependent(RentRoom dependent) {
		this.dependent = dependent;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public RoomEntity getRoom() {
		return room;
	}

	public void setRoom(RoomEntity room) {
		this.room = room;
	}

	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

    
}
