package service;

import exception.CustomerBlacklistedException;
import exception.EventSoldOutException;
import factory.ReservationFactory;
import model.Customer;
import model.Event;
import model.Reservation;
import persistence.ReservationRepository;

public class ReservationService {
	private ReservationRepository reservationRepository = new ReservationRepository();
	private ReservationFactory reservationFactory = new ReservationFactory(reservationRepository);

	private BlackListService blackListService;

	public ReservationService(BlackListService blackListService) {
		this.blackListService = blackListService;
	}

	public Reservation createReservation(Event event, Customer customer, int amount) throws EventSoldOutException, CustomerBlacklistedException {
		if(blackListService.isBlacklisted(customer.getName()))
			throw new CustomerBlacklistedException();

		return reservationFactory.createReservation(event, customer, amount);
	}

	public Reservation getReservationTo(Event event, Customer customer) {
		return reservationRepository.findByEventAndCustomer(event, customer).orElseGet(() -> null);
	}

	public void saveToDisk(String path) {
		reservationRepository.saveToDisk(path);
	}

	public void loadFromDisk(String path) {
		reservationRepository.loadFromDisk(path);
	}
}