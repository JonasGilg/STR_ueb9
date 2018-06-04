package model;

public class Ticketing {
	private long id;
	private Event event;
	private Customer customer;
	private int numberOfBookedTickets;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public int getNumberOfBookedTickets() {
		return numberOfBookedTickets;
	}

	public void setNumberOfBookedTickets(int numberOfBookedTickets) {
		this.numberOfBookedTickets = numberOfBookedTickets;
	}
}
