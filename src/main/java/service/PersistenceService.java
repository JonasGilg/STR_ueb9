package service;

public class PersistenceService {


	private EventService eventService;
	private CustomerService customerService;
	private ReservationService reservationService;

	public PersistenceService(EventService eventService, CustomerService customerService, ReservationService reservationService) {
		this.eventService = eventService;
		this.customerService = customerService;
		this.reservationService = reservationService;
	}

	public void saveStateToDisk() {
		eventService.saveToDisk("events.ser");
		customerService.saveToDisk("customers.ser");
		reservationService.saveToDisk("reservations.ser");
	}

	public void loadStateFromDisk() {
		eventService.loadFromDisk("events.ser");
		customerService.loadFromDisk("customers.ser");
		reservationService.loadFromDisk("reservations.ser");
	}
}
