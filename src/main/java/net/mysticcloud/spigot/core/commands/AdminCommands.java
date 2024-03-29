package net.mysticcloud.spigot.core.commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.mysticcloud.spigot.core.MysticCore;
import net.mysticcloud.spigot.core.commands.listeners.AdminCommandTabCompleter;
import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.UID;
import net.mysticcloud.spigot.core.utils.accounts.MysticAccountManager;
import net.mysticcloud.spigot.core.utils.accounts.MysticPlayer;
import net.mysticcloud.spigot.core.utils.admin.DebugUtils;
import net.mysticcloud.spigot.core.utils.admin.Holiday;
import net.mysticcloud.spigot.core.utils.admin.MysticPerms;
import net.mysticcloud.spigot.core.utils.chat.CustomTag;
import net.mysticcloud.spigot.core.utils.particles.BlockParticleUtils;
import net.mysticcloud.spigot.core.utils.particles.ParticleFormat;
import net.mysticcloud.spigot.core.utils.particles.ParticleFormatEnum;
import net.mysticcloud.spigot.core.utils.particles.formats.RandomFormat;
import net.mysticcloud.spigot.core.utils.placeholder.EmoticonType;
import net.mysticcloud.spigot.core.utils.placeholder.Emoticons;
import net.mysticcloud.spigot.core.utils.portals.Portal;
import net.mysticcloud.spigot.core.utils.portals.PortalUtils;
import net.mysticcloud.spigot.core.utils.punishment.PunishmentUtils;
import net.mysticcloud.spigot.core.utils.skulls.SkullUtils;
import net.mysticcloud.spigot.core.utils.teleport.TeleportUtils;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class AdminCommands implements CommandExecutor {

	public AdminCommands(MysticCore plugin, String... cmd) {
		for (String s : cmd) {
			PluginCommand com = plugin.getCommand(s);
			com.setExecutor(this);
			com.setTabCompleter(new AdminCommandTabCompleter());
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (cmd.getName().equalsIgnoreCase("blockparticles")) {
			if (sender instanceof Player) {
				if (sender.hasPermission(MysticPerms.CMD_BLOCK_PARTICLES)) {
					if (args.length == 0 || (args.length > 0 && args[0].equalsIgnoreCase("help"))) {
						sender.sendMessage(CoreUtils.colorize(
								CoreUtils.prefixes("particles") + "Below are a list of commands you can use:"));
						sender.sendMessage(CoreUtils.colorize("&7/" + label + " list&f - Lists all block particles."));
						sender.sendMessage(CoreUtils.colorize("&7/" + label
								+ " create [id] [format] [option1=value1] [option2=value2]...&f - Creates a new block particle instance where you're standing."));
						sender.sendMessage(CoreUtils.colorize(
								"&7/" + label + " format <id> [format]&f - Updates and shows current format."));
						sender.sendMessage(CoreUtils.colorize("&7/" + label
								+ " options <id> <option1=value1> [option2=value2]...&f - Updates options."));
						sender.sendMessage(CoreUtils.colorize(
								"&7/" + label + " particle <id> [particle]&f - Updates and shows particle type."));
						sender.sendMessage(CoreUtils.colorize(
								"&7/" + label + " delete <id>&f - Deletes all records of that block particle."));
						return true;
					}
					if (args[0].equalsIgnoreCase("create")) {
						String id = args.length >= 2 ? args[1] : "bp-" + new UID(5);
						ParticleFormat format = args.length >= 3 ? ParticleFormatEnum.valueOf(args[2]).formatter()
								: new RandomFormat();
						format.particle(Particle.FLAME);
						if (args.length >= 4)
							for (int i = 3; i != args.length; i++) {
								if (args[i].contains("=")) {
									format.setOption(args[i].split("=")[0], args[i].split("=")[1]);
								}
							}
						BlockParticleUtils.createBlockParticles(id,
								((Player) sender).getLocation().getBlock().getLocation().clone().add(0.5, 0.5, 0.5),
								format);
						sender.sendMessage(CoreUtils.colorize(
								CoreUtils.prefixes("particles") + "You've created &7" + id + "&f block particle"));

					}
					if (args[0].equalsIgnoreCase("list")) {
						String a = "";
						sender.sendMessage(CoreUtils.colorize(
								CoreUtils.prefixes("particles") + "All of the block particles are listed below"));
						for (Entry<String, Location> e : BlockParticleUtils.getBlocks().entrySet()) {
							a = a == "" ? "&f" + e.getKey() : a + "&7,&f " + e.getKey();
						}
						sender.sendMessage(CoreUtils.colorize(a + "&7."));
					}
					if (args[0].equalsIgnoreCase("format")) {
						if (args.length == 1) {
							sender.sendMessage(CoreUtils.colorize(
									CoreUtils.prefixes("particles") + "Usage: /" + label + " format <id> [format]"));
							return true;
						}
						String id = args[1];
						if (args.length >= 3) {
							ParticleFormat format = ParticleFormatEnum.valueOf(args[2]).formatter();
							BlockParticleUtils.updateFormat(id, format);
						}
						sender.sendMessage(CoreUtils.colorize(CoreUtils.prefixes("particles") + "The format &7" + id
								+ "&f is using is &7" + BlockParticleUtils.getBlockParticleFormat(id).name() + "&f."));
					}

					if (args[0].equalsIgnoreCase("options")) {
						if (args.length <= 2) {
							sender.sendMessage(CoreUtils.colorize(CoreUtils.prefixes("particles") + "Usage: /" + label
									+ " options <id> <option1=value1> [option2=value2]..."));
							return true;
						}
						String id = args[1];
						if (args.length >= 3)
							for (int i = 2; i != args.length; i++) {
								BlockParticleUtils.updateOptions(id, args[i]);
							}
					}

					if (args[0].equalsIgnoreCase("particle")) {
						if (args.length == 1) {
							sender.sendMessage(CoreUtils.colorize(CoreUtils.prefixes("particles") + "Usage: /" + label
									+ " particle <id> [particle]"));
							return true;
						}
						String id = args[1];
						if (args.length >= 3) {
							BlockParticleUtils.getBlockParticleFormat(id).particle(Particle.valueOf(args[2]));
						}
						sender.sendMessage(CoreUtils
								.colorize(CoreUtils.prefixes("particles") + "The particle &7" + id + "&f is using is &7"
										+ BlockParticleUtils.getBlockParticleFormat(id).particle().name() + "&f."));
					}
					if (args[0].equalsIgnoreCase("delete")) {
						if (args.length == 1) {
							sender.sendMessage(CoreUtils
									.colorize(CoreUtils.prefixes("particles") + "Usage: /" + label + " delete <id>"));
							return true;
						}
						String id = args[1];
						BlockParticleUtils.deleteBlockParticle(id);
					}

				}
			}
		}
		if (cmd.getName().equalsIgnoreCase("portal")) {
			if (sender instanceof Player) {
				if (sender.hasPermission(MysticPerms.CMD_PORTAL)) {
					if (args.length == 0) {
						sender.sendMessage(CoreUtils.colorize(CoreUtils.prefixes("portals")
								+ "Entering portal editor. Left and right click blocks to select a region, then run the command &7/portal create <name>&f."));
						PortalUtils.enterPortalEditor((Player) sender);
						return true;
					}
					if (args[0].equalsIgnoreCase("info")) {
						if (args.length == 2) {
							if (PortalUtils.getPortal(args[1]) != null) {
								Portal p = PortalUtils.getPortal(args[1]);
								sender.sendMessage(CoreUtils.colorize(
										CoreUtils.prefixes("portals") + "Info on portal &f" + p.name() + "&7:"));
								sender.sendMessage(CoreUtils.colorize("&7Name&8:&9 " + p.name()));
								sender.sendMessage(CoreUtils
										.colorize("&7Link&8:&9 " + (p.link() == "" ? "Not linked." : p.link())));
								sender.sendMessage(CoreUtils.colorize("&7Region&8:"));
								sender.sendMessage(CoreUtils.colorize("&7 - Name&8:&9 " + p.region().name()));

								sender.sendMessage(CoreUtils.colorize("&7 - X1&8:&9 " + p.region().x1));
								sender.sendMessage(CoreUtils.colorize("&7 - Y1&8:&9 " + p.region().y1));
								sender.sendMessage(CoreUtils.colorize("&7 - Z1&8:&9 " + p.region().z1));

								sender.sendMessage(CoreUtils.colorize("&7 - X2&8:&9 " + p.region().x2));
								sender.sendMessage(CoreUtils.colorize("&7 - Y2&8:&9 " + p.region().y2));
								sender.sendMessage(CoreUtils.colorize("&7 - Z2&8:&9 " + p.region().z2));

							} else {
								sender.sendMessage(CoreUtils.colorize(CoreUtils.prefixes("portals")
										+ "Sorry, couldn't find any information on portal &f" + args[1] + "&7."));
							}
						} else {
							sender.sendMessage(CoreUtils
									.colorize(CoreUtils.prefixes("portals") + "Usage: &f/portal info <portal>&7."));
						}
						return true;
					}
					if (args[0].equalsIgnoreCase("help")) {
						sender.sendMessage(
								CoreUtils.prefixes("portals") + "Below are some portal commands you can run.");
						sender.sendMessage(CoreUtils.colorize("&9/portal &f- Enters the portal editor."));
						sender.sendMessage(CoreUtils.colorize("&9/portal create <name> &f- Creates a portal."));
						sender.sendMessage(CoreUtils.colorize(
								"&9/portal link <portal1> <portal2> &f- Links a portal to another. &oThis is not 2 way. If you want portals to only link to each other you must run the command twice and switch the portal arguments."));
					}

					if (args[0].equalsIgnoreCase("list")) {
						sender.sendMessage(CoreUtils.colorize(
								CoreUtils.prefixes("portals") + "Listed below are all the existing portals:"));
						for (Portal p : PortalUtils.getPortals()) {
							ComponentBuilder list = new ComponentBuilder(p.name()).color(ChatColor.WHITE)
									.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/portal info " + p.name()))
									.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(new ComponentBuilder(
											ChatColor.translateAlternateColorCodes('&', "&fClick for more info."))
													.create())));
							if (!p.link().equals("")) {
								list.append(" -> ").color(ChatColor.GRAY);
								list.append(p.link())
										.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
												"/portal info " + p.link()))
										.color(ChatColor.BLUE)
										.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
												new Text(
														new ComponentBuilder(ChatColor.translateAlternateColorCodes('&',
																"&fClick for more info.")).create())));
							}
							sender.spigot().sendMessage(list.create());
						}
						return true;
					}
					if (args[0].equalsIgnoreCase("create")) {
						if (args.length == 2) {
							PortalUtils.playerCreatePortal((Player) sender, args[1]);
						} else {
							sender.sendMessage(CoreUtils.prefixes("portals") + "Try /portal create <name>");
						}
					}
					if (args[0].equalsIgnoreCase("link")) {
						if (args.length == 3) {
							if (PortalUtils.getPortal(args[1]) != null) {
								if (PortalUtils.getPortal(args[2]) != null) {
									sender.sendMessage(CoreUtils.colorize(CoreUtils.prefixes("portals")
											+ "Linking portal &f" + args[1] + "&7 with &f" + args[2] + "&7."));
									PortalUtils.getPortal(args[1]).link(args[2]);
									CoreUtils.getPlugin().getConfig().set("Portal." + args[1] + ".link", args[2]);
									CoreUtils.getPlugin().saveConfig();
								} else {
									sender.sendMessage(CoreUtils.colorize(CoreUtils.prefixes("portals")
											+ "Sorry but we couldn't find any portals named &f" + args[2] + "&7."));
								}
							} else {
								sender.sendMessage(CoreUtils.colorize(CoreUtils.prefixes("portals")
										+ "Sorry but we couldn't find any portals named &f" + args[1] + "&7."));
							}
						} else {
							sender.sendMessage(CoreUtils.colorize(CoreUtils.prefixes("portals")
									+ "Try &f'/portal link <portal1> <portal2>'&7. Remember portals don't have to link to each other, so if you want them to be sure to run the command again, just flip the portal names."));
						}
					}

				} else {
					sender.sendMessage(
							CoreUtils.prefixes("portals") + "Sorry you don't have permission to use that command.");
				}
			}
		}
		if (cmd.getName().equalsIgnoreCase("kick")) {
			if (sender.hasPermission(MysticPerms.CMD_KICK)) {
				if (args.length > 0) {
					String a = "";
					if (args.length > 1) {
						int b = 0;
						for (String s : args) {
							if (b != 0)
								a = a == "" ? s : a + " " + s;
							b = b + 1;
						}
					}

					PunishmentUtils.kick(args[0], sender instanceof Player ? sender.getName() : "CONSOLE", a);
				} else {
					sender.sendMessage(CoreUtils.prefixes("Usage: /kick <player> [reason]"));
					return true;
				}
			} else {
				sender.sendMessage(
						CoreUtils.colorize(CoreUtils.prefixes("admin") + "You don't have permission to do that."));
			}
		}

		if (cmd.getName().equalsIgnoreCase("skull")) {

			if (!(sender instanceof Player)) {
				sender.sendMessage(CoreUtils.prefixes("admin") + "Players only.");
				return true;
			}

			if (!sender.hasPermission(MysticPerms.CMD_SKULL)) {
				sender.sendMessage(CoreUtils.prefixes("admin") + "You don't have permission to use that command.");
				return true;
			}

			if (args.length == 0) {
				sender.sendMessage(CoreUtils.prefixes("admin") + "Usage: /skull <dbName|player>");
				return true;
			}
			sender.sendMessage(CoreUtils.prefixes("admin") + "Searching for \"" + args[0] + "\" in the database...");
			ItemStack item = SkullUtils.getSkull(args[0]);
			sender.sendMessage(CoreUtils.prefixes("admin") + (item.getType().equals(Material.SKELETON_SKULL)
					? "Could not find \"" + args[0] + "\" in the player head database."
					: "Found \"" + args[0] + "\"!"));
			((Player) sender).getInventory().addItem(item);
		}

		if (cmd.getName().equalsIgnoreCase("setspawn")) {

			if (sender.hasPermission(MysticPerms.CMD_SETSPAWN)) {
				if (args.length == 0)
					if (sender instanceof Player) {
						CoreUtils.setSpawnLocation(((Player) sender).getLocation());
						sender.sendMessage(CoreUtils.prefixes("admin") + "Set spawn!");
					} else {
						sender.sendMessage(CoreUtils.prefixes("admin") + "Usage: /setspawn <world> <x> <y> <x>");
					}
				return true;

			}
		}

		if (cmd.getName().equalsIgnoreCase("seen")) {
			if (sender.hasPermission(MysticPerms.CMD_SPAWN)) {
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
			if (sender.hasPermission(MysticPerms.CMD_UUID)) {
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
				if (sender.hasPermission(MysticPerms.CMD_SPEED)) {
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
			if (sender.hasPermission(MysticPerms.ADMIN)) {
				for (Plugin pl : Bukkit.getPluginManager().getPlugins()) {
					pls = (!pls.equals("") ? pls + "&7,&3 " : "&3") + (pl.isEnabled() ? "&a" : "&c") + pl.getName();
				}
			}
			if (pls.equals(""))
				pls = "&aMysticCore";
			sender.sendMessage(
					CoreUtils.colorize(msg + pls + org.bukkit.ChatColor.getLastColors(CoreUtils.colorize(msg)) + "."));
		}
		if (cmd.getName().equalsIgnoreCase("votetest")) {
			Map<UUID, Integer> votes = new HashMap<>();
			ResultSet rs = CoreUtils.sendQuery("SELECT * FROM Votes ORDER BY Day DESC");
			try {
				while (rs.next()) {
					if (new Date().getTime() - Long.parseLong(rs.getString("Day")) <= TimeUnit.MILLISECONDS.convert(30,
							TimeUnit.DAYS)) {
						UUID uid = UUID.fromString(rs.getString("UUID"));
						votes.put(uid, (votes.containsKey(uid) ? votes.get(uid) : 0) + 1);
					} else
						break;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			for (PermissionUser user : PermissionsEx.getPermissionManager().getGroup("voter").getUsers()) {
				user.removeGroup("voter");
			}
			int i = 0;
			for (Entry<UUID, Integer> e : CoreUtils.sortByValue(votes).entrySet()) {
				if (i < 5) {
					String s = CoreUtils.lookupUsername(e.getKey());
					if (PermissionsEx.getUser(s).inGroup("default"))
						PermissionsEx.getUser(s).removeGroup("default");
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pex user " + s + " group add voter");
				}
				i = i + 1;
			}

		}
		if (cmd.getName().equalsIgnoreCase("back")) {
			if (sender instanceof Player) {
				if (sender.hasPermission(MysticPerms.CMD_BACK)) {
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
			if (sender.hasPermission(MysticPerms.CMD_SETLEVEL)) {
				if (args.length > 1 && Bukkit.getPlayer(args[0]) == null)
					return true;
				if (args.length == 1 && !(sender instanceof Player))
					return true;
				String a = args.length == 1 ? args[0] : args[1];
				MysticPlayer mp = MysticAccountManager
						.getMysticPlayer(args.length > 1 ? Bukkit.getPlayer(args[0]) : ((Player) sender));
				if (sender instanceof Player)
					mp.setXP(Double.parseDouble(a));
				else
					mp.gainXP(Double.parseDouble(a));
			}

		}
		if (cmd.getName().equalsIgnoreCase("control")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(CoreUtils.prefixes("error") + "Player only command.");
				return false;
			}

			if (sender.hasPermission(MysticPerms.CMD_CONTROL)) {
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
				if (sender.hasPermission(MysticPerms.CMD_DEBUG)) {

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
