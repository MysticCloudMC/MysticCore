package net.mysticcloud.spigot.core.utils.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.mysticcloud.spigot.core.utils.AlertType;
import net.mysticcloud.spigot.core.utils.CoreUtils;

public class Event {

	EventType type;
	boolean global = true;
	List<UUID> players = new ArrayList<>();
	EventCheck check;
	String name;
	Map<String, Object> metadata = new HashMap<>();
	Map<UUID, Double> scores = new HashMap<>();

	public Event(String name, EventType type) {
		this.name = CoreUtils.colorize(name);
		this.type = type;
	}

	public boolean isGlobal() {
		return global;
	}

	public void setGlobal(boolean global) {
		this.global = global;
	}

	public void addPlayer(UUID uid) {
		if (!players.contains(uid))
			players.add(uid);
	}

	public void setPlayers(List<UUID> player) {
		this.players = player;
	}

	public List<UUID> getPlayers() {
		return players;
	}

	public void removePlayer(UUID uid) {
		if (players.contains(uid))
			players.remove(uid);
	}

	public void setEventCheck(EventCheck check) {
		this.check = check;
	}

	void setEventType(EventType type) {
		this.type = type;
	}
	public boolean hasMetadata(String key) {
		return metadata.containsKey(key);
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

	public void broadcast(String message) {
		if (isGlobal()) {
			Bukkit.broadcastMessage(CoreUtils.colorize(message));
		} else {
			for (UUID uid : players) {
				if (Bukkit.getPlayer(uid) != null)
					Bukkit.getPlayer(uid).sendMessage(CoreUtils.colorize(message));
			}
		}
	}

	public void start() {
		start("&c", "&4", "&f");
	}

	public void start(String color1, String color2, String color3) {
		String f = CoreUtils.colorize(color1 + "-=-=-=[" + color2 + name + " Event" + color1 + "]=-=-=-");
		broadcast(CoreUtils.colorize(f));
		broadcast(CoreUtils.colorize(name + color3 + " is starting."));
		if (metadata.get("DESCRIPTION") != null) {
			broadcast(CoreUtils.colorize("" + metadata.get("DESCRIPTION")));
		}
		broadcast(CoreUtils.colorize("Event Type: " + color1 + type.name()));
		if (type.equals(EventType.TIMED)) {
			try {
				broadcast(CoreUtils.colorize(
						"Duration: " + CoreUtils.formatDate((long) metadata.get("DURATION"), color3, color1)));
			} catch (NullPointerException ex) {
				CoreUtils.alert(AlertType.FATAL, "Event DURATION was not specified in the metadata");

			}
		}

		String s = color1 + "-";
		for (int a = 1; !(a >= ChatColor.stripColor(f).length() / 2); a++)
			s = s + "=-";
		broadcast(CoreUtils.colorize(s));
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

	public void overrideMetadata(String key, Object value) {

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

	public void end() {
		broadcast(CoreUtils.colorize("&c-=-=-=[&4" + name + " Event &4&lCompleted&c]=-=-=-"));
		check.end();
	}

}
