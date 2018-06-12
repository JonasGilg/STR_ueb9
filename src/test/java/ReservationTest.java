import exception.CustomerAlreadyExistsException;
import exception.EventSoldOutException;
import model.Customer;
import model.Event;
import model.Reservation;
import org.junit.Before;
import org.junit.Test;
import service.EventManagementService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class ReservationTest {
	private static final String DEFAULT_TITLE = "Generic Title";
	private static final LocalDateTime DEFAULT_DATE_AND_TIME = LocalDateTime.MIN;
	private static final BigDecimal DEFAULT_TICKET_PRICE = BigDecimal.ONE;
	private static final int DEFAULT_NUMBER_OF_SEATS = 100;

	private static final String DEFAULT_NAME = "John Doe";
	private static final String DEFAULT_ADDRESS = "1st Street, 99999 Sometown";

	private EventManagementService eventManagementService;

	@Before
	public void setUp() {
		eventManagementService = new EventManagementService();
	}

	@Test
	public void testCreateTicketing() throws CustomerAlreadyExistsException, EventSoldOutException {
		Event event = eventManagementService.createEvent(DEFAULT_TITLE, DEFAULT_DATE_AND_TIME, DEFAULT_TICKET_PRICE, DEFAULT_NUMBER_OF_SEATS);
		Customer customer = eventManagementService.createCustomer(DEFAULT_NAME, DEFAULT_ADDRESS);

		Reservation reservation = eventManagementService.createReservation(event, customer, 5);
		assertEquals(event.getId(), reservation.getEventId());
		assertEquals(customer.getName(), reservation.getCustomerName());
		assertEquals(5, reservation.getNumberOfBookedTickets());
	}

	@Test
	public void testMultiReservation() throws CustomerAlreadyExistsException, EventSoldOutException {
		Event event = eventManagementService.createEvent(DEFAULT_TITLE, DEFAULT_DATE_AND_TIME, DEFAULT_TICKET_PRICE, DEFAULT_NUMBER_OF_SEATS);
		Customer customer = eventManagementService.createCustomer(DEFAULT_NAME, DEFAULT_ADDRESS);

		Reservation reservation1 = eventManagementService.createReservation(event, customer, 1);
		Reservation reservation2 = eventManagementService.createReservation(event, customer, 2);

		assertEquals(reservation1, reservation2);
		assertEquals(reservation1.getId(), reservation2.getId());
		assertEquals(reservation1.getCustomerName(), reservation2.getCustomerName());
		assertEquals(reservation1.getEventId(), reservation2.getEventId());

		assertEquals(3, reservation1.getNumberOfBookedTickets());
		assertEquals(3, reservation2.getNumberOfBookedTickets());
	}

	@Test
	public void testGetReservationFromCustomerAndEvent() throws CustomerAlreadyExistsException, EventSoldOutException {
		Event event = eventManagementService.createEvent(DEFAULT_TITLE, DEFAULT_DATE_AND_TIME, DEFAULT_TICKET_PRICE, DEFAULT_NUMBER_OF_SEATS);
		Customer customer = eventManagementService.createCustomer(DEFAULT_NAME, DEFAULT_ADDRESS);

		Reservation reservation1 = eventManagementService.createReservation(event, customer, 1);

		Reservation reservation2 = eventManagementService.getReservationTo(event, customer);

		assertEquals(reservation1.getId(), reservation2.getId());
		assertEquals(event.getId(), reservation2.getEventId());
		assertEquals(customer.getName(), reservation2.getCustomerName());
	}

	@Test(expected = EventSoldOutException.class)
	public void testFailCreateReservation() throws CustomerAlreadyExistsException, EventSoldOutException {
		Event event = eventManagementService.createEvent(DEFAULT_TITLE, DEFAULT_DATE_AND_TIME, DEFAULT_TICKET_PRICE, DEFAULT_NUMBER_OF_SEATS);
		Customer customer = eventManagementService.createCustomer(DEFAULT_NAME, DEFAULT_ADDRESS);

		eventManagementService.createReservation(event, customer, 101);
	}
}
