package persistence;

import model.Event;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class EventRepository {
	private Map<Long, Event> dataBase = new HashMap<>();

	public void save(Event event) {
		dataBase.put(event.getId(), event);
	}

	public Collection<Event> findAll() {
		return dataBase.values();
	}

	public Event find(long id) {
		return dataBase.get(id);
	}
}
