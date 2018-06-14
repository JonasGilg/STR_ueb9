package service;

import exception.CustomerAlreadyExistsException;
import factory.CustomerFactory;
import model.Customer;
import persistence.CustomerRepository;

import java.util.Collection;

public class CustomerService {
	private CustomerRepository customerRepository = new CustomerRepository();
	private CustomerFactory customerFactory = new CustomerFactory(customerRepository);

	public Customer createCustomer(String name, String address) throws CustomerAlreadyExistsException {
		return customerFactory.createCustomer(name, address);
	}

	public Collection<Customer> getAllCustomers() {
		return customerRepository.findAll();
	}

	public void saveToDisk(String path) {
		customerRepository.saveToDisk(path);
	}

	public void loadFromDisk(String path) {
		customerRepository.loadFromDisk(path);
	}
}
