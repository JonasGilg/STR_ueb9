import exception.CustomerAlreadyExistsException;
import exception.CustomerBlacklistedException;
import exception.EventSoldOutException;
import model.Customer;
import model.Event;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import service.BlackListService;
import service.CustomerService;
import service.EventService;
import service.ReservationService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class EventTest {
	private static final String DEFAULT_TITLE = "Generic Title";
	private static final LocalDateTime DEFAULT_DATE_AND_TIME = LocalDateTime.MIN;
	private static final BigDecimal DEFAULT_TICKET_PRICE = BigDecimal.ONE;
	private static final int DEFAULT_NUMBER_OF_SEATS = 100;

	private static final String DEFAULT_NAME = "John Doe";
	private static final String DEFAULT_ADDRESS = "1st Street, 99999 Sometown";

	private EventService eventService;

	@Before
	public void setUp() {
		eventService = new EventService();
	}

	@Test
	public void testCreation() {
		Event event = eventService.createEvent(DEFAULT_TITLE, DEFAULT_DATE_AND_TIME, DEFAULT_TICKET_PRICE, DEFAULT_NUMBER_OF_SEATS);

		assertEquals(DEFAULT_TITLE, event.getTitle());
		assertEquals(DEFAULT_DATE_AND_TIME, event.getDateAndTime());
		assertEquals(DEFAULT_TICKET_PRICE, event.getTicketPrice());
		assertEquals(DEFAULT_NUMBER_OF_SEATS, event.getNumberOfSeats());
	}

	@Test
	public void testIdUniqueness() {
		Event event1 = eventService.createEvent(DEFAULT_TITLE, DEFAULT_DATE_AND_TIME, DEFAULT_TICKET_PRICE, DEFAULT_NUMBER_OF_SEATS);
		Event event2 = eventService.createEvent(DEFAULT_TITLE, DEFAULT_DATE_AND_TIME, DEFAULT_TICKET_PRICE, DEFAULT_NUMBER_OF_SEATS);

		assertNotEquals(event1.getId(), event2.getId());
	}

	@Test
	public void testGetAllEvents() {
		Event event1 = eventService.createEvent(DEFAULT_TITLE, DEFAULT_DATE_AND_TIME, DEFAULT_TICKET_PRICE, DEFAULT_NUMBER_OF_SEATS);
		Event event2 = eventService.createEvent(DEFAULT_TITLE, DEFAULT_DATE_AND_TIME, DEFAULT_TICKET_PRICE, DEFAULT_NUMBER_OF_SEATS);

		Collection<Event> events = eventService.getAllEvents();
		assertThat(events, hasItems(event1, event2));
	}

	@Test
	public void testGetRemainingSeats() throws CustomerAlreadyExistsException, EventSoldOutException, CustomerBlacklistedException {
		CustomerService customerService = new CustomerService();

		BlackListService blackListService = Mockito.mock(BlackListService.class);
		when(blackListService.isBlacklisted(DEFAULT_NAME)).thenReturn(false);

		ReservationService reservationService = new ReservationService(blackListService, null);

		Event event = eventService.createEvent(DEFAULT_TITLE, DEFAULT_DATE_AND_TIME, DEFAULT_TICKET_PRICE, DEFAULT_NUMBER_OF_SEATS);
		Customer customer = customerService.createCustomer(DEFAULT_NAME, DEFAULT_ADDRESS);

		int remainingSeats = eventService.getRemainingSeats(event.getId());
		reservationService.createReservation(event, customer, 2);

		assertEquals(remainingSeats - 2, eventService.getRemainingSeats(event.getId()));
	}
}
