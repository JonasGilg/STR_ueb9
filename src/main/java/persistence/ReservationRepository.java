package persistence;

import model.Customer;
import model.Event;
import model.Reservation;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ReservationRepository {
	private Map<Long, Reservation> dataBase = new HashMap<>();

	public void save(Reservation reservation) {
		dataBase.put(reservation.getId(), reservation);
	}

	public Optional<Reservation> findByEventAndCustomer(Event event, Customer customer) {
		return dataBase.values()
				.stream()
				.filter(reservation ->
						reservation.getEventId() == event.getId() && reservation.getCustomerName().equals(customer.getName()))
				.findFirst();
	}

	public void saveToDisk(String filename) {
		try (FileOutputStream fos = new FileOutputStream(filename); ObjectOutputStream out = new ObjectOutputStream(fos)) {
			out.writeObject(dataBase);
		} catch (Exception ignored) {
		}
	}

	public void loadFromDisk(String filename) {
		try (FileInputStream fis = new FileInputStream(filename); ObjectInputStream ois = new ObjectInputStream(fis)) {
			dataBase = (Map<Long, Reservation>) ois.readObject();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception ignored) {
		}
	}
}
