package factory;

import exception.EventSoldOutException;
import model.Customer;
import model.Event;
import model.Reservation;
import persistence.ReservationRepository;

import java.util.Optional;

public class ReservationFactory {
	private ReservationRepository repository;

	public ReservationFactory(ReservationRepository repository) {
		this.repository = repository;
	}

	public Reservation createReservation(Event event, Customer customer, int amount) throws EventSoldOutException {
		if(event.getRemainingSeats() < amount)
			throw new EventSoldOutException();

		event.reserveSeats(amount);

		Optional<Reservation> reservation = repository.findByEventAndCustomer(event, customer);
		reservation.ifPresent(it -> it.addTickets(amount));
		return reservation.orElseGet(() -> {
			Reservation newReservation = new Reservation(event.getId(), customer.getName(), amount);
			repository.save(newReservation);
			return newReservation;
		});
	}
}
