package net.mysticcloud.spigot.core.commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.commands.listeners.AdminCommandTabCompleter;
import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.CustomTag;
import net.mysticcloud.spigot.core.utils.accounts.MysticAccountManager;
import net.mysticcloud.spigot.core.utils.accounts.MysticPlayer;
import net.mysticcloud.spigot.core.utils.admin.DebugUtils;
import net.mysticcloud.spigot.core.utils.admin.Holiday;
import net.mysticcloud.spigot.core.utils.particles.ParticleFormatEnum;
import net.mysticcloud.spigot.core.utils.placeholder.EmoticonType;
import net.mysticcloud.spigot.core.utils.placeholder.Emoticons;
import net.mysticcloud.spigot.core.utils.teleport.TeleportUtils;

public class AdminCommands implements CommandExecutor {

	public AdminCommands(Main plugin, String... cmd) {
		for (String s : cmd) {
			PluginCommand com = plugin.getCommand(s);
			com.setExecutor(this);
			com.setTabCompleter(new AdminCommandTabCompleter());
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (cmd.getName().equalsIgnoreCase("setspawn")) {
			// Spawn set code
			if (args.length == 0)
				if (sender instanceof Player) {
					CoreUtils.setSpawnLocation(((Player) sender).getLocation());
					sender.sendMessage(CoreUtils.prefixes("admin") + "Set spawn!");
				} else {
					sender.sendMessage(CoreUtils.prefixes("admin") + "Usage: /setspawn <world> <x> <y> <x>");
				}
			return true;
		}

		if (cmd.getName().equalsIgnoreCase("seen")) {
			if (sender.hasPermission("mysticcloud.admin.cmd.seen")) {
				if (args.length == 1) {
					UUID uid = CoreUtils.LookupUUID(args[0]);
					if (uid != null) {
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
						ResultSet srvrs = CoreUtils.sendQuery(
								"SELECT SERVER FROM ServerStats WHERE UUID='" + uid + "' ORDER BY DATE DESC");
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
						sender.sendMessage(CoreUtils.colorize("&3&lMysticCore &7>&f " + args[0] + " was last seen &7"
								+ (seen) + "&f, on &7" + server + "&f, with &7" + ip + "&f."));

					} else {
						sender.sendMessage(CoreUtils
								.colorize("&3&lMysticCore &7>&f That player couldn't be found in the database."));
					}

				} else {
					sender.sendMessage(CoreUtils.prefixes("admin") + "/seen <user>");
				}
			}
			return true;
		}

		if (cmd.getName().equalsIgnoreCase("uuid")) {
			if (sender.hasPermission("mysticcloud.admin.cmd.uuid")) {
				if (args.length == 1) {
					sender.sendMessage(CoreUtils.colorize(
							"&e&lUUID &7>&f Search result for \"" + args[0] + "\": " + CoreUtils.LookupUUID(args[0])));
				} else {
					sender.sendMessage(CoreUtils.prefixes("admin") + "/uuid <user>");
				}
			}
			return true;
		}

		if (cmd.getName().equalsIgnoreCase("speed")) {
			if (sender instanceof Player) {
				if (sender.hasPermission("mysticcloud.admin.speed")) {
					if (args.length == 1) {
						if (((Player) sender).isFlying()) {
							((Player) sender).setFlySpeed(Float.parseFloat(args[0]));
						} else {
							((Player) sender).setWalkSpeed(Float.parseFloat(args[0]));
						}
					} else {
						if (((Player) sender).isFlying()) {
							sender.sendMessage("Your fly speed: " + ((Player) sender).getFlySpeed());
						} else {
							sender.sendMessage("Your walk speed: " + ((Player) sender).getWalkSpeed());
						}
					}
				} else {
					sender.sendMessage("No permissions.");
				}
			} else {
				sender.sendMessage("Player only command! :))");
			}
		}
		if (cmd.getName().equalsIgnoreCase("plugins")) {
			String msg = "&3Plugins (&f" + Bukkit.getPluginManager().getPlugins().length + "&3)&f: ";
			String pls = "";
			if (sender.hasPermission("mysticcloud.admin")) {
				for (Plugin pl : Bukkit.getPluginManager().getPlugins()) {
					pls = (!pls.equals("") ? pls + "&7,&3 " : "&3") + (pl.isEnabled() ? "&a" : "&c") + pl.getName();
				}
			}
			if (pls.equals(""))
				pls = "&aMysticCore";
			sender.sendMessage(
					CoreUtils.colorize(msg + pls + org.bukkit.ChatColor.getLastColors(CoreUtils.colorize(msg)) + "."));
		}
		if (cmd.getName().equalsIgnoreCase("back")) {
			if (sender instanceof Player) {
				if (sender.hasPermission("mysticcloud.admin.cmd.back")) {
					TeleportUtils.goBack(((Player) sender));
				} else {
					sender.sendMessage(
							CoreUtils.prefixes("admin") + "Sorry you don't have permission to use that command.");
				}
			} else {
				sender.sendMessage(CoreUtils.prefixes("admin") + "That is a player only command.");
			}
		}
		if (cmd.getName().equalsIgnoreCase("level")) {
			if (sender.hasPermission("mysticcloud.admin.setlevel")) {
				if (args.length > 1 && Bukkit.getPlayer(args[0]) == null)
					return true;
				if (args.length == 1 && !(sender instanceof Player))
					return true;
				String a = args.length == 1 ? args[0] : args[1];
				MysticPlayer mp = MysticAccountManager
						.getMysticPlayer(args.length > 1 ? Bukkit.getPlayer(args[0]) : ((Player) sender));
				if (a.contains("+") || a.contains("-")) {
					a = a.replaceAll("+", "");
					mp.gainXP(Double.parseDouble(a));
				} else {
					mp.setXP(Double.parseDouble(a));
				}

			}
		}
		if (cmd.getName().equalsIgnoreCase("control")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(CoreUtils.prefixes("error") + "Player only command.");
				return false;
			}

			if (sender.hasPermission("mysticcloud.admin.control")) {
				sender.sendMessage(CoreUtils.prefixes("error") + "No permission.");
				return false;
			}
			if (args.length == 1) {
				sender.sendMessage(CoreUtils.prefixes("error") + "Usage: /control <player>");
				return false;
			}
			if (Bukkit.getPlayer(args[0]) != null) {
				sender.sendMessage(
						CoreUtils.prefixes("error") + "Could not find that player. Player must be online to control.");
				return false;
			}

			CoreUtils.control((Player) sender, Bukkit.getPlayer(args[0]));

		}

		if (cmd.getName().equalsIgnoreCase("invsee")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (player.hasPermission("mysticcloud.admin.cmd.invsee")) {
					if (args.length == 1) {
						if (Bukkit.getPlayer(args[0]) != null) {
							Player other = Bukkit.getPlayer(args[0]);
							Inventory inv = other.getInventory();
							player.openInventory(inv);
						} else {
							player.sendMessage(CoreUtils.prefixes("admin") + "That player isn't online.");
						}
					} else {
						player.sendMessage(CoreUtils.prefixes("admin") + "You must specify a player");
					}
				} else {
					player.sendMessage(CoreUtils.prefixes("admin") + "You don't have permission to use that command.");
				}

			} else {
				sender.sendMessage(CoreUtils.prefixes("admin") + "Player only command.");
			}
		}

