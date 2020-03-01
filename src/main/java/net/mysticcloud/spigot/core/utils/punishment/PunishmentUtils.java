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
		int occurrences = 1;
		ResultSet rs = CoreUtils.sendQuery("SELECT * FROM Punishments WHERE UUID='" + uid.toString() + "';");
		try {
			while (rs.next()) {
				occurrences = occurrences + 1;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long duration = TimeUnit.MILLISECONDS.convert(occurrences * 3, TimeUnit.HOURS);
		
		Punishment punish = new Punishment(uid, type, (int)duration, new Date().getTime());
		punishments.add(punish);
		
		if(type.equals(PunishmentType.BAN) || type.equals(PunishmentType.KICK))
			if(Bukkit.getPlayer(uid)!=null){
				Bukkit.getPlayer(uid).kickPlayer("You've been banned/kicked");
			}
		if(type.equals(PunishmentType.MUTE)) {
			if(Bukkit.getPlayer(uid)!=null){
				Bukkit.getPlayer(uid).sendMessage(CoreUtils.prefixes("punishments") + "You've been muted.");
			}
		}
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
		
		inv.setConfiguration(new char[] {
				'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X',
				'X', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'X',
				'X', 'X', 'H', 'I', 'X', 'J', 'K', 'X', 'X',
				'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'
				});
		return inv.getInventory();
	}

}
