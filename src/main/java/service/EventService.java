package service;

import factory.EventFactory;
import model.Event;
import persistence.EventRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;

public class EventService {
	private EventRepository eventRepository = new EventRepository();
	private EventFactory eventFactory = new EventFactory(eventRepository);

	public Event createEvent(String title, LocalDateTime dateAndTime, BigDecimal ticketPrice, int numberOfSeats, String email) {
		return eventFactory.createEvent(title, dateAndTime, ticketPrice, numberOfSeats, email);
	}

	public Collection<Event> getAllEvents() {
		return eventRepository.findAll();
	}

	public int getRemainingSeats(long id) {
		return eventRepository.find(id).getRemainingSeats();
	}

	void saveToDisk(String path) {
		eventRepository.saveToDisk(path);
	}

	void loadFromDisk(String path) {
		eventRepository.loadFromDisk(path);
	}
}
