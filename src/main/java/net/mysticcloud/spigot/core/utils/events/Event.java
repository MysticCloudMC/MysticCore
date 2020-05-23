package net.mysticcloud.spigot.core.utils.events;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.mysticcloud.spigot.core.utils.CoreUtils;

public class Event {

	EventType type;
	EventCheck check;
	String name;
	Map<String, Object> metadata = new HashMap<>();
	Map<UUID, Double> scores = new HashMap();

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

	public LinkedHashMap<UUID, Double> sortScores() {
		List<Entry<UUID, Double>> list = new LinkedList<Entry<UUID, Double>>(scores.entrySet());
		Collections.sort(list, new Comparator<Entry<UUID, Double>>() {

			@Override
			public int compare(Entry<UUID, Double> o1, Entry<UUID, Double> o2) {
				// TODO Auto-generated method stub
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});

		LinkedHashMap<UUID, Double> temp = new LinkedHashMap<>();
		for (Entry<UUID, Double> aa : list) {
			temp.put(aa.getKey(), aa.getValue());
		}

		return temp;

	}

	public void start() {
		String f = CoreUtils.colorize("&c-=-=-=[&4" + name + " Event&c]=-=-=-");
		Bukkit.broadcastMessage(CoreUtils.colorize(f));
		Bukkit.broadcastMessage(CoreUtils.colorize(name + "&f is starting."));
		if(metadata.get("DESCRIPTION") != null) {
			Bukkit.broadcastMessage(CoreUtils.colorize("Description: " + metadata.get("DESCRIPTION")));
		}
		Bukkit.broadcastMessage(CoreUtils.colorize("Event Type: &c" + type.name()));
		if (type.equals(EventType.TIMED)) {
			Bukkit.broadcastMessage(CoreUtils
					.colorize("Duration: " + CoreUtils.formatDate((long) metadata.get("DURATION"), "&f", "&c")));
		}

		String s = "&c";
		for (int a = 1; !(a >= f.length() / 2); a++)
			s = s + "-=";
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

	public Map<UUID, Double> getScores() {
		return scores;
	}

	public void score(Player player, double score) {
		scores.put(player.getUniqueId(),
				(scores.containsKey(player.getUniqueId()) ? scores.get(player.getUniqueId()) + score : score));
	}

}
