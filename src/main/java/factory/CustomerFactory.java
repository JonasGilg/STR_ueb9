package factory;

import exception.CustomerAlreadyExistsException;
import model.Customer;
import persistence.CustomerRepository;

public class CustomerFactory {
	private CustomerRepository repository;

	public CustomerFactory(CustomerRepository repository) {
		this.repository = repository;
	}

	public Customer createCustomer(String name, String address) throws CustomerAlreadyExistsException {
		if(repository.find(name) == null) {
			Customer customer = new Customer(name, address);
			repository.save(customer);
			return customer;
		} else {
			throw new CustomerAlreadyExistsException();
		}
	}
}
