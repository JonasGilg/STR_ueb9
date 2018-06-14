import exception.CustomerAlreadyExistsException;
import exception.CustomerBlacklistedException;
import exception.EventSoldOutException;
import model.Customer;
import model.Event;
import model.Reservation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import service.*;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class PersistenceTest {
	private static final String DEFAULT_TITLE = "Generic Title";
	private static final LocalDateTime DEFAULT_DATE_AND_TIME = LocalDateTime.MIN;
	private static final BigDecimal DEFAULT_TICKET_PRICE = BigDecimal.ONE;
	private static final int DEFAULT_NUMBER_OF_SEATS = 100;

	private static final String DEFAULT_NAME = "John Doe";
	private static final String DEFAULT_ADDRESS = "1st Street, 99999 Sometown";

	private EventService eventService1;
	private CustomerService customerService1;
	private ReservationService reservationService1;
	private PersistenceService persistenceService1;

	private EventService eventService2;
	private CustomerService customerService2;
	private ReservationService reservationService2;
	private PersistenceService persistenceService2;

	@Before
	public void setUp() {
		BlackListService blackListService = Mockito.mock(BlackListService.class);
		when(blackListService.isBlacklisted(DEFAULT_NAME)).thenReturn(false);

		eventService1 = new EventService();
		customerService1 = new CustomerService();
		reservationService1 = new ReservationService(blackListService,null);
		persistenceService1 = new PersistenceService(eventService1, customerService1, reservationService1);

		eventService2 = new EventService();
		customerService2 = new CustomerService();
		reservationService2 = new ReservationService(blackListService, null);
		persistenceService2 = new PersistenceService(eventService2, customerService2, reservationService2);
	}

	@After
	public void cleanUp() {
		try {
			File events = new File("events.ser");
			events.delete();

			File customers = new File("customers.ser");
			customers.delete();

			File reservations = new File("reservations.ser");
			reservations.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testPersistence() throws CustomerAlreadyExistsException, EventSoldOutException, CustomerBlacklistedException {
		Event event1 = eventService1.createEvent(DEFAULT_TITLE, DEFAULT_DATE_AND_TIME, DEFAULT_TICKET_PRICE, DEFAULT_NUMBER_OF_SEATS);
		Customer customer1 = customerService1.createCustomer(DEFAULT_NAME, DEFAULT_ADDRESS);

		Reservation reservation1 = reservationService1.createReservation(event1, customer1, 2);
		persistenceService1.saveStateToDisk();

		persistenceService2.loadStateFromDisk();
		Collection<Event> events = eventService2.getAllEvents();
		Event event2 = events.iterator().next();
		assertEquals(event1.getId(), event2.getId());
		assertEquals(event1.getTitle(), event2.getTitle());
		assertEquals(event1.getDateAndTime(), event2.getDateAndTime());
		assertEquals(event1.getNumberOfSeats(), event2.getNumberOfSeats());
		assertEquals(event1.getTicketPrice(), event2.getTicketPrice());

		Collection<Customer> customers = customerService2.getAllCustomers();
		Customer customer2 = customers.iterator().next();
		assertEquals(customer1.getName(), customer2.getName());
		assertEquals(customer1.getAddress(), customer2.getAddress());

		Reservation reservation2 = reservationService2.getReservationTo(event1, customer1);
		assertEquals(reservation1.getId(), reservation2.getId());
		assertEquals(reservation1.getCustomerName(), reservation2.getCustomerName());
		assertEquals(reservation1.getEventId(), reservation2.getEventId());
		assertEquals(reservation1.getNumberOfBookedTickets(), reservation2.getNumberOfBookedTickets());
	}
}
