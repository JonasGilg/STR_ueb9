import exception.CustomerAlreadyExistsException;
import exception.EventSoldOutException;
import model.Customer;
import model.Event;
import model.Reservation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import service.EventManagementService;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

public class PersistenceTest {
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
	public void testPersistence() throws CustomerAlreadyExistsException, EventSoldOutException {
		Event event1 = eventManagementService.createEvent(DEFAULT_TITLE, DEFAULT_DATE_AND_TIME, DEFAULT_TICKET_PRICE, DEFAULT_NUMBER_OF_SEATS);
		Customer customer1 = eventManagementService.createCustomer(DEFAULT_NAME, DEFAULT_ADDRESS);

		Reservation reservation1 = eventManagementService.createReservation(event1, customer1, 2);
		eventManagementService.saveStateToDisk();

		EventManagementService eventManagementService2 = new EventManagementService();
		eventManagementService2.loadStateFromDisk();

		Collection<Event> events = eventManagementService2.getAllEvents();
		Event event2 = events.iterator().next();
		assertEquals(event1.getId(), event2.getId());
		assertEquals(event1.getTitle(), event2.getTitle());
		assertEquals(event1.getDateAndTime(), event2.getDateAndTime());
		assertEquals(event1.getNumberOfSeats(), event2.getNumberOfSeats());
		assertEquals(event1.getTicketPrice(), event2.getTicketPrice());

		Collection<Customer> customers = eventManagementService2.getAllCustomers();
		Customer customer2 = customers.iterator().next();
		assertEquals(customer1.getName(), customer2.getName());
		assertEquals(customer1.getAddress(), customer2.getAddress());

		Reservation reservation2 = eventManagementService2.getReservationTo(event1, customer1);
		assertEquals(reservation1.getId(), reservation2.getId());
		assertEquals(reservation1.getCustomerName(), reservation2.getCustomerName());
		assertEquals(reservation1.getEventId(), reservation2.getEventId());
		assertEquals(reservation1.getNumberOfBookedTickets(), reservation2.getNumberOfBookedTickets());
	}
}
