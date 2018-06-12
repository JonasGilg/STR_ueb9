package model;

import java.io.Serializable;

public class Reservation implements Serializable {
	private long id;
	private long eventId;
	private String customerName;
	private int numberOfBookedTickets;

	private static int idCounter = 0;

	public Reservation(long eventId, String customerName, int numberOfBookedTickets) {
		id = idCounter++;
		this.eventId = eventId;
		this.customerName = customerName;
		this.numberOfBookedTickets = numberOfBookedTickets;
	}

	public long getId() {
		return id;
	}

	public long getEventId() {
		return eventId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public int getNumberOfBookedTickets() {
		return numberOfBookedTickets;
	}

	public void addTickets(int amount) {
		numberOfBookedTickets += amount;
	}
}
