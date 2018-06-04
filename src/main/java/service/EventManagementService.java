package service;

import exception.CustomerAlreadyExistsException;
import factory.CustomerFactory;
import factory.EventFactory;
import model.Customer;
import model.Event;
import persistence.CustomerRepository;
import persistence.EventRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public class EventManagementService {
	private EventFactory eventFactory = new EventFactory();
	private EventRepository eventRepository = new EventRepository();

	private CustomerFactory customerFactory = new CustomerFactory();
	private CustomerRepository customerRepository = new CustomerRepository();

	public Event createEvent(String title, LocalDateTime dateAndTime, BigDecimal ticketPrice, int numberOfSeats) {
		Event event = eventFactory.create(title, dateAndTime, ticketPrice, numberOfSeats);
		eventRepository.save(event);
		return event;
	}

	public Collection<Event> getAllEvents() {
		return eventRepository.findAll();
	}

	public Customer createCustomer(String name, String address) throws CustomerAlreadyExistsException {
		if(customerRepository.find(name) == null) {
			Customer customer = customerFactory.create(name, address);
			customerRepository.save(customer);
			return customer;
		} else {
			throw new CustomerAlreadyExistsException();
		}
	}

	public int getRemainingSeats(long id) {
		return eventRepository.find(id).getRemainingSeats();
	}
}
