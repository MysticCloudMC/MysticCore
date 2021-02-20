package net.mysticcloud.spigot.core.commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.utils.CoreUtils;

public class SeenCommand implements CommandExecutor {

	public SeenCommand(Main plugin, String cmd){
		plugin.getCommand(cmd).setExecutor(this);
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(sender.hasPermission("mysticcloud.admin.cmd.seen")){
			if(args.length == 1) {
				UUID uid = CoreUtils.LookupUUID(args[0]);
				if(uid != null){
					SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd yyyy hh:mm");
					
					String server = "NaN";
					String ip = "NaN";
					String seen = "NaN";
					
					ResultSet iprs = CoreUtils.sendQuery("SELECT IP,UUID FROM PlayerStats");
					try {
						while (iprs.next()) {
							if (iprs.getString("UUID").equalsIgnoreCase(uid.toString())) {
								ip = iprs.getString("IP");
								
								break;
							}
						}
						iprs.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
					seen = sdf.format(new Date(CoreUtils.LookupLastSeen(uid)));
					
					ResultSet srvrs = CoreUtils.sendQuery("SELECT SERVER FROM ServerStats WHERE UUID='" + uid + "' ORDER BY DATE DESC");
					try {
						while (srvrs.next()) {
							if (!srvrs.getString("SERVER").equalsIgnoreCase("NaN")) {
								server = srvrs.getString("SERVER");
								break;
							}
						}
						
						srvrs.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}

					
					
					
					sender.sendMessage(CoreUtils.colorize("&3&lMysticCore &7>&f " + args[0] + " was last seen &7" + (seen) + "&f, on &7" + server + "&f, with &7" + ip + "&f."));
					
				} else {
					sender.sendMessage(CoreUtils.colorize("&3&lMysticCore &7>&f That player couldn't be found in the database."));
				}
				
			} else {
				sender.sendMessage(CoreUtils.prefixes("admin") + "/seen <user>");
			}
		}
		return true;
	}
}
