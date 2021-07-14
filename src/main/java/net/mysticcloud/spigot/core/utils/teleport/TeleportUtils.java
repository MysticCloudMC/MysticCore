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
import org.bukkit.scheduler.BukkitTask;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.runnables.TimeoutTeleportationRequest;
import net.mysticcloud.spigot.core.utils.CoreUtils;

public class TeleportUtils {

	private static Map<UUID, UUID> teleportRequests = new HashMap<>();
	private static long requestTimeout = 90000;
	private static List<UUID> disabledRequests = new ArrayList<>();
	private static Map<UUID, Location> lastLoc = new HashMap<>();
	private static Map<UUID, TeleportWrapper> tasks = new HashMap<>();

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
		Bukkit.getScheduler().runTaskLater(Main.getPlugin(),
				new TimeoutTeleportationRequest(player.getUniqueId(), other.getUniqueId()),
				TimeUnit.SECONDS.convert(requestTimeout, TimeUnit.MILLISECONDS) * 20);
		BaseComponent[] accept = new ComponentBuilder("Type ").color(ChatColor.WHITE).append("/tpaccept")
				.color(ChatColor.GRAY).append(" or click ").color(ChatColor.WHITE).append("[Accept]")
				.color(ChatColor.GREEN).event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaccept"))
				.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
						new Text(CoreUtils.colorize(
								"&fClick to &aaccept&f the\n&fteleport request from\n&7" + player.getName()))))
				.create();
		BaseComponent[] deny = new ComponentBuilder("Type ").color(ChatColor.WHITE).append("/tpdeny")
				.color(ChatColor.GRAY).append(" or click ").color(ChatColor.WHITE).append("[Deny]").color(ChatColor.RED)
				.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpdeny"))
				.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
						new Text(CoreUtils.colorize(CoreUtils
								.colorize("&fClick to &cdeny&f the\n&fteleport request from\n&7" + player.getName())))))
				.create();

		other.sendMessage(CoreUtils.colorize(
				CoreUtils.prefixes("teleport") + "&7" + player.getName() + "&f is requesting to teleport to you."));
//		String accept = TextComponent.toLegacyText(ComponentSerializer.parse(
//				"{\"translate\":\"chat.type.text\",\"with\":[{\"color\":\"white\",\"text\":\"Type \"},{\"color\":\"gray\",\"text\":\"/tpaccept\"},{\"color\":\"white\",\"text\":\" or click \"},{\"text\":\"[Accept]\",\"color\":\"green\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tpaccept\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"Click to accept teleport from "
//						+ player.getName() + "\",\"color\":\"green\"}}}]}"));
//		String deny = TextComponent.toLegacyText(ComponentSerializer.parse(
//				"{\"translate\":\"chat.type.text\",\"with\":[{\"color\":\"white\",\"text\":\"Type \"},{\"color\":\"gray\",\"text\":\"/tpdeny\"},{\"color\":\"white\",\"text\":\" or click \"},{\"text\":\"[Deny]\",\"color\":\"red\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tpdeny\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"Click to deny teleport from "
//						+ player.getName() + "\",\"color\":\"red\"}}}]}"));

		other.spigot().sendMessage(accept);
		other.spigot().sendMessage(deny);
		other.sendMessage(CoreUtils.colorize(
				"You have " + CoreUtils.formatDate(requestTimeout, "&f", "&7") + "&f before this request times out."));
		player.sendMessage(CoreUtils.prefixes("teleport") + "Your teleport request has been sent.");
		return TeleportResult.REQUESTED;
	}

	public static void timeoutRequest(UUID player, UUID other) {
		if (teleportRequests.containsKey(other)) {
			if (teleportRequests.get(other).equals(player)) {
				teleportRequests.remove(other);
				if (Bukkit.getPlayer(player) != null) 
					Bukkit.getPlayer(player).sendMessage(
							CoreUtils.colorize(CoreUtils.prefixes("teleport") + "Your request has timed out."));
				
				if (Bukkit.getPlayer(other) != null) 
					Bukkit.getPlayer(other).sendMessage(
							CoreUtils.colorize(CoreUtils.prefixes("teleport") + "The request has timed out."));
				
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
			if (Bukkit.getPlayer(teleportRequests.get(player.getUniqueId())) != null)
				Bukkit.getPlayer(teleportRequests.get(player.getUniqueId()))
						.sendMessage(CoreUtils.colorize(CoreUtils.prefixes("teleport") + "Your request to &7"
								+ player.getName() + "&f has been denied."));
			player.sendMessage(
					CoreUtils.colorize(CoreUtils.prefixes("teleport") + "You've denied the teleport request."));
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
			BukkitTask task = Bukkit.getScheduler().runTaskLater(Main.getPlugin(), new Runnable() {

				@Override
				public void run() {
					if (player.getLocation().getBlockX() == holder.getBlockX()
							&& player.getLocation().getBlockZ() == holder.getBlockZ())
						teleportLocation(player, loc, message, overrideWait);
					else {
						player.removeMetadata("coreteleporting", Main.getPlugin());
						player.sendMessage(CoreUtils.prefixes("teleport")
								+ "You've moved you so your teleportation has been cancelled.");
					}

				}
			}, 5 * 20);

			TeleportWrapper wrap = new TeleportWrapper(player, player.getLocation(), task.getTaskId());
			tasks.put(player.getUniqueId(), wrap);
			player.setMetadata("coreteleporting", new FixedMetadataValue(Main.getPlugin(), "yup"));
			player.sendMessage(CoreUtils.prefixes("teleport") + "Teleporting in 5 seconds. Don't move.");
			return;
		}
		player.removeMetadata("coreteleporting", Main.getPlugin());
		if (message)
			player.sendMessage(CoreUtils.colorize(
					CoreUtils.prefixes("teleport") + "You've teleported to &7" + loc.getWorld().getName() + "&f, &7"
							+ loc.getBlockX() + "&f, &7" + loc.getBlockY() + "&f, &7" + loc.getBlockZ() + "&f."));
		player.teleport(loc);
	}

	public static void checkTeleport(Player player) {
		if (tasks.containsKey(player.getUniqueId())) {
			if (player.getLocation().getBlockX() == tasks.get(player.getUniqueId()).getStartingPoint().getBlockX()
					&& player.getLocation().getBlockZ() == tasks.get(player.getUniqueId()).getStartingPoint()
							.getBlockZ()) {
				return;
			} else {
				player.removeMetadata("coreteleporting", Main.getPlugin());
				Bukkit.getScheduler().cancelTask(tasks.get(player.getUniqueId()).getID());
				player.sendMessage(
						CoreUtils.prefixes("teleport") + "You've moved you so your teleportation has been cancelled.");

			}
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
						teleportPlayer(player, other, sender, overrideWait);
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

	public static void addToHistory(Player player, Location from) {
		lastLoc.put(player.getUniqueId(), from);
	}

	public static void goBack(Player player) {
		if (!lastLoc.containsKey(player.getUniqueId())) {
			player.sendMessage(CoreUtils.prefixes("teleport") + "You don't have a teleport history.");
			return;
		}
		teleport(player, lastLoc.get(player.getUniqueId()), true, false);

	}

}
