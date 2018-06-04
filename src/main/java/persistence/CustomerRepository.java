package persistence;

import model.Customer;

import java.util.HashMap;
import java.util.Map;

public final class CustomerRepository {
	private Map<String , Customer> dataBase = new HashMap<>();

	public void save(Customer customer) {
		dataBase.put(customer.getName(), customer);
	}

	public Customer find(String name) {
		return dataBase.get(name);
	}
}
