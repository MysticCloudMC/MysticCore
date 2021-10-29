package net.mysticcloud.spigot.core.utils.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.bukkit.Bukkit;

import net.mysticcloud.spigot.core.utils.CoreUtils;

public class DebugUtils {

	static List<UUID> debuggers = new ArrayList<>();
	private static List<UUID> debuggers__remove = new ArrayList<>();

	private static boolean holidayCheck = true;

	public static void debug(String message) {
		for (UUID uid : debuggers) {
			if (Bukkit.getPlayer(uid) == null) {
				debuggers__remove.add(uid);
				continue;
			}
			Bukkit.getPlayer(uid).sendMessage(CoreUtils.prefixes("debug") + CoreUtils.colorize(message));
		}
		for (UUID uid : debuggers__remove) {
			debuggers.remove(uid);
		}
		debuggers__remove.clear();
		CoreUtils.getPlugin().getLogger().log(new LogRecord(Level.ALL, CoreUtils.prefixes("debug") + CoreUtils.colorize(message)));
	}

	public static void addDebugger(UUID uid) {
		debuggers.add(uid);
	}

	public static boolean isDebugger(UUID uid) {
		return debuggers.contains(uid);
	}

	public static void removeDebugger(UUID uid) {
		debuggers.remove(uid);
	}

	public static void holidayCheck(boolean b) {
		holidayCheck = b;
	}

	public static boolean holidayCheck() {
		return holidayCheck;
	}
}
