import exception.CustomerAlreadyExistsException;
import model.Customer;
import org.junit.Before;
import org.junit.Test;
import service.CustomerService;

import java.util.Collection;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class CustomerTest {
	private static final String DEFAULT_NAME = "John Doe";
	private static final String DEFAULT_NAME_2 = "Jane Doe";
	private static final String DEFAULT_ADDRESS = "1st Street, 99999 Sometown";

	private CustomerService customerService;

	@Before
	public void setUp() {
		customerService = new CustomerService();
	}

	@Test
	public void testCreate() throws CustomerAlreadyExistsException {
		Customer customer = customerService.createCustomer(DEFAULT_NAME, DEFAULT_ADDRESS);

		assertEquals(DEFAULT_NAME, customer.getName());
		assertEquals(DEFAULT_ADDRESS, customer.getAddress());
	}

	@Test(expected = CustomerAlreadyExistsException.class)
	public void testNameUniqueness() throws CustomerAlreadyExistsException {
		customerService.createCustomer(DEFAULT_NAME, DEFAULT_ADDRESS);
		customerService.createCustomer(DEFAULT_NAME, DEFAULT_ADDRESS);
	}

	@Test
	public void testGetAllCustomers() throws CustomerAlreadyExistsException {
		Customer customer1 = customerService.createCustomer(DEFAULT_NAME, DEFAULT_ADDRESS);
		Customer customer2 = customerService.createCustomer(DEFAULT_NAME_2, DEFAULT_ADDRESS);

		Collection<Customer> customers = customerService.getAllCustomers();
		assertThat(customers, hasItems(customer1, customer2));
	}
}
