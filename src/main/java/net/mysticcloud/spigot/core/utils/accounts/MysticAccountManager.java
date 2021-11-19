package net.mysticcloud.spigot.core.utils.accounts;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.admin.DebugUtils;

public class MysticAccountManager {

	static Map<UUID, MysticPlayer> mplayers = new HashMap<>();

	public static MysticPlayer getMysticPlayer(UUID uid) {
		if (mplayers.containsKey(uid)) {
			return mplayers.get(uid);
		}

		if (Bukkit.getPlayer(uid) == null) {
			Bukkit.getConsoleSender().sendMessage(
					CoreUtils.colorize("&cA new MysticPlayer was requested while the associated player was offline."));
			return null;
		}

		mplayers.put(uid, new MysticPlayer(uid));
		return mplayers.get(uid);
	}

	public static MysticPlayer getMysticPlayer(Player player) {
		if (player == null)
			return null;
		return getMysticPlayer(player.getUniqueId());
	}

	public static MysticPlayer updateMysticPlayer(UUID uid) {
		DebugUtils.debug("Creating new Mystic Player");
		MysticPlayer player = new MysticPlayer(uid);
		mplayers.put(uid, player);
		return player;
	}

}
