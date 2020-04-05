package net.mysticcloud.spigot.core.utils.punishment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.InventoryCreator;

public class PunishmentUtils {

	static List<Punishment> punishments = new ArrayList<>();
	static List<Punishment> finished = new ArrayList<>();

	public static void registerPunishments() {
		ResultSet rs = CoreUtils.sendQuery("SELECT * FROM Punishments;");
		try {
			while (rs.next()) {
				int duration = Integer.parseInt(rs.getString("DURATION"));
				UUID uid = UUID.fromString(rs.getString("UUID"));
				PunishmentType type = PunishmentType.valueOf(rs.getString("ACTION"));
				long date = Long.parseLong(rs.getString("DATE"));
				String notes = rs.getString("NOTES");
				if (!notes.contains("[WARNING]")) {
					Punishment punishment = new Punishment(uid, type, duration, date);
					punishment.setNotes(notes);
					punishments.add(punishment);
					
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static void punish(String staff, UUID offender, InfringementType inf, String notes) {
		punish(staff,offender,inf,InfringementSeverity.LOW, notes);
	}

	public static void punish(String staff, UUID offender, InfringementType inf, InfringementSeverity severity, String notes) {
		int occurrences = getOccurrences(offender, inf);
		boolean warn = false;
		long duration = 0;
		PunishmentType type = PunishmentType.WARN;
		if (occurrences == 0) {
			warn = true;
			notes = "[WARNING] " + notes;
		}
			

		
		notes = "[SEVERITY " + severity.name() + "] " + notes; 
		if (!warn) {
			switch (inf) {
			case CHAT:
				if(occurrences <= 1){
					type = PunishmentType.KICK;
					if(!notes.contains("[WARNING]")) notes = "[WARNING] " + notes;
				} else {
					type = PunishmentType.MUTE;
					duration = TimeUnit.MILLISECONDS.convert(occurrences * 3, TimeUnit.HOURS);
				}
				break;
			case HACKING:
				type = PunishmentType.BAN;
				switch(severity){
				case EXTREME:
					if(getOccurrences(offender,inf,severity) >= 1){
						duration = TimeUnit.MILLISECONDS.convert(Integer.MAX_VALUE, TimeUnit.DAYS);
					} else {
						duration = TimeUnit.MILLISECONDS.convert(31, TimeUnit.DAYS);
					}
					break;
				case HIGH:
					duration = TimeUnit.MILLISECONDS.convert(occurrences * 30, TimeUnit.DAYS);
					break;
				case MEDIUM:
					duration = TimeUnit.MILLISECONDS.convert(occurrences*7, TimeUnit.DAYS);
					break;
				case LOW:
				default:
					duration = TimeUnit.MILLISECONDS.convert(occurrences*3, TimeUnit.DAYS);
					break;
				}
				
				break;
			case GREIF:
				
				break;
			default:
				break;
			}
			if(!type.equals(PunishmentType.KICK)){
				Punishment punish = new Punishment(offender, type, (int) duration, new Date().getTime());
				punish.setNotes(notes);
				punishments.add(punish);
			}	
		}
		punish(staff, offender, inf, type, notes, warn, duration);
		
	}


	private static void punish(String staff, UUID uid, InfringementType inf, PunishmentType type, String notes, boolean warn, long duration) {

		
		if (type.equals(PunishmentType.KICK) && !warn)
			if (Bukkit.getPlayer(uid) != null) 
				Bukkit.getPlayer(uid).kickPlayer(CoreUtils.colorize("You've been kicked\n" + notes));
			
		if(type.equals(PunishmentType.BAN))
			if(Bukkit.getPlayer(uid) != null)
				Bukkit.getPlayer(uid).kickPlayer(CoreUtils.colorize("You've been banned for &4" + CoreUtils.getSimpleTimeFormat(duration) + "&f\n" + notes));
		if (type.equals(PunishmentType.MUTE) && !warn) {
			if (Bukkit.getPlayer(uid) != null) 
				Bukkit.getPlayer(uid).sendMessage(CoreUtils.prefixes("punishments") + "You've been muted.");
			
		}
		if (type.equals(PunishmentType.WARN) || warn) {
			if (Bukkit.getPlayer(uid) != null) 
				Bukkit.getPlayer(uid).sendMessage(CoreUtils.prefixes("punishments") + "You've been warned: " + CoreUtils.colorize(notes));
			
		}

		CoreUtils.sendInsert("INSERT INTO Punishments (UUID, TYPE, DURATION, DATE, NOTES, STAFF, ACTION) VALUES ('"
				+ uid.toString() + "','" + inf.name() + "','" + duration + "','" + new Date().getTime() + "','" + notes
				+ "','" + staff + "', '" + type.name() + "');");

	}

	public static List<Punishment> getPunishments() {
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

	public static int getOccurrences(UUID uid, InfringementType type) {
		int occurrences = 0;
		ResultSet rs = CoreUtils.sendQuery("SELECT * FROM Punishments WHERE UUID='" + uid.toString() + "';");
		try {
			while (rs.next()) {
				if (rs.getString("TYPE").equalsIgnoreCase(type.name()))
					occurrences = occurrences + 1;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return occurrences;
	}
	public static int getOccurrences(UUID uid, InfringementType type, InfringementSeverity severity) {
		int occurrences = 0;
		ResultSet rs = CoreUtils.sendQuery("SELECT * FROM Punishments WHERE UUID='" + uid.toString() + "';");
		try {
			while (rs.next()) {
				if (rs.getString("TYPE").equalsIgnoreCase(type.name())){
					if(rs.getString("NOTES").contains("[SEVERITY ")){
						String sev = rs.getString("NOTES").replaceAll("[","|");
						sev = sev.replaceAll("]","|");
						sev = sev.split("EVERITY ")[1].split("|")[0];
						Bukkit.broadcastMessage(sev);
						if(InfringementSeverity.valueOf(sev).equals(severity)){
							occurrences = occurrences+1;
						}
					} 
				}
					
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return occurrences;
	}

	public static int getOccurrences(UUID uid, PunishmentType type) {
		int occurrences = 0;
		ResultSet rs = CoreUtils.sendQuery("SELECT * FROM Punishments WHERE UUID='" + uid.toString() + "';");
		try {
			while (rs.next()) {
				if (rs.getString("ACTION").equalsIgnoreCase(type.name()))
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
		for (Punishment finish : finished) {
			punishments.remove(finish);
		}
		finished.clear();
	}

	public static Inventory getPunishmentGUI(String string) {
		InventoryCreator inv = new InventoryCreator("&b&lColors", null, 36);

		inv.addItem(new ItemStack(Material.PINK_DYE), "&eChat spamming", 'A', new String[] {});
		inv.addItem(new ItemStack(Material.RED_DYE), "&eStaff disrespect", 'B', new String[] {});
		inv.addItem(new ItemStack(Material.ORANGE_DYE), "&eHacking", 'C', new String[] {});
		inv.addItem(new ItemStack(Material.YELLOW_DYE), "&eYellow", 'D', new String[] {});

		inv.addItem(new ItemStack(Material.LIME_DYE), "&aLime Green", 'E', new String[] {});
		inv.addItem(new ItemStack(Material.GREEN_DYE), "&2Green", 'F', new String[] {});

		inv.addItem(new ItemStack(Material.BLUE_DYE), "&3Blue", 'G', new String[] {});
		inv.addItem(new ItemStack(Material.PURPLE_DYE), "&5Purple", 'H', new String[] {});
		inv.addItem(new ItemStack(Material.BROWN_DYE), "&6Brown", 'I', new String[] {});

		inv.addItem(new ItemStack(Material.BLACK_DYE), "&7Black", 'J', new String[] {});
		inv.addItem(new ItemStack(Material.WHITE_DYE), "&fWhite", 'K', new String[] {});

		inv.addItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), "&eClick an option", 'X', (String[]) null);

		inv.setConfiguration(
				new char[] { 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'});
		return inv.getInventory();
	}
}
						
