package persistence;

import model.Customer;

import java.io.*;
import java.util.Collection;
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

	public Collection<Customer> findAll() {
		return dataBase.values();
	}

	public void saveToDisk(String filename) {
		try (FileOutputStream fos = new FileOutputStream(filename); ObjectOutputStream out = new ObjectOutputStream(fos)) {
			out.writeObject(dataBase);
		} catch (Exception ignored) {
		}
	}

	public void loadFromDisk(String filename) {
		try (FileInputStream fis = new FileInputStream(filename); ObjectInputStream ois = new ObjectInputStream(fis)) {
			dataBase = (Map<String, Customer>) ois.readObject();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception ignored) {
		}
	}
}
