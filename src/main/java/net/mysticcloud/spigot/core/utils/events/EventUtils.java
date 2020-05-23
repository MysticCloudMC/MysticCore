package net.mysticcloud.spigot.core.utils.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventUtils {

	static Map<Integer, Event> events = new HashMap<>();
	
	static List<Integer> events__remove = new ArrayList<>();

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

	public static Map<Integer, Event> getEvents() {
		return events;
	}

	public static void addRemoveEvent(Integer key) {
		events__remove.add(key);
	}
	public static void removeEvent(Integer key) {
		events.remove(key);
	}

	public static List<Integer> removeEvents() {
		return events__remove;
	}

	public static void clearRemoveEvents() {
		events__remove.clear();
	}

}
