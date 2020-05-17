package net.mysticcloud.spigot.core.utils.teleport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.mysticcloud.spigot.core.utils.CoreUtils;

public class TeleportUtils {

	private static Map<UUID, UUID> teleportRequests = new HashMap<>();
	private static long requestTimeout = 90000;
	private static List<UUID> disabledRequests = new ArrayList<>();

	public static TeleportResult requestTeleport(Player player, Player other) {
		if (teleportRequests.containsKey(other.getUniqueId())) {
			if (teleportRequests.get(other.getUniqueId()).equals(player.getUniqueId())) {
				player.sendMessage(
						CoreUtils.prefixes("teleport") + "You've already requested to teleport to that player.");
				return TeleportResult.ALREADY_REQUESTED;
			}
		}
		if (disabledRequests.contains(other.getUniqueId())) {
			player.sendMessage(CoreUtils.prefixes("teleport") + "That player has teleportation requests disabled.");
			return TeleportResult.REQUESTS_DISABLED;
		}
		teleportRequests.put(other.getUniqueId(), player.getUniqueId());
		// TODO put a timer here
		other.sendMessage(CoreUtils.colorize(
				CoreUtils.prefixes("teleport") + "&7" + player.getName() + "&f is requesting to teleport to you."));
		other.sendMessage(CoreUtils.colorize("Type &7/tpaccept&f to &aaccept&f the request."));
		other.sendMessage(CoreUtils.colorize("Type &7/tpdeny&f to &cdeny&f the request."));
		other.sendMessage(CoreUtils.colorize(
				"You have " + CoreUtils.formatDate(requestTimeout, "&f", "&7") + "&f before this request times out."));
		return TeleportResult.REQUESTED;
	}

	public static TeleportResult acceptTeleport(Player player) {
		if (teleportRequests.containsKey(player.getUniqueId())) {
			if (Bukkit.getPlayer(teleportRequests.get(player.getUniqueId())) != null) {
				teleportPlayer(Bukkit.getPlayer(teleportRequests.get(player.getUniqueId())), player);
				teleportRequests.remove(player.getUniqueId());
				return TeleportResult.ACCEPTED;
			} else {
				player.sendMessage(CoreUtils.prefixes("teleport") + "That player isn't online.");
				teleportRequests.remove(player.getUniqueId());
				return TeleportResult.REQUESTER_OFFLINE;
			}
		} else {
			player.sendMessage(CoreUtils.prefixes("teleport") + "You don't have any requests.");
			return TeleportResult.NO_REQUESTS;
		}
	}

	public static TeleportResult denyTeleport(Player player) {
		if (teleportRequests.containsKey(player.getUniqueId())) {
			teleportRequests.remove(player.getUniqueId());
			return TeleportResult.DENIED;
		} else {
			player.sendMessage(CoreUtils.prefixes("teleport") + "You don't have any requests.");
			return TeleportResult.NO_REQUESTS;
		}
	}

	public static void teleportPlayer(String sender, Player player, Player other) {

		player.sendMessage(CoreUtils.colorize(CoreUtils.prefixes("teleport") + "You've been teleported to &7"
				+ other.getName() + "&f by &7" + sender + "&f."));
		teleportPlayer(player, other, true);
	}

	public static void teleportPlayer(Player player, Player other) {
		teleportPlayer(player, other, false);
	}

	public static void teleportPlayer(Player player, Player other, boolean sender) {

		player.teleport(other);
		if (!sender)
			player.sendMessage(CoreUtils
					.colorize(CoreUtils.prefixes("teleport") + "You've teleported to &7" + other.getName() + "&f."));
		if (other.hasPermission("mysticcloud.admin"))
			other.sendMessage(CoreUtils
					.colorize(CoreUtils.prefixes("teleport") + "&7" + player.getName() + "&f has teleported to you"));
	}

	public static boolean disableTeleportRequests(Player player) {
		if (disabledRequests.contains(player.getUniqueId())) {
			disabledRequests.remove(player.getUniqueId());
		}
		return disabledRequests.contains(player.getUniqueId());
	}

	public static boolean enableTeleportRequests(Player player) {
		if (!disabledRequests.contains(player.getUniqueId())) {
			disabledRequests.add(player.getUniqueId());
		}
		return disabledRequests.contains(player.getUniqueId());
	}

	public static boolean toggleTeleportRequests(Player player) {
		if (disabledRequests.contains(player.getUniqueId())) {
			disabledRequests.remove(player.getUniqueId());
		} else {
			disabledRequests.add(player.getUniqueId());
		}
		return disabledRequests.contains(player.getUniqueId());
	}

}
