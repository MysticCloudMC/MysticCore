package net.mysticcloud.spigot.core.utils.accounts;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json2.JSONObject;

import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.admin.DebugUtils;

public class MysticAccountManager {

	static Map<UUID, MysticPlayer> mplayers = new HashMap<>();

	public static MysticPlayer getMysticPlayer(UUID uid) {
		if (mplayers.containsKey(uid)) {
			return mplayers.get(uid);
		}
		updateMysticPlayer(uid);

		if (Bukkit.getPlayer(uid) == null) {
			Bukkit.getConsoleSender().sendMessage(
					CoreUtils.colorize("&cA new MysticPlayer was requested while the associated player was offline."));
			return null;
		}

		MysticPlayer player = new MysticPlayer(uid);

		mplayers.put(uid, player);

		updateMysticPlayer(player.getUUID());

		return player;
	}

	public static MysticPlayer getMysticPlayer(Player player) {
		if (player == null)
			return null;
		return getMysticPlayer(player.getUniqueId());
	}

	public static void saveMysticPlayer(Player player) {
		saveMysticPlayer(getMysticPlayer(player));
	}

	public static void saveMysticPlayer(UUID uid) {
		saveMysticPlayer(getMysticPlayer(uid));
	}

	public static void saveMysticPlayer(MysticPlayer player) {
		String sql = "UPDATE MysticPlayers SET ";
		sql = sql + "BALANCE=\"" + player.getBalance() + "\", ";
		sql = sql + "GEMS=\"" + player.getGems() + "\",";
		sql = sql + "LEVEL=\"" + player.getXP() + "\", ";
		sql = sql + "EXTRA_DATA=\"" + player.getExtraData().toString().replaceAll("\"", "\\\\\"") + "\" ";
		sql = sql + "WHERE UUID=\"" + player.getUUID() + "\";";
		DebugUtils.debug(sql);
		CoreUtils.sendUpdate(sql);
	}

	public static int updateMysticPlayer(UUID uid) {
		ResultSet rs = CoreUtils.sendQuery("SELECT * FROM MysticPlayers WHERE UUID='" + uid.toString() + "';");
		int a = 0;
		try {
			while (rs.next()) {
				a = a + 1;
				MysticPlayer mp = mplayers.containsKey(uid) ? mplayers.get(uid) : new MysticPlayer(uid);
				mp.setBalance(Double.parseDouble(rs.getString("BALANCE")));
				mp.setGems(Integer.parseInt(rs.getString("GEMS")));
				mp.setXP(Double.parseDouble(rs.getString("LEVEL")));
				mp.setNitro(Boolean.parseBoolean(rs.getString("DISCORD_BOOSTER")));
				JSONObject json = mp.getExtraData();
				mp.setExtraData(json);
				if(json.has("settings")) {
					JSONObject settings = json.getJSONObject("settings");
					for(PlayerSettings s : PlayerSettings.values()) {
						if(json.has(s.name()))
							mp.setSetting(s, settings.getString(s.name()));
					}
				}
				mplayers.put(uid, mp);
				CoreUtils.debug("Registered MysticPlayer: " + uid);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (a == 0) {
			CoreUtils.sendInsert("INSERT INTO MysticPlayers (UUID, BALANCE, GEMS, LEVEL) VALUES ('" + uid.toString()
					+ "','0','0','1');");
			DebugUtils.debug("Created MysticPlayer: " + uid);
			
			//TODO save extra data as-well
		}
		return a;
	}

}
