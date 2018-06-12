package service;

import exception.CustomerAlreadyExistsException;
import exception.EventSoldOutException;
import factory.CustomerFactory;
import factory.EventFactory;
import factory.ReservationFactory;
import model.Customer;
import model.Event;
import model.Reservation;
import persistence.CustomerRepository;
import persistence.EventRepository;
import persistence.ReservationRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;

public class EventManagementService {
	private EventRepository eventRepository = new EventRepository();
	private EventFactory eventFactory = new EventFactory(eventRepository);

	private CustomerRepository customerRepository = new CustomerRepository();
	private CustomerFactory customerFactory = new CustomerFactory(customerRepository);

	private ReservationRepository reservationRepository = new ReservationRepository();
	private ReservationFactory reservationFactory = new ReservationFactory(reservationRepository);

	public Event createEvent(String title, LocalDateTime dateAndTime, BigDecimal ticketPrice, int numberOfSeats) {
		return eventFactory.createEvent(title, dateAndTime, ticketPrice, numberOfSeats);
	}

	public Collection<Event> getAllEvents() {
		return eventRepository.findAll();
	}

	public Customer createCustomer(String name, String address) throws CustomerAlreadyExistsException {
		return customerFactory.createCustomer(name, address);
	}

	public Collection<Customer> getAllCustomers() {
		return customerRepository.findAll();
	}

	public int getRemainingSeats(long id) {
		return eventRepository.find(id).getRemainingSeats();
	}

	public Reservation createReservation(Event event, Customer customer, int amount) throws EventSoldOutException {
		return reservationFactory.createReservation(event, customer, amount);
	}

	public Reservation getReservationTo(Event event, Customer customer) {
		return reservationRepository.findByEventAndCustomer(event, customer).orElseGet(() -> null);
	}

	public void saveStateToDisk() {
		eventRepository.saveToDisk("events.ser");
		customerRepository.saveToDisk("customers.ser");
		reservationRepository.saveToDisk("reservations.ser");
	}

	public void loadStateFromDisk() {
		eventRepository.loadFromDisk("events.ser");
		customerRepository.loadFromDisk("customers.ser");
		reservationRepository.loadFromDisk("reservations.ser");
	}
}
