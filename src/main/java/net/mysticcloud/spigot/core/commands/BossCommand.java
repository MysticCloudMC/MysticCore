package net.mysticcloud.spigot.core.commands;

import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.craftbukkit.v1_16_R2.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_16_R2.Entity;
import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.commands.listeners.CommandTabCompleter;
import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.accounts.MysticAccountManager;
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
							EventUtils.startBossEvent(boss,((Player)sender).getLocation());
							return true;
						}
						
					}
					sender.sendMessage(CoreUtils.prefixes("boss") + "Unknown boss.");
					if(args[0].equalsIgnoreCase("test")) {
						Event e = EventUtils.createEvent("Boss Test", EventType.TIMED).getValue();
						IronBoss boss = new IronBoss(((CraftWorld)((Player)sender).getWorld()).getHandle());
						e.setMetadata("UUID", boss.getUniqueID());
						e.setMetadata("LOCATION", ((Player)sender).getLocation());
						e.setMetadata("BOSS", boss);
						e.setMetadata("DURATION", TimeUnit.MILLISECONDS.convert(10, TimeUnit.MINUTES));
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
									if(z == 1) {
										MysticAccountManager.getMysticPlayer(entry.getKey()).gainXP(0.5);
										Bukkit.getPlayer(entry.getKey()).getInventory().addItem(new ItemStack(Material.DIAMOND,3));
										Bukkit.getPlayer(entry.getKey()).sendMessage(CoreUtils.prefixes("boss") + "You did the most damage! You earned 50xp, and 3 diamonds!");
									}
									if(z == 2) {
										MysticAccountManager.getMysticPlayer(entry.getKey()).gainXP(0.35);
										Bukkit.getPlayer(entry.getKey()).sendMessage(CoreUtils.prefixes("boss") + "You came in second place. You earned 35xp.");
									}
									if(z == 3) Bukkit.getPlayer(entry.getKey()).sendMessage(CoreUtils.prefixes("boss") + "You came in 3rd place.");
								}
							}
							
						};
						
						e.setEventCheck(check);
						e.start();
					}
					if(args[0].equalsIgnoreCase("test2")) {
						Event e = EventUtils.createEvent("Timed Test", EventType.TIMED).getValue();
						e.setMetadata("DURATION", TimeUnit.MILLISECONDS.convert(60, TimeUnit.SECONDS));
						e.setMetadata("DESCRIPTION", CoreUtils.colorize("Pick up as many gold nuggets as you can!"));
						e.setMetadata("STARTED", CoreUtils.getDate().getTime());
						EventCheck check = new EventCheck() {

							@Override
							public boolean check() {
								return CoreUtils.getDate().getTime() - ((long)e.getMetadata("DURATION")) >= ((long)e.getMetadata("STARTED"));
							}

							@Override
							public void start() {
//								MysticEntityUtils.spawnBoss((Entity)e.getMetadata("BOSS"), (Location) e.getMetadata("LOCATION"));
							}

							@Override
							public void end() {
								int z = e.getScores().size();
								for (Entry<UUID, Double> entry : e.sortScores().entrySet()) {
									if(z == 1) {
										MysticAccountManager.getMysticPlayer(entry.getKey()).gainXP(entry.getValue());
										Bukkit.getPlayer(entry.getKey()).sendMessage(CoreUtils.colorize(CoreUtils.prefixes("boss") + "You did the most damage! You earned &7" + entry.getValue()*100 + "&fxp!"));
									}
									if(z == 2) {
										MysticAccountManager.getMysticPlayer(entry.getKey()).gainXP(entry.getValue());
										Bukkit.getPlayer(entry.getKey()).sendMessage(CoreUtils.colorize(CoreUtils.prefixes("boss") + "You came in second place. You earned &7" + entry.getValue()*100 + "&fxp!"));
									}
									if(z == 3) {
										MysticAccountManager.getMysticPlayer(entry.getKey()).gainXP(entry.getValue());
										Bukkit.getPlayer(entry.getKey()).sendMessage(CoreUtils.colorize(CoreUtils.prefixes("boss") + "You came in 3rd place. You earned &" + entry.getValue()*100 + "&fxp!"));
									}
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
