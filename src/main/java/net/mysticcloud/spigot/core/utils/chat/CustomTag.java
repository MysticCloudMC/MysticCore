package net.mysticcloud.spigot.core.utils.chat;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.admin.DebugUtils;
import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class CustomTag {

	static Map<String, String> tags = new HashMap<>();

	private static String table_name = "custom_tags";

//	AVACADO("&2[AVACADO]"),
//	BEAST("&6[%emoticon:SWORD%BEAST%emoticon:SWORD%] "),
//	STAR("&e[%emoticon:STAR_7%STAR%emoticon:STAR_7%] "),
//	SUPERNOVA("&5[%emoticon:SUN%SUPER NOVA%emoticon:SUN%] "),
//	SPARLE("&d[%emoticon:SPARKLE_SMALL%" + "SPARKLE" + "&d%emoticon:SPARKLE_SMALL%] "),
//	PSYCHO("&d[%emoticon:HEART_1%PSYCHO%emoticon:HEART_1%] "),
//	
//	NONE("you shouldn't have this.");

//	String tag;

//	CustomTag(String tag) {
//		this.tag = CoreUtils.colorize(tag);
//	}

	public static void start() {
		registerTags();
	}

	public static void reloadTags() {
		tags.clear();
		registerTags();
		DebugUtils.debug("Tags reloaded.");
	}

	public static void registerTags() {

		ResultSet rs = CoreUtils.sendQuery("SELECT * FROM " + table_name + "");
		try {
			while (rs.next()) {
				CoreUtils.debug("Adding Tag: " + rs.getString("name"));
				tags.put(rs.getString("name").toUpperCase(), CoreUtils.colorize(rs.getString("tag")));
			}
			rs.close();
		} catch (NullPointerException | SQLException ex) {
			// Skip

		}

	}

	public static String getTag(String key) {
		return tags.containsKey(key.toUpperCase()) ? tags.get(key.toUpperCase()) : "[NT] ";
	}

	public static String[] values() {
		String[] r = new String[tags.values().size()];
		r = tags.values().toArray(r);
		return r;
	}

	public static String[] keys() {
		String[] r = new String[tags.keySet().size()];
		r = tags.keySet().toArray(r);
		return r;
	}

	public static void setTag(Player player, String key) {

		PermissionGroup group = PermissionsEx.getPermissionManager().getGroup(player.getUniqueId().toString());
		group.setPrefix(CustomTag.getTag(key), null);
		if (!PermissionsEx.getUser(player).inGroup(group)) {
			PermissionsEx.getUser(player).addGroup(group);
		}
		player.sendMessage(CoreUtils.colorize(CoreUtils.prefixes("root") + "Tag set."));
	}

	public static String getTag(Player player) {

		PermissionGroup group = PermissionsEx.getPermissionManager().getGroup(player.getUniqueId().toString());
		if (PermissionsEx.getUser(player).inGroup(group)) {
			return group.getPrefix();
		}
		return "";
	}

	public static void removeTag(Player player) {

		PermissionGroup group = PermissionsEx.getPermissionManager().getGroup(player.getUniqueId().toString());
		if (PermissionsEx.getUser(player).inGroup(group)) {
			PermissionsEx.getUser(player).removeGroup(group);
		}
		player.sendMessage(CoreUtils.colorize(CoreUtils.prefixes("root") + "Tag removed."));
	}

	public static int removeTag(String key) {
		key = key.toUpperCase();
		if (tags.containsKey(key)) {
			tags.remove(key);
			return CoreUtils.sendUpdate("DELETE FROM " + table_name + " WHERE name='" + key.toUpperCase() + "';");
		} else {
			return -99;
		}
	}

	public static int addTag(String key, String value) {
		key = key.toUpperCase();
		if (tags.containsKey(key)) {
			tags.put(key, CoreUtils.colorize(value));
			return CoreUtils.sendUpdate("UPDATE " + table_name + " SET tag='" + value + "' WHERE name='" + key + "';");
		} else {
			tags.put(key, CoreUtils.colorize(value));
			return CoreUtils.sendInsert(
					"INSERT INTO " + table_name + " (name, tag) VALUES ('" + key + "', '" + value + "');") ? -1000
							: -1001;
		}
	}

}
