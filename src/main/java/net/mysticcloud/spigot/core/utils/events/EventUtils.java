package net.mysticcloud.spigot.core.utils.events;

import java.util.HashMap;
import java.util.Map;

public class EventUtils {

	static Map<Integer, Event> events = new HashMap<>();

	public static Event createEvent(String name, EventType type) {
		Event event = new Event(name, type);
		int id = 0;
		while (true) {
			if (!events.containsKey(id))
				break;
			id = id + 1;
		}
		events.put(id, event);
		
		return event;
	}

	public static boolean checkEvent(int id) {
		if (events.containsKey(id))
			return events.get(id).getEventCheck().check();
		else
			return false;

	}

}
