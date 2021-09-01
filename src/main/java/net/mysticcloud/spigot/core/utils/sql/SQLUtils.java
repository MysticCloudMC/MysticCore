package net.mysticcloud.spigot.core.utils.sql;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class SQLUtils {

	public static final String[] QUERY_COMMANDS = new String[] { "SELECT", "SHOW" };
	public static final String[] INSERT_COMMANDS = new String[] { "INSERT", "CREATE" };
	public static final String[] UPDATE_COMMANDS = new String[] { "UPDATE", "DELETE", "DROP" };

	private static Map<String, IDatabase> databases = new HashMap<>();

	public static boolean createDatabase(String name, IDatabase db) throws SQLException {
		if (!db.init()) {
			System.out.println("There was an error registering database: " + name);
			return false;
		}
		databases.put(name, db);
		return true;
	}

	public static void createDatabase(String name, SQLDriver driver, String host, String database, Integer port,
			String username, String password) throws SQLException {
		createDatabase(name, new IDatabase(driver, host, database, port, username, password));

	}

	public static IDatabase getDatabase(String name) {
		return databases.containsKey(name) ? databases.get(name) : null;
	}

}