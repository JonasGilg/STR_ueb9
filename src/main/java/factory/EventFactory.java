package factory;

import model.Event;
import persistence.EventRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class EventFactory {
	private EventRepository repository;

	public EventFactory(EventRepository repository) {
		this.repository = repository;
	}

	public Event createEvent(String title, LocalDateTime dateAndTime, BigDecimal ticketPrice, int numberOfSeats) {
		Event event = new Event(title, dateAndTime, ticketPrice, numberOfSeats);
		repository.save(event);
		return event;
	}

	public Event createEvent(String title, LocalDateTime dateAndTime, BigDecimal ticketPrice, int numberOfSeats, String email) {
		Event event = new Event(title, dateAndTime, ticketPrice, numberOfSeats, email);
		repository.save(event);
		return event;
	}
}
