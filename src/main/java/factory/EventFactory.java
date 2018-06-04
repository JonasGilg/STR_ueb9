package factory;

import model.Event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class EventFactory {
	private long eventCounter = 0;

	public Event create(String title, LocalDateTime dateAndTime, BigDecimal ticketPrice, int numberOfSeats) {
		return new Event(eventCounter++, title, dateAndTime, ticketPrice, numberOfSeats);
	}
}
