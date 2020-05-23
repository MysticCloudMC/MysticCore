package net.mysticcloud.spigot.core.utils.events;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;

import net.mysticcloud.spigot.core.utils.CoreUtils;

public class Event {

	EventType type;
	EventCheck check;
	String name;
	Map<String, Object> metadata = new HashMap<>();

	public Event(String name, EventType type) {
		this.name = CoreUtils.colorize(name);
		this.type = type;
	}

	public void setEventCheck(EventCheck check) {
		this.check = check;
	}

	void setEventType(EventType type) {
		this.type = type;
	}

	void setName(String name) {
		this.name = CoreUtils.colorize(name);
	}

	public EventCheck getEventCheck() {
		return check;
	}

	public EventType getEventType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public void start() {
		String f = CoreUtils.colorize("&c-=-=-=[&4"+ name + " Event&c]=-=-=-");
		Bukkit.broadcastMessage(CoreUtils.colorize(f));
		Bukkit.broadcastMessage(CoreUtils.colorize(name + "&f is starting."));
		Bukkit.broadcastMessage(CoreUtils.colorize("Event Type: &c" + type.name()));
		if(type.equals(EventType.TIMED)) {
			Bukkit.broadcastMessage(CoreUtils.colorize("Duration: " + CoreUtils.formatDate((long)metadata.get("DURATION"), "&f", "&c")));
		}
		
		String s = "&c";
		for(int a=1;!(a>=f.length()/2);a++)
			s = s +"-=";
		Bukkit.broadcastMessage(CoreUtils.colorize(s));
		check.start();
	}

	public void setMetadata(String key, Object value) {
		while (true) {
			if (metadata.containsKey(key))
				key = key + "_1";
			break;
		}

		metadata.put(key, value);
	}

	public Object getMetadata(String key) {
		return metadata.get(key);
	}

}
