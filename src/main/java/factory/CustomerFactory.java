package factory;

import model.Customer;

public class CustomerFactory {
	public Customer create(String name, String address) {
		return new Customer(name, address);
	}
}
