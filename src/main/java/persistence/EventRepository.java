package persistence;

import model.Event;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class EventRepository {
	private Map<Long, Event> dataBase = new HashMap<>();
	private long idCounter = 0;

	public void save(Event event) {
		event.setId(idCounter++);
		dataBase.put(event.getId(), event);
	}

	public Collection<Event> findAll() {
		return dataBase.values();
	}

	public Event find(long id) {
		return dataBase.get(id);
	}

	public void saveToDisk(String filename) {
		try (FileOutputStream fos = new FileOutputStream(filename); ObjectOutputStream out = new ObjectOutputStream(fos)) {
			out.writeObject(dataBase);
			out.writeLong(idCounter);
		} catch (Exception ignored) {
		}
	}

	public void loadFromDisk(String filename) {
		try (FileInputStream fis = new FileInputStream(filename); ObjectInputStream ois = new ObjectInputStream(fis)) {
			dataBase = (Map<Long, Event>) ois.readObject();
			idCounter = ois.readLong();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception ignored) {
		}
	}
}
