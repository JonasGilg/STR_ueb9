import exception.CustomerAlreadyExistsException;
import model.Customer;
import org.junit.Before;
import org.junit.Test;
import service.EventManagementService;

import static org.junit.Assert.assertEquals;

public class CustomerTest {
	private static final String DEFAULT_NAME = "John Doe";
	private static final String DEFAULT_ADDRESS = "1st Street, 99999 Sometown";

	private EventManagementService eventManagementService;

	@Before
	public void setUp() {
		eventManagementService = new EventManagementService();
	}

	@Test
	public void testCreate() throws CustomerAlreadyExistsException {
		Customer customer = eventManagementService.createCustomer(DEFAULT_NAME, DEFAULT_ADDRESS);

		assertEquals(DEFAULT_NAME, customer.getName());
		assertEquals(DEFAULT_ADDRESS, customer.getAddress());
	}

	@Test(expected = CustomerAlreadyExistsException.class)
	public void testNameUniqueness() throws CustomerAlreadyExistsException {
		eventManagementService.createCustomer(DEFAULT_NAME, DEFAULT_ADDRESS);
		eventManagementService.createCustomer(DEFAULT_NAME, DEFAULT_ADDRESS);
	}
}
