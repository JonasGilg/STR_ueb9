import exception.CustomerAlreadyExistsException;
import exception.CustomerBlacklistedException;
import exception.EventSoldOutException;
import model.Customer;
import model.Event;
import model.Reservation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import service.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("Duplicates")
public class ReservationTest {
	private static final String DEFAULT_TITLE = "Generic Title";
	private static final LocalDateTime DEFAULT_DATE_AND_TIME = LocalDateTime.MIN;
	private static final BigDecimal DEFAULT_TICKET_PRICE = BigDecimal.ONE;
	private static final int DEFAULT_NUMBER_OF_SEATS = 100;

	private static final String DEFAULT_NAME = "John Doe";
	private static final String DEFAULT_ADDRESS = "1st Street, 99999 Sometown";

	private ReservationService reservationService;
	private EventService eventService;
	private CustomerService customerService;
	private EmailService emailService;

	@Before
	public void setUp() {
		BlackListService blackListService = Mockito.mock(BlackListService.class);
		when(blackListService.isBlacklisted(DEFAULT_NAME)).thenReturn(false);


		emailService = Mockito.mock(EmailService.class);
		reservationService = new ReservationService(blackListService, emailService);
		eventService = new EventService();
		customerService = new CustomerService();
	}

	@Test
	public void testCreateTicketing() throws CustomerAlreadyExistsException, EventSoldOutException, CustomerBlacklistedException {
		Event event = eventService.createEvent(DEFAULT_TITLE, DEFAULT_DATE_AND_TIME, DEFAULT_TICKET_PRICE, DEFAULT_NUMBER_OF_SEATS);
		Customer customer = customerService.createCustomer(DEFAULT_NAME, DEFAULT_ADDRESS);

		Reservation reservation = reservationService.createReservation(event, customer, 5);
		assertEquals(event.getId(), reservation.getEventId());
		assertEquals(customer.getName(), reservation.getCustomerName());
		assertEquals(5, reservation.getNumberOfBookedTickets());
	}

	@Test
	public void testMultiReservation() throws CustomerAlreadyExistsException, EventSoldOutException, CustomerBlacklistedException {
		Event event = eventService.createEvent(DEFAULT_TITLE, DEFAULT_DATE_AND_TIME, DEFAULT_TICKET_PRICE, DEFAULT_NUMBER_OF_SEATS);
		Customer customer = customerService.createCustomer(DEFAULT_NAME, DEFAULT_ADDRESS);

		Reservation reservation1 = reservationService.createReservation(event, customer, 1);
		Reservation reservation2 = reservationService.createReservation(event, customer, 2);

		assertEquals(reservation1, reservation2);
		assertEquals(reservation1.getId(), reservation2.getId());
		assertEquals(reservation1.getCustomerName(), reservation2.getCustomerName());
		assertEquals(reservation1.getEventId(), reservation2.getEventId());

		assertEquals(3, reservation1.getNumberOfBookedTickets());
		assertEquals(3, reservation2.getNumberOfBookedTickets());
	}

	@Test
	public void testGetReservationFromCustomerAndEvent() throws CustomerAlreadyExistsException, EventSoldOutException, CustomerBlacklistedException {
		Event event = eventService.createEvent(DEFAULT_TITLE, DEFAULT_DATE_AND_TIME, DEFAULT_TICKET_PRICE, DEFAULT_NUMBER_OF_SEATS);
		Customer customer = customerService.createCustomer(DEFAULT_NAME, DEFAULT_ADDRESS);

		Reservation reservation1 = reservationService.createReservation(event, customer, 1);

		Reservation reservation2 = reservationService.getReservationTo(event, customer);

		assertEquals(reservation1.getId(), reservation2.getId());
		assertEquals(event.getId(), reservation2.getEventId());
		assertEquals(customer.getName(), reservation2.getCustomerName());
	}

	@Test(expected = EventSoldOutException.class)
	public void testFailCreateReservation() throws CustomerAlreadyExistsException, EventSoldOutException, CustomerBlacklistedException {
		Event event = eventService.createEvent(DEFAULT_TITLE, DEFAULT_DATE_AND_TIME, DEFAULT_TICKET_PRICE, DEFAULT_NUMBER_OF_SEATS);
		Customer customer = customerService.createCustomer(DEFAULT_NAME, DEFAULT_ADDRESS);

		reservationService.createReservation(event, customer, 101);
	}

	@Test(expected = CustomerBlacklistedException.class)
	public void testReservationBlacklistFail() throws CustomerAlreadyExistsException, EventSoldOutException, CustomerBlacklistedException {
		BlackListService blackListService = Mockito.mock(BlackListService.class);
		ReservationService reservationService3 = new ReservationService(blackListService, emailService);

		Event event = eventService.createEvent(DEFAULT_TITLE, DEFAULT_DATE_AND_TIME, DEFAULT_TICKET_PRICE, DEFAULT_NUMBER_OF_SEATS);
		Customer customer = customerService.createCustomer(DEFAULT_NAME, DEFAULT_ADDRESS);

		Mockito.when(blackListService.isBlacklisted(DEFAULT_NAME)).thenReturn(true);

		reservationService3.createReservation(event, customer, 20);

		Mockito.verify(blackListService).isBlacklisted(DEFAULT_NAME);
	}

	@Test
	public void testReservationBlacklistSucceed() throws CustomerAlreadyExistsException, EventSoldOutException {
		BlackListService blackListService = Mockito.mock(BlackListService.class);
		ReservationService reservationService3 = new ReservationService(blackListService, emailService);

		Event event = eventService.createEvent(DEFAULT_TITLE, DEFAULT_DATE_AND_TIME, DEFAULT_TICKET_PRICE, DEFAULT_NUMBER_OF_SEATS);
		Customer customer = customerService.createCustomer(DEFAULT_NAME, DEFAULT_ADDRESS);

		Mockito.when(blackListService.isBlacklisted(DEFAULT_NAME)).thenReturn(false);

		try {
			reservationService3.createReservation(event, customer, 20);
		} catch (CustomerBlacklistedException e) {
			Assert.fail();
		}

		Mockito.verify(blackListService).isBlacklisted(DEFAULT_NAME);
	}

	@Test
	public void testTenPercentEmailDelivery() throws CustomerAlreadyExistsException, CustomerBlacklistedException, EventSoldOutException {
		Event event = eventService.createEvent(DEFAULT_TITLE, DEFAULT_DATE_AND_TIME, DEFAULT_TICKET_PRICE, DEFAULT_NUMBER_OF_SEATS, "example@address.com");
		Customer customer = customerService.createCustomer(DEFAULT_NAME, DEFAULT_ADDRESS);

		reservationService.createReservation(event, customer, event.getNumberOfSeats() / 10 + 1);

		verify(emailService).sendEmail("example@address.com");

	}
}
