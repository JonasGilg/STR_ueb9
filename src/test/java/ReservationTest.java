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
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@SuppressWarnings("Duplicates")
public class ReservationTest {
	private static final String DEFAULT_TITLE = "Generic Title";
	private static final LocalDateTime DEFAULT_DATE_AND_TIME = LocalDateTime.MIN;
	private static final BigDecimal DEFAULT_TICKET_PRICE = BigDecimal.ONE;
	private static final int DEFAULT_NUMBER_OF_SEATS = 100;
	private static final String DEFAULT_EMAIL_ADDRESS = "example@address.com";

	private static final String DEFAULT_NAME1 = "John Doe";
	private static final String DEFAULT_ADDRESS = "1st Street, 99999 Sometown";

	private static final int DEFAULT_RESERVATION_SIZE = 10;

	private Event defaultEvent1;

	private Customer defaultCustomer1;

	private ReservationService reservationService;
	private EmailService emailService;

	@Before
	public void setUp() throws CustomerAlreadyExistsException {
		BlackListService blackListService = Mockito.mock(BlackListService.class);
		when(blackListService.isBlacklisted(DEFAULT_NAME1)).thenReturn(false);


		emailService = Mockito.mock(EmailService.class);
		reservationService = new ReservationService(blackListService, emailService);
		EventService eventService = new EventService();
		CustomerService customerService = new CustomerService();

		defaultEvent1 = eventService.createEvent(DEFAULT_TITLE, DEFAULT_DATE_AND_TIME, DEFAULT_TICKET_PRICE, DEFAULT_NUMBER_OF_SEATS, DEFAULT_EMAIL_ADDRESS);

		defaultCustomer1 = customerService.createCustomer(DEFAULT_NAME1, DEFAULT_ADDRESS);
	}

	@Test
	public void testCreateReservation() throws EventSoldOutException, CustomerBlacklistedException {
		Reservation reservation = reservationService.createReservation(defaultEvent1, defaultCustomer1, DEFAULT_RESERVATION_SIZE);

		assertEquals(defaultEvent1.getId(), reservation.getEventId());
		assertEquals(defaultCustomer1.getName(), reservation.getCustomerName());
		assertEquals(DEFAULT_RESERVATION_SIZE, reservation.getNumberOfBookedTickets());
	}

	@Test
	public void testMultiReservation() throws EventSoldOutException, CustomerBlacklistedException {
		Reservation reservation1 = reservationService.createReservation(defaultEvent1, defaultCustomer1, 1);
		Reservation reservation2 = reservationService.createReservation(defaultEvent1, defaultCustomer1, 2);

		assertEquals(reservation1, reservation2);
		assertEquals(reservation1.getId(), reservation2.getId());
		assertEquals(reservation1.getCustomerName(), reservation2.getCustomerName());
		assertEquals(reservation1.getEventId(), reservation2.getEventId());

		assertEquals(3, reservation1.getNumberOfBookedTickets());
		assertEquals(3, reservation2.getNumberOfBookedTickets());
	}

	@Test
	public void testGetReservationFromCustomerAndEvent() throws EventSoldOutException, CustomerBlacklistedException {
		Reservation reservation1 = reservationService.createReservation(defaultEvent1, defaultCustomer1, 1);
		Reservation reservation2 = reservationService.getReservationTo(defaultEvent1, defaultCustomer1);

		assertEquals(reservation1.getId(), reservation2.getId());
		assertEquals(defaultEvent1.getId(), reservation2.getEventId());
		assertEquals(defaultCustomer1.getName(), reservation2.getCustomerName());
	}

	@Test(expected = EventSoldOutException.class)
	public void testFailCreateReservation() throws EventSoldOutException, CustomerBlacklistedException {
		reservationService.createReservation(defaultEvent1, defaultCustomer1, 101);
	}

	@Test(expected = CustomerBlacklistedException.class)
	public void testReservationBlacklistFail() throws EventSoldOutException, CustomerBlacklistedException {
		BlackListService blackListService = Mockito.mock(BlackListService.class);
		ReservationService reservationService3 = new ReservationService(blackListService, emailService);

		Mockito.when(blackListService.isBlacklisted(DEFAULT_NAME1)).thenReturn(true);

		reservationService3.createReservation(defaultEvent1, defaultCustomer1, 20);

		Mockito.verify(blackListService).isBlacklisted(DEFAULT_NAME1);
	}

	@Test
	public void testReservationBlacklistSucceed() throws EventSoldOutException {
		BlackListService blackListService = Mockito.mock(BlackListService.class);
		ReservationService reservationService3 = new ReservationService(blackListService, emailService);

		Mockito.when(blackListService.isBlacklisted(DEFAULT_NAME1)).thenReturn(false);

		try {
			reservationService3.createReservation(defaultEvent1, defaultCustomer1, 20);
		} catch (CustomerBlacklistedException e) {
			Assert.fail();
		}

		Mockito.verify(blackListService).isBlacklisted(DEFAULT_NAME1);
	}

	@Test
	public void testGreaterThanTenPercentEmailDelivery() throws CustomerBlacklistedException, EventSoldOutException {
		reservationService.createReservation(defaultEvent1, defaultCustomer1, defaultEvent1.getNumberOfSeats() / 10 + 1);

		verify(emailService).sendEmail(DEFAULT_EMAIL_ADDRESS);
	}

	@Test
	public void testEqualsTenPercentEmailDelivery() throws CustomerBlacklistedException, EventSoldOutException {
		reservationService.createReservation(defaultEvent1, defaultCustomer1, defaultEvent1.getNumberOfSeats() / 10);

		verify(emailService).sendEmail(DEFAULT_EMAIL_ADDRESS);
	}

	@Test
	public void testLessThanTenPercentEmailDelivery() throws CustomerBlacklistedException, EventSoldOutException {
		reservationService.createReservation(defaultEvent1, defaultCustomer1, defaultEvent1.getNumberOfSeats() / 10 - 1);

		verifyZeroInteractions(emailService);
	}
}
