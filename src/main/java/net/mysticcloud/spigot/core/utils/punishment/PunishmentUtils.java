package net.mysticcloud.spigot.core.utils.punishment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
	public static Map<String, List<Object>> punishmentBuilder = new HashMap<>();

	public static void registerPunishments() {
		ResultSet rs = CoreUtils.sendQuery("SELECT * FROM Punishments;");
		try {
			while (rs.next()) {
				long duration = Long.parseLong(rs.getString("DURATION"));
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
			rs.close();
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
					switch(severity){
					
					
					
					case MINIMAL:
						duration = TimeUnit.MILLISECONDS.convert(occurrences * 3, TimeUnit.HOURS);
						break;
					
					default:
						duration = TimeUnit.MILLISECONDS.convert(occurrences * 6, TimeUnit.HOURS);
						break;
					}
					
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
		if(!staff.equals("CONSOLE")){
			Bukkit.getPlayer(staff).sendMessage(CoreUtils.colorize(CoreUtils.prefixes("admin") + type.name() + " " + CoreUtils.lookupUsername(uid) + " for " + CoreUtils.formatDate(duration, "&f", "&8")));
			Bukkit.getPlayer(staff).sendMessage(CoreUtils.colorize("&3Infringement&7: " + inf.name()));
			Bukkit.getPlayer(staff).sendMessage(CoreUtils.colorize("&3Notes&7: " + notes));
		} else {
			Bukkit.getConsoleSender().sendMessage(CoreUtils.colorize(CoreUtils.prefixes("admin") + type.name() + " " + CoreUtils.lookupUsername(uid) + " for " + CoreUtils.formatDate(duration, "&f", "&8")));
			Bukkit.getConsoleSender().sendMessage(CoreUtils.colorize("&3Infringement&7: " + inf.name()));
			Bukkit.getConsoleSender().sendMessage(CoreUtils.colorize("&3Notes&7: " + notes));
		}

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
			rs.close();
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
			rs.close();
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
						String sev = rs.getString("NOTES").replaceAll("[\\[\\]]","|");
//						sev = sev.replaceAll("]","|");
						sev = sev.split("EVERITY ")[1].split("|")[0];
						Bukkit.broadcastMessage(sev);
						if(InfringementSeverity.valueOf(sev).equals(severity)){
							occurrences = occurrences+1;
						}
					} 
				}
					
			}
			rs.close();
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
			rs.close();
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
		InventoryCreator inv = new InventoryCreator("&8What kind of offence was this?", null, 36);

		inv.addItem(new ItemStack(Material.PAPER), "&eChat Offences", 'A', new String[] {
				"&7 - Spamming",
				"&7 - Advertising",
				"&7 - Disrespect"});
		inv.addItem(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE), "&eHacking Offences", 'B', new String[] {
				"&7(Basically anything that gives a player",
				"an advantage. This INCLUDES taking",
				"advanage of bugs and glitches)"});
		inv.addItem(new ItemStack(Material.DIAMOND_SWORD), "&cThreats or Attacks", 'C', new String[] {
				"&7 - DDoS",
				"&7 - DOX",
				"&7 - Threatens harm",
				"to a player (even if",
				"it's not feasible)"});

		inv.addItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), "&eClick an option", 'X', (String[]) null);

		inv.setConfiguration(
				new char[] { 
						'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X',
						'X', 'X', 'X', 'A', 'B', 'C', 'X', 'X', 'X',
						'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'
						});
		return inv.getInventory();
	}
	
	public static Inventory getChatPunishmentGUI() {
		InventoryCreator inv = new InventoryCreator("&8What kind of offence was this?", null, 36);

		inv.addItem(new ItemStack(Material.PINK_DYE), "&eChat spamming", 'A', new String[] {});
		inv.addItem(new ItemStack(Material.RED_DYE), "&eAdvertising", 'B', new String[] {});
		inv.addItem(new ItemStack(Material.ORANGE_DYE), "&eDisrespect", 'C', new String[] {});

		inv.addItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), "&eClick an option", 'X', (String[]) null);

		inv.setConfiguration(
				new char[] { 
						'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X',
						'X', 'X', 'X', 'A', 'B', 'C', 'X', 'X', 'X',
						'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'
						});
		return inv.getInventory();
	}

	public static Inventory getHackingPunishmentGUI() {
		InventoryCreator inv = new InventoryCreator("&8How severe was this?", null, 36);

		inv.addItem(new ItemStack(Material.CYAN_DYE), "&7Severity: &bMINIMAL", 'A', new String[] {
				"&7 - X-Raying"
		});
		inv.addItem(new ItemStack(Material.LIME_DYE), "&7Severity: &aLOW", 'B', new String[] {});
		inv.addItem(new ItemStack(Material.YELLOW_DYE), "&7Severity: &eMEDIUM", 'C', new String[] {});
		inv.addItem(new ItemStack(Material.ORANGE_DYE), "&7Severity: &6HIGH", 'D', new String[] {});
		inv.addItem(new ItemStack(Material.RED_DYE), "&7Severity: &cEXTREME", 'E', new String[] {});

		inv.addItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), "&eClick an option", 'X', (String[]) null);

		inv.setConfiguration(
				new char[] { 
						'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X',
						'X', 'X', 'A', 'B', 'C', 'D', 'E', 'X', 'X',
						'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'
						});
		return inv.getInventory();
	}

	public static Inventory getThreatsPunishmentGUI() {
		InventoryCreator inv = new InventoryCreator("&8How severe was this?", null, 36);

		inv.addItem(new ItemStack(Material.CYAN_DYE), "&7Severity: &bMINIMAL", 'A', new String[] {
		});
		inv.addItem(new ItemStack(Material.LIME_DYE), "&7Severity: &aLOW", 'B', new String[] {});
		inv.addItem(new ItemStack(Material.YELLOW_DYE), "&7Severity: &eMEDIUM", 'C', new String[] {});
		inv.addItem(new ItemStack(Material.ORANGE_DYE), "&7Severity: &6HIGH", 'D', new String[] {});
		inv.addItem(new ItemStack(Material.RED_DYE), "&7Severity: &cEXTREME", 'E', new String[] {});

		inv.addItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), "&eClick an option", 'X', (String[]) null);

		inv.setConfiguration(
				new char[] { 
						'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X',
						'X', 'X', 'A', 'B', 'C', 'D', 'E', 'X', 'X',
						'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'
						});
		return inv.getInventory();
	}
	
	public static Inventory getNotesGUI(String staff, UUID offender, InfringementType inf, InfringementSeverity severity, String notes) {
	    
		
		enterNotesEditor(staff,offender,inf,severity,notes);
		
		
        return null;
	}
	
	public static void enterNotesEditor(String staff, UUID offender, InfringementType inf, InfringementSeverity severity, String notes){
		
		if(!staff.equalsIgnoreCase("CONSOLE")){
			Bukkit.getPlayer(staff).closeInventory();
			Bukkit.getPlayer(staff).sendMessage(CoreUtils.prefixes("punishments") + "Use the command \"/punish complete [extra notes]\" to file the punishment.");
		} else {
			Bukkit.getConsoleSender().sendMessage(CoreUtils.prefixes("punishments") + "Use the command \"/punish complete [extra notes]\" to file the punishment.");
		}
		
		
		
		
		List<Object> punishInfo = new ArrayList<>();
		punishInfo.add(offender);
		punishInfo.add(inf);
		punishInfo.add(severity);
		punishInfo.add(notes);
		
		punishmentBuilder.put(staff, punishInfo);
	}
	
	public static boolean finishPunishment(String staff){
		if(punishmentBuilder.containsKey(staff)){
			InfringementType type = null;
			InfringementSeverity severity = null;
			String notes = "";
			UUID uid = null;
			
			for(Object data : punishmentBuilder.get(staff)){
				if(data instanceof InfringementType){
					type = (InfringementType) data;
				}
				if(data instanceof InfringementSeverity){
					severity = (InfringementSeverity) data;
				}
				if(data instanceof String){
					notes = "" + data;
				}
				if(data instanceof UUID){
					uid = (UUID) data;
				}
				
			}
			punish(staff,uid,type,severity,notes);
			punishmentBuilder.remove(staff);
			return true;
		}
		return false;
	}
	
}
						
