package net.mysticcloud.spigot.core.utils.teleport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.runnables.TimeoutTeleportationRequest;
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
		Bukkit.broadcastMessage("" + TimeUnit.SECONDS.convert(requestTimeout, TimeUnit.MILLISECONDS));
		Bukkit.getScheduler().runTaskLater(Main.getPlugin(),
				new TimeoutTeleportationRequest(player.getUniqueId(), other.getUniqueId()),
				TimeUnit.SECONDS.convert(requestTimeout, TimeUnit.MILLISECONDS) * 20);

		other.sendMessage(CoreUtils.colorize(
				CoreUtils.prefixes("teleport") + "&7" + player.getName() + "&f is requesting to teleport to you."));
		other.sendMessage(CoreUtils.colorize("Type &7/tpaccept&f to &aaccept&f the request."));
		other.sendMessage(CoreUtils.colorize("Type &7/tpdeny&f to &cdeny&f the request."));
		other.sendMessage(CoreUtils.colorize(
				"You have " + CoreUtils.formatDate(requestTimeout, "&f", "&7") + "&f before this request times out."));
		return TeleportResult.REQUESTED;
	}

	public static void timeoutRequest(UUID player, UUID other) {
		if (teleportRequests.containsKey(other)) {
			if (teleportRequests.get(other).equals(player)) {
				teleportRequests.remove(other);
				if (Bukkit.getPlayer(player) != null) {
					Bukkit.getPlayer(player).sendMessage(
							CoreUtils.colorize(CoreUtils.prefixes("teleport") + "Your request has timed out."));
				}
			}
		}
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

	public static void teleport(Player player, Player other) {
		teleport(player, other, false);
	}

	public static void teleport(Player player, Location loc) {
		teleport(player, loc, true);
	}

	public static void teleport(Player player, Player other, boolean sender) {
		teleportPlayer(player, other, sender);
	}

	public static void teleport(Player player, Location loc, boolean message) {
		teleportLocation(player, loc, message);
	}

	public static void teleport(Player player, Player other, boolean sender, boolean overrideWait) {
		teleportPlayer(player, other, sender, overrideWait);
	}

	public static void teleport(Player player, Location loc, boolean message, boolean overrideWait) {
		teleportLocation(player, loc, message, overrideWait);
	}

	public static void teleportLocation(Player player, Location loc) {
		teleportLocation(player, loc, true);
	}

	public static void teleportLocation(Player player, Location loc, boolean message) {
		teleportLocation(player, loc, message, false);
	}

	public static void teleportLocation(Player player, Location loc, boolean message, boolean overrideWait) {
		if ((!player.hasPermission("mysticcloud.teleport.waitoverride") && !player.hasMetadata("coreteleporting"))
				&& !overrideWait) {

			Location holder = player.getLocation();
			Bukkit.getScheduler().runTaskLater(Main.getPlugin(), new Runnable() {

				@Override
				public void run() {
					if (player.getLocation().getBlockX() == holder.getBlockX()
							&& player.getLocation().getBlockZ() == holder.getBlockZ())
						teleportLocation(player, loc);
					else {
						player.removeMetadata("coreteleporting", Main.getPlugin());
						player.sendMessage(CoreUtils.prefixes("teleport")
								+ "You've moved you so your teleportation has been cancelled.");
					}

				}
			}, 10 * 20);
			player.setMetadata("coreteleporting", new FixedMetadataValue(Main.getPlugin(), "yup"));
			player.sendMessage(CoreUtils.prefixes("teleport") + "Teleporting in 10 seconds. Don't move.");
			return;
		}
		player.removeMetadata("coreteleporting", Main.getPlugin());
		if (message)
			player.sendMessage(CoreUtils.colorize(
					CoreUtils.prefixes("teleport") + "You've teleported to &7" + loc.getWorld().getName() + "&f, &7"
							+ loc.getBlockX() + "&f, &7" + loc.getBlockY() + "&f, &7" + loc.getBlockZ() + "&f."));
		player.teleport(loc);
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
		teleportPlayer(player, other, sender, false);
	}

	public static void teleportPlayer(Player player, Player other, boolean sender, boolean overrideWait) {
		if ((!player.hasPermission("mysticcloud.teleport.waitoverride") && !player.hasMetadata("coreteleporting"))
				&& !overrideWait) {
			Location holder = player.getLocation();
			Bukkit.getScheduler().runTaskLater(Main.getPlugin(), new Runnable() {

				@Override
				public void run() {
					if (player.getLocation().getBlockX() == holder.getBlockX()
							&& player.getLocation().getBlockZ() == holder.getBlockZ())
						teleportPlayer(player, other);
					else {
						player.removeMetadata("coreteleporting", Main.getPlugin());
						player.sendMessage(CoreUtils.prefixes("teleport")
								+ "You've moved you so your teleportation has been cancelled.");
					}
				}
			}, 10 * 20);
			player.setMetadata("coreteleporting", new FixedMetadataValue(Main.getPlugin(), "yup"));
			player.sendMessage(CoreUtils.prefixes("teleport") + "Teleporting in 10 seconds.");
			return;
		}
		player.removeMetadata("coreteleporting", Main.getPlugin());
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
