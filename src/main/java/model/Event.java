package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public final class Event implements Serializable {
	private long id;
	private final String title;
	private final LocalDateTime dateAndTime;
	private final BigDecimal ticketPrice;
	private final int numberOfSeats;
	private int remainingSeats;
	private String email;

	public Event(String title, LocalDateTime dateAndTime, BigDecimal ticketPrice, int numberOfSeats, String email) {
		this.title = title;
		this.dateAndTime = dateAndTime;
		this.ticketPrice = ticketPrice;
		this.numberOfSeats = numberOfSeats;
		this.remainingSeats = numberOfSeats;
		this.email = email;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) { this.id = id; }

	public String getTitle() {
		return title;
	}

	public LocalDateTime getDateAndTime() {
		return dateAndTime;
	}

	public BigDecimal getTicketPrice() {
		return ticketPrice;
	}

	public int getNumberOfSeats() {
		return numberOfSeats;
	}

	public int getRemainingSeats() {
		return remainingSeats;
	}

	public void reserveSeats(int amount) {
		remainingSeats -= amount;
	}

	public String getEmail() {
		return this.email;
	}
}
