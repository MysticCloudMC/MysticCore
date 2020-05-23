package net.mysticcloud.spigot.core.commands;

import java.util.UUID;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_15_R1.Entity;
import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.commands.listeners.CommandTabCompleter;
import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.entities.Bosses;
import net.mysticcloud.spigot.core.utils.entities.IronBoss;
import net.mysticcloud.spigot.core.utils.entities.MysticEntityUtils;
import net.mysticcloud.spigot.core.utils.events.Event;
import net.mysticcloud.spigot.core.utils.events.EventCheck;
import net.mysticcloud.spigot.core.utils.events.EventType;
import net.mysticcloud.spigot.core.utils.events.EventUtils;

public class BossCommand implements CommandExecutor {

	public BossCommand(Main plugin, String... cmds) {
		for (String cmd : cmds) {
			PluginCommand com = plugin.getCommand(cmd);
			com.setExecutor(this);
			com.setTabCompleter(new CommandTabCompleter());
		}

	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender.hasPermission("mysticcloud.admin.cmd.boss")) {
			if (sender instanceof Player) {
				if (args.length == 1) {
					for (Bosses boss : Bosses.values()) {
						if (boss.getCallName().equalsIgnoreCase(args[0])) {
							MysticEntityUtils.spawnBoss(boss, ((Player) sender).getLocation());
							return true;
						}
					}
					if(args[0].equalsIgnoreCase("test")) {
						Event e = EventUtils.createEvent("Boss Test", EventType.COMPLETION);
						IronBoss boss = new IronBoss(((CraftWorld)((Player)sender).getWorld()).getHandle());
						e.setMetadata("UUID", boss.getUniqueID());
						e.setMetadata("LOCATION", ((Player)sender).getLocation());
						e.setMetadata("BOSS", boss);
						EventCheck check = new EventCheck() {

							@Override
							public boolean check() {
								return Bukkit.getEntity((UUID)e.getMetadata("UUID")) == null;
							}

							@Override
							public void start() {
								MysticEntityUtils.spawnBoss((Entity)e.getMetadata("BOSS"), (Location) e.getMetadata("LOCATION"));
							}

							@Override
							public void end() {
								int z = MysticEntityUtils.damages.get(((Entity)e.getMetadata("BOSS")).getBukkitEntity().getUniqueId()).size();
								for (Entry<UUID, Double> entry : MysticEntityUtils.sortScores(((Entity)e.getMetadata("BOSS")).getBukkitEntity().getUniqueId()).entrySet()) {
									if(z == 1) Bukkit.getPlayer(entry.getKey()).sendMessage(CoreUtils.prefixes("boss") + "You did the most damage!");
									if(z == 2) Bukkit.getPlayer(entry.getKey()).sendMessage(CoreUtils.prefixes("boss") + "You came in second place.");
									if(z == 3) Bukkit.getPlayer(entry.getKey()).sendMessage(CoreUtils.prefixes("boss") + "You came in 3rd place.");
								}
							}
							
						};
						
						e.setEventCheck(check);
						e.start();
					}
				}
			} else {
				sender.sendMessage(CoreUtils.prefixes("bosses") + "Player only command.");
			}
		}
		return true;
	}
}
