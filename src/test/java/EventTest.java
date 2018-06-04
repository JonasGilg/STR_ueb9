import model.Event;
import org.junit.Before;
import org.junit.Test;
import service.EventManagementService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.*;

public class EventTest {
	private static final String DEFAULT_TITLE = "Generic Title";
	private static final LocalDateTime DEFAULT_DATE_AND_TIME = LocalDateTime.MIN;
	private static final BigDecimal DEFAULT_TICKET_PRICE = BigDecimal.ONE;
	private static final int DEFAULT_NUMBER_OF_SEATS = 100;

	private EventManagementService eventManagementService;

	@Before
	public void setUp() {
		eventManagementService = new EventManagementService();
	}

	@Test
	public void testCreation() {
		Event event = eventManagementService.createEvent(DEFAULT_TITLE, DEFAULT_DATE_AND_TIME, DEFAULT_TICKET_PRICE, DEFAULT_NUMBER_OF_SEATS);

		assertEquals(DEFAULT_TITLE, event.getTitle());
		assertEquals(DEFAULT_DATE_AND_TIME, event.getDateAndTime());
		assertEquals(DEFAULT_TICKET_PRICE, event.getTicketPrice());
		assertEquals(DEFAULT_NUMBER_OF_SEATS, event.getNumberOfSeats());
	}

	@Test
	public void testIdUniqueness() {
		Event event1 = eventManagementService.createEvent(DEFAULT_TITLE, DEFAULT_DATE_AND_TIME, DEFAULT_TICKET_PRICE, DEFAULT_NUMBER_OF_SEATS);
		Event event2 = eventManagementService.createEvent(DEFAULT_TITLE, DEFAULT_DATE_AND_TIME, DEFAULT_TICKET_PRICE, DEFAULT_NUMBER_OF_SEATS);

		assertNotEquals(event1.getId(), event2.getId());
	}

	@Test
	public void testGetAllEvents() {
		Event event1 = eventManagementService.createEvent(DEFAULT_TITLE, DEFAULT_DATE_AND_TIME, DEFAULT_TICKET_PRICE, DEFAULT_NUMBER_OF_SEATS);
		Event event2 = eventManagementService.createEvent(DEFAULT_TITLE, DEFAULT_DATE_AND_TIME, DEFAULT_TICKET_PRICE, DEFAULT_NUMBER_OF_SEATS);

		Collection<Event> events = eventManagementService.getAllEvents();
		assertThat(events, hasItems(event1, event2));
	}

	@Test
	public void testGetRemainingSeats() {
		Event event = eventManagementService.createEvent(DEFAULT_TITLE, DEFAULT_DATE_AND_TIME, DEFAULT_TICKET_PRICE, DEFAULT_NUMBER_OF_SEATS);

		eventManagementService.getRemainingSeats(event.getId());
	}
}