		if (cmd.getName().equalsIgnoreCase("debug")) {
			if (sender instanceof Player) {
				if (sender.hasPermission("mysticcloud.admin.cmd.debug")) {

					if (args.length == 0) {
						if (DebugUtils.isDebugger(((Player) sender).getUniqueId())) {
							sender.sendMessage(CoreUtils.prefixes("debug") + "Removing you from the Debug Group");
							DebugUtils.removeDebugger(((Player) sender).getUniqueId());
							return true;
						}
						sender.sendMessage(CoreUtils.prefixes("debug") + "Adding you to the Debug Group");
						DebugUtils.addDebugger(((Player) sender).getUniqueId());
						return true;

					}

					if (args.length >= 1) {
						if (args[0].equalsIgnoreCase("emoticons")) {
							if (args.length == 1) {
								for (Emoticons emote : Emoticons.values()) {
									sender.sendMessage(emote.name() + ": " + emote);
								}
							}
							if (args.length == 2) {
								for (Emoticons emote : Emoticons.values()) {
									if (EmoticonType.valueOf(args[1].toUpperCase()) != null) {
										if (emote.getTypes().contains(EmoticonType.valueOf(args[1].toUpperCase()))) {
											sender.sendMessage(emote.name() + ": " + emote);
										}
									} else if (emote.name().contains(args[1].toUpperCase()))
										sender.sendMessage(emote.name() + ": " + emote);
								}
							}
						}
						if (args[0].equalsIgnoreCase("hide")) {
							if (args.length == 2) {
								if (Bukkit.getPlayer(args[1]) != null)
									((Player) sender).hidePlayer(Bukkit.getPlayer(args[1]));
								else
									sender.sendMessage(CoreUtils.prefixes("error") + "That player isn't online.");
							}
							if (args.length == 3) {
								if (Bukkit.getPlayer(args[1]) == null) {
									sender.sendMessage(CoreUtils.prefixes("error") + args[1] + " isn't online.");
									return true;
								}
								if (Bukkit.getPlayer(args[2]) == null) {
									sender.sendMessage(CoreUtils.prefixes("error") + args[2] + " isn't online.");
									return true;
								}
								Bukkit.getPlayer(args[1]).hidePlayer(Bukkit.getPlayer(args[2]));
							}
						}
						if (args[0].equalsIgnoreCase("show")) {
							if (args.length == 2) {
								if (Bukkit.getPlayer(args[1]) != null)
									((Player) sender).showPlayer(Bukkit.getPlayer(args[1]));
								else
									sender.sendMessage(CoreUtils.prefixes("error") + "That player isn't online.");
							}
							if (args.length == 3) {
								if (Bukkit.getPlayer(args[1]) == null) {
									sender.sendMessage(CoreUtils.prefixes("error") + args[1] + " isn't online.");
									return true;
								}
								if (Bukkit.getPlayer(args[2]) == null) {
									sender.sendMessage(CoreUtils.prefixes("error") + args[2] + " isn't online.");
									return true;
								}
								Bukkit.getPlayer(args[1]).showPlayer(Bukkit.getPlayer(args[2]));
							}
						}
						if (args.length == 2) {
							if (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("delete")
									|| args[0].equalsIgnoreCase("add")) {
								if (Bukkit.getPlayer(args[1]) == null) {
									sender.sendMessage(CoreUtils.prefixes("debug") + "That player is not online.");
									return true;
								}
								if (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("delete")) {
									UUID uid = Bukkit.getPlayer(args[1]).getUniqueId();
									if (DebugUtils.isDebugger(uid)) {
										DebugUtils.removeDebugger(uid);
										Bukkit.getPlayer(uid).sendMessage(
												CoreUtils.prefixes("debug") + "Removing you from the Debug Group");
									} else {
										sender.sendMessage(
												CoreUtils.prefixes("debug") + "That player isn't in the Debug Group");
									}
									return true;
								}
								if (args[0].equalsIgnoreCase("add")) {
									UUID uid = Bukkit.getPlayer(args[1]).getUniqueId();
									if (!DebugUtils.isDebugger(uid)) {
										DebugUtils.addDebugger(uid);
										Bukkit.getPlayer(uid).sendMessage(
												CoreUtils.prefixes("debug") + "Adding you to the Debug Group");
									} else {
										sender.sendMessage(CoreUtils.prefixes("debug")
												+ "That player is already in the Debug Group");
									}
									return true;
								}

							}
						}

						if (args[0].equalsIgnoreCase("reload")) {
							if (args.length == 2) {
								if (args[1].equalsIgnoreCase("tags")) {
									if (sender instanceof Player)
										DebugUtils.addDebugger(((Player) sender).getUniqueId());
									CustomTag.reloadTags();
								}
							}
						}

						if (args[0].equalsIgnoreCase("holiday")) {
							if (args.length == 1) {
								DebugUtils.holidayCheck(!DebugUtils.holidayCheck());
								sender.sendMessage(CoreUtils.prefixes("debug") + "Holiday Check is set to " + CoreUtils
										.colorize((DebugUtils.holidayCheck() ? "&a&ltrue" : "&c&lfalse") + "&f."));
							}
							if (args.length == 2) {
								CoreUtils.setHoliday(Holiday.valueOf(args[1]));
								if (DebugUtils.holidayCheck()) {
									DebugUtils.holidayCheck(false);
									sender.sendMessage(CoreUtils.prefixes("debug") + "Holiday Check is set to "
											+ CoreUtils.colorize("&c&lfalse&f."));
								}
							}
						}

						if (args[0].equalsIgnoreCase("pcolor")) {
							if (args.length == 3) {
								CoreUtils.particles(((Player) sender).getUniqueId())
										.setDustOptions(new DustOptions(
												org.bukkit.Color.fromRGB(Integer.parseInt(args[1].split(",")[0]),
														Integer.parseInt(args[1].split(",")[1]),
														Integer.parseInt(args[1].split(",")[2])),
												Float.parseFloat(args[2])));
							}
						}

						if (args[0].equalsIgnoreCase("particles")) {
							if (args.length == 3) {
								CoreUtils.particles.put(((Player) sender).getUniqueId(),
										ParticleFormatEnum.valueOf(args[1].toUpperCase()).formatter());
								CoreUtils.particles(((Player) sender).getUniqueId())
										.particle(Particle.valueOf(args[2].toUpperCase()));
							}
						}

						if (args[0].equalsIgnoreCase("color")) {

							String s = "Hello World! Aren't rainbows cool?";
							ComponentBuilder builder = new ComponentBuilder();
							for (int i = 0; i != s.length(); i++) {

								builder.append(s.substring(i, i + 1))
										.color(ChatColor.of(CoreUtils.generateColor(i, 0.5)));
							}
							sender.spigot().sendMessage(builder.create());
						}
						if (args[0].equalsIgnoreCase("time")) {
							sender.sendMessage(CoreUtils.colorize(CoreUtils.prefixes("debug") + CoreUtils.getMonth()
									+ "/" + CoreUtils.getDay() + " " + CoreUtils.getTime() + " "
									+ (CoreUtils.getHoliday().equals(Holiday.NONE) ? ""
											: CoreUtils.getHoliday().getName())));

						}
					}
				}

			}
			return true;
		}
		return true;
	}
}
