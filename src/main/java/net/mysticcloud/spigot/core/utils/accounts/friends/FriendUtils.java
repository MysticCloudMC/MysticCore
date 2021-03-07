package net.mysticcloud.spigot.core.utils.accounts.friends;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.mysticcloud.spigot.core.utils.CoreUtils;

public class FriendUtils {

	static Map<Integer, List<Integer>> friends = new HashMap<>();
	static Map<UUID, Integer> forumIds = new HashMap<>();

	public static void start() {
		ResultSet rs = CoreUtils.sendQuery("SELECT * FROM MysticPlayers WHERE NOT FORUMS_NAME IS NULL;");
		try {
			while (rs.next())
				forumIds.put(UUID.fromString(rs.getString("UUID")), Integer.parseInt(rs.getString("FORUMS_NAME")));
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ResultSet rs2 = CoreUtils.getForumsDatabase().query("SELECT * FROM xf_user_follow");
		try {
			while (rs2.next()) {
				if (friends.containsKey(rs2.getInt("user_id"))) {
					friends.get(rs2.getInt("user_id")).add(rs2.getInt("follow_user_id"));
				} else {
					friends.put(rs2.getInt("user_id"), new ArrayList<>());
					friends.get(rs2.getInt("user_id")).add(rs2.getInt("follow_user_id"));
				}
			}
			rs2.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void update() {
		friends.clear();
		forumIds.clear();
		start();
	}

	public static FriendRequest addFriend(UUID player, UUID friend) {
		FriendRequest request = new FriendRequest();
		if (getForumsID(player) == null) {
			request.setStatus(FriendRequestStatus.PLAYER_NO_FORUMS);
			return request;
		}
		if (getForumsID(friend) == null) {
			request.setStatus(FriendRequestStatus.FRIEND_NO_FORUMS);
			return request;
		}
		if (isFriends(getForumsID(player), getForumsID(friend))) {
			request.setStatus(FriendRequestStatus.FRIENDS);
			return request;
		}

		if (!friends.containsKey(getForumsID(player))) {
			CoreUtils.getForumsDatabase()
					.input("INSERT INTO xf_user_follow (user_id, follow_user_id, follow_date) VALUES ("
							+ getForumsID(player) + "," + getForumsID(player) + "," + (new Date().getTime()) + ");");
			friends.put(getForumsID(player), new ArrayList<>());
		}
		friends.get(getForumsID(player)).add(getForumsID(friend));
		request.setStatus(friends.containsKey(getForumsID(friend))
				? (friends.get(getForumsID(friend)).contains(getForumsID(player)) ? FriendRequestStatus.FRIENDS_NEW
						: FriendRequestStatus.REQUEST_SENT)
				: FriendRequestStatus.REQUEST_SENT);
		return request;

	}

	public static Integer getForumsID(UUID uid) {
		return forumIds.containsKey(uid) ? forumIds.get(uid) : null;
	}

	public static boolean isFriends(UUID player, UUID friend) {
		int id1 = forumIds.containsKey(player) ? forumIds.get(player) : -1;
		int id2 = forumIds.containsKey(friend) ? forumIds.get(friend) : -2;
		if (id1 == -1 || id2 == -2)
			return false;
		return isFriends(player, friend);

	}

	public static boolean isFriends(int player, int friend) {
		if (!friends.containsKey(player))
			return false;
		if (!friends.containsKey(friend))
			return false;
		return friends.get(player).contains(friend) && friends.get(friend).contains(player);
	}

	@SuppressWarnings("deprecation")
	public static List<UUID> getFriends(UUID uid) {
		List<UUID> friends = new ArrayList<>();
		if (getForumsID(uid) != null) {
			for (Integer i : FriendUtils.friends.get(getForumsID(uid))) {
				friends.add(CoreUtils.LookupUUID(i));
			}
		}
		return friends;
	}

}
