package model;

import java.io.Serializable;

public final class Reservation implements Serializable {
	private long id;
	private final long eventId;
	private final String customerName;
	private int numberOfBookedTickets;

	public Reservation(long eventId, String customerName, int numberOfBookedTickets) {
		this.eventId = eventId;
		this.customerName = customerName;
		this.numberOfBookedTickets = numberOfBookedTickets;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) { this.id = id; }

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
