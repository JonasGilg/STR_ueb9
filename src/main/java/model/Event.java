package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Event implements Serializable {
	private long id;
	private String title;
	private LocalDateTime dateAndTime;
	private BigDecimal ticketPrice;
	private int numberOfSeats;
	private int remainingSeats;

	private static int idCounter = 0;

	public Event(String title, LocalDateTime dateAndTime, BigDecimal ticketPrice, int numberOfSeats) {
		this.id = idCounter++;
		this.title = title;
		this.dateAndTime = dateAndTime;
		this.ticketPrice = ticketPrice;
		this.numberOfSeats = numberOfSeats;
		this.remainingSeats = numberOfSeats;
	}

	public long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public LocalDateTime getDateAndTime() {
		return dateAndTime;
	}

	public void setDateAndTime(LocalDateTime dateAndTime) {
		this.dateAndTime = dateAndTime;
	}

	public BigDecimal getTicketPrice() {
		return ticketPrice;
	}

	public void setTicketPrice(BigDecimal ticketPrice) {
		this.ticketPrice = ticketPrice;
	}

	public int getNumberOfSeats() {
		return numberOfSeats;
	}

	public void setNumberOfSeats(int numberOfSeats) {
		this.numberOfSeats = numberOfSeats;
	}

	public int getRemainingSeats() {
		return remainingSeats;
	}

	public void reserveSeats(int amount) {
		remainingSeats -= amount;
	}
}
