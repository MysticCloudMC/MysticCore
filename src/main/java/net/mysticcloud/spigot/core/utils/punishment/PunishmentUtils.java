package net.mysticcloud.spigot.core.utils.punishment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import net.mysticcloud.spigot.core.utils.CoreUtils;

public class PunishmentUtils {

	static List<Punishment> punishments = new ArrayList<>();
	static List<Punishment> finished = new ArrayList<>();
	
	
	public static void registerPunishments() {
		ResultSet rs = CoreUtils.sendQuery("SELECT * FROM Punishments;");
		try {
			while (rs.next()) {
				int duration = Integer.parseInt(rs.getString("DURATION"));
				UUID uid = UUID.fromString(rs.getString("UUID"));
				PunishmentType type = PunishmentType.valueOf(rs.getString("TYPE"));
				long date = Long.parseLong(rs.getString("DATE"));
				Punishment punishment = new Punishment(uid,type,duration,date);
				punishments.add(punishment);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

	public static void punish(UUID uid, PunishmentType type) {
		punish(uid, type, "");
	}

	public static void punish(UUID uid, PunishmentType type, String notes) {
		int occurrences = 0;
		ResultSet rs = CoreUtils.sendQuery("SELECT * FROM Punishments WHERE UUID='" + uid.toString() + "';");
		try {
			while (rs.next()) {
				occurrences = occurrences + 1;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int duration = occurrences * 3;
		
		Punishment punish = new Punishment(uid, type, duration, new Date().getTime());
		punishments.add(punish);

		CoreUtils.sendInsert("INSERT INTO Punishments (UUID, TYPE, DURATION, DATE, NOTES) VALUES ('" + uid.toString()
				+ "','" + type.name() + "','" + duration + "','" + new Date().getTime() + "','" + notes + "');");

	}
	
	public static List<Punishment> getPunishments(){
		return punishments;
	}

	public static int getOccurrences(UUID uid) {
		int occurrences = 0;
		ResultSet rs = CoreUtils.sendQuery("SELECT * FROM Punishments WHERE UUID='" + uid.toString() + "';");
		try {
			while (rs.next()) {
				occurrences = occurrences + 1;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return occurrences;
	}

	public static void finishPunishment(Punishment punishment) {
		finished.add(punishment);
	}
	public static void finishPunishments() {
		for(Punishment finish :finished) {
			punishments.remove(finish);
		}
		finished.clear();
	}

}
