package net.mysticcloud.spigot.core.listeners;

import java.util.Date;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.server.TabCompleteEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.SpawnReason;
import net.mysticcloud.spigot.core.utils.accounts.MysticAccountManager;
import net.mysticcloud.spigot.core.utils.accounts.MysticPlayer;
import net.mysticcloud.spigot.core.utils.accounts.PlayerSettings;
import net.mysticcloud.spigot.core.utils.admin.DebugUtils;
import net.mysticcloud.spigot.core.utils.admin.FoodInfo;
import net.mysticcloud.spigot.core.utils.afk.AFKUtils;
import net.mysticcloud.spigot.core.utils.chat.CustomTag;
import net.mysticcloud.spigot.core.utils.gui.GuiManager;
import net.mysticcloud.spigot.core.utils.particles.formats.CircleFeetFormat;
import net.mysticcloud.spigot.core.utils.placeholder.PlaceholderUtils;
import net.mysticcloud.spigot.core.utils.portals.PortalUtils;
import net.mysticcloud.spigot.core.utils.punishment.Punishment;
import net.mysticcloud.spigot.core.utils.punishment.PunishmentType;
import net.mysticcloud.spigot.core.utils.punishment.PunishmentUtils;
import net.mysticcloud.spigot.core.utils.teleport.TeleportUtils;

public class PlayerListener implements Listener {

	public PlayerListener(JavaPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onTabComplete(TabCompleteEvent e) {
		if (e.getBuffer().contains("/about")) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent e) {
		if (e.getPlayer().hasMetadata("dontsaveteleport"))
			return;
		if (!e.getTo().getWorld().getName().equals(e.getFrom().getWorld().getName())) {
			TeleportUtils.addToHistory(e.getPlayer(), e.getFrom());
			return;
		}
		if (e.getTo().distance(e.getFrom()) > 10) {
			TeleportUtils.addToHistory(e.getPlayer(), e.getFrom());
			return;
		}
	}

	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent e) {
		if (e.getPlayer().hasMetadata("coreteleporting")) {
			TeleportUtils.checkTeleport(e.getPlayer());
		}
//		if (e.getPlayer().hasMetadata("portaling")) {
//			try {
//				if (!((Region) e.getPlayer().getMetadata("portaling").get(0).value()).inside(e.getFrom())) {
//					e.getPlayer().removeMetadata("portaling", CoreUtils.getPlugin());
//				}
//			} catch (Exception ex) {
//				for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
//					e.getPlayer().removeMetadata("portaling", plugin);
//				}
//			}
//		} else {
//			for (Portal portal : PortalUtils.getPortals()) {
//				if (portal.region().inside(e.getTo())) {
//					if (PortalUtils.getPortal(portal.link()) == null) {
//						e.getPlayer().sendMessage(
//								CoreUtils.prefixes("portals") + "Sorry, that portal isn't linked to anything.");
//						e.getPlayer().setMetadata("portaling",
//								new FixedMetadataValue(CoreUtils.getPlugin(), portal.region()));
//						return;
//					}
//					e.getPlayer()
//							.teleport(new Location(
//									Bukkit.getWorld(PortalUtils.getPortal(portal.link()).region().world()),
//									PortalUtils.getPortal(portal.link()).center().getX(),
//									PortalUtils.getPortal(portal.link()).center().getY(),
//									PortalUtils.getPortal(portal.link()).center().getZ(),
//									e.getPlayer().getLocation().getYaw(), e.getPlayer().getLocation().getPitch()));
//					e.getPlayer().setMetadata("portaling",
//							new FixedMetadataValue(CoreUtils.getPlugin(), PortalUtils.getPortal(portal.link()).region()));
//				}
//			}
//		}

		if (AFKUtils.isAFK(e.getPlayer())) {
			if (!(AFKUtils.getAFKPacket(e.getPlayer()) == null)) {
				if (!AFKUtils.getAFKPacket(e.getPlayer()).getLocation().getBlock().getLocation()
						.equals(e.getPlayer().getLocation().getBlock().getLocation())) {
					AFKUtils.afk(e.getPlayer(), false);
				}
			}

		}
		if (CoreUtils.getVoidWorlds().contains(e.getPlayer().getWorld().getName())) {
			if (e.getPlayer().getLocation().getY() <= 0.5) {
				TeleportUtils.teleport(e.getPlayer(), CoreUtils.getSpawnLocation(), false, true);
				e.getPlayer().setMetadata("fell", new FixedMetadataValue(CoreUtils.getPlugin(), "yup"));
				Bukkit.getScheduler().runTaskLater(CoreUtils.getPlugin(), new Runnable() {
					@Override
					public void run() {
						e.getPlayer().removeMetadata("fell", CoreUtils.getPlugin());
					}
				}, 10 * 20);
			}
		}
	}

	@EventHandler
	public void onPlayerPickUpItem(EntityPickupItemEvent e) {
		if (e.getEntity() instanceof Player) {

			if (e.getItem().getItemStack().getItemMeta().equals(CoreUtils.getGemItem().getItemMeta())) {
				MysticAccountManager.getMysticPlayer(((Player) e.getEntity()))
						.addGems(e.getItem().getItemStack().getAmount());
//				e.getItem().teleport(new Location(e.getEntity().getWorld(),0,-1,0));
				e.getItem().remove();
				e.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPreCommand(PlayerCommandPreprocessEvent e) {
		String[] args = e.getMessage().split(" ");
		if (args[0].equalsIgnoreCase("/plugins") || args[0].equalsIgnoreCase("/pl")) {
			e.setCancelled(true);
			String msg = "&ePlugins (&7@&e)&7: ";
			String pls = "";
			if (e.getPlayer().hasPermission("mysticcloud.admin")) {
				for (Plugin pl : Bukkit.getPluginManager().getPlugins()) {
					pls = (!pls.equals("") ? pls + "&7,&3 " : "&3") + (pl.isEnabled() ? "&3" : "&9") + pl.getName();
				}
			}
			e.getPlayer()
					.sendMessage(CoreUtils.colorize(CoreUtils.formatMessage(msg,
							pls.equals("") ? "1" : Bukkit.getPluginManager().getPlugins().length + "")
							+ (pls.equals("") ? "&3MysticCore" : pls)
							+ org.bukkit.ChatColor.getLastColors(CoreUtils.colorize(msg)) + "."));
		}
		if (args[0].equalsIgnoreCase("/help") || args[0].equalsIgnoreCase("/?")) {
			e.setCancelled(true);
			e.getPlayer().sendMessage(
					CoreUtils.colorize("&3---------------------&7[&3&lHelp Menu&7]&3---------------------"));
			e.getPlayer().sendMessage(CoreUtils.colorize(""));
			e.getPlayer().sendMessage(CoreUtils.colorize(
					"&f Need help with your current gamemode? There is always help posted at the &7/spawn&f of every gamemode"));
			e.getPlayer().sendMessage(CoreUtils.colorize(""));
			e.getPlayer().sendMessage(CoreUtils
					.colorize("&f Need to report player/staff abuse? Head onto the forums and let us know please! :)"));
			e.getPlayer().sendMessage(CoreUtils.colorize(""));
			e.getPlayer().sendMessage(
					CoreUtils.colorize("&f Need a referesher on the rules? &7/rules&f should help you out. :)"));
			e.getPlayer().sendMessage(CoreUtils.colorize(""));
			e.getPlayer().sendMessage(
					CoreUtils.colorize("&f Want to apply for staff? You can do that on the forums as-well."));
			e.getPlayer().sendMessage(CoreUtils.colorize(""));
			e.getPlayer().sendMessage(
					CoreUtils.colorize("&f Need to tweak something? &7/settings&f should have everything you need!"));
			e.getPlayer().sendMessage(CoreUtils.colorize(""));
			e.getPlayer().sendMessage(CoreUtils.colorize("&3-----------------------------------------------------"));
		}
	}

//	@EventHandler
//	public void onPlayerChat(AsyncPlayerChatEvent e) {
//		if (e.getMessage().toUpperCase().startsWith("!PCOLOR") && e.getPlayer().hasPermission("mysticcloud.admin")) {
//			String[] args = e.getMessage().split(" ");
//			if (args.length == 3) {
//				CoreUtils.particles(e.getPlayer().getUniqueId()).setDustOptions(new DustOptions(
//						org.bukkit.Color.fromRGB(Integer.parseInt(args[1].split(",")[0]),
//								Integer.parseInt(args[1].split(",")[1]), Integer.parseInt(args[1].split(",")[2])),
//						Float.parseFloat(args[2])));
//			}
//		}
//
//		if (e.getMessage().toUpperCase().startsWith("!PARTICLES") && e.getPlayer().hasPermission("mysticcloud.admin")) {
//			String[] args = e.getMessage().split(" ");
//			if (args.length == 3) {
//
//				CoreUtils.particles.put(e.getPlayer().getUniqueId(),
//						ParticleFormatEnum.valueOf(args[1].toUpperCase()).formatter());
//				CoreUtils.particles(e.getPlayer().getUniqueId()).particle(Particle.valueOf(args[2].toUpperCase()));
//
//			}
//		}
//	}

	@EventHandler
	public void onSignEdit(SignChangeEvent e) {
		for (int i = 0; i != e.getLines().length; i++) {
			e.setLine(i, CoreUtils.colorize(e.getLine(i)));
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		MysticAccountManager.getMysticPlayer(e.getPlayer()).save();
		e.setQuitMessage(CoreUtils.colorize("&3" + e.getPlayer().getName() + "&7 has left the game."));
	}

	// @EventHandler
	// public void onPlayerInteract(PlayerInteractEvent e) {
	// if(e.getAction().equals(Action.LEFT_CLICK_AIR) &&
	// e.getPlayer().isSneaking()) {
	// CoreUtils.particletest = CoreUtils.particletest >
	// Particle.values().length-1 ? 1 : CoreUtils.particletest+1;
	// }
	// }
	//

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerDeath(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player && CoreUtils.coreHandleDamage()) {
			if (((Player) e.getEntity()).getHealth() - e.getDamage() <= 0) {
				e.setCancelled(true);
				CoreUtils.teleportToSpawn((Player) e.getEntity(), SpawnReason.DEATH);
				((Player) e.getEntity()).setHealth(((Player) e.getEntity()).getMaxHealth());
				((Player) e.getEntity()).setFoodLevel(20);

			}
		}
	}

//	@EventHandler
//	public void onEntityDeath(EntityDeathEvent e) {
//		if (MysticEntityUtils.damages.containsKey(e.getEntity().getUniqueId())) {
//			MysticEntityUtils.killBoss(e.getEntity());
//			
//		}
//	}

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
		if (e.getEntity().hasMetadata("pet")) {
			e.setCancelled(true);
			return;
		}
		if (e.getDamager() instanceof Snowball && e.getEntity() instanceof Player) {
			if (e.getDamager().hasMetadata("doesDamage")) {
				e.setDamage(2);
			}
		}
//		if (e.getDamager() instanceof Player)
//			if (MysticEntityUtils.damages.containsKey(e.getEntity().getUniqueId())) {
//				if (!MysticEntityUtils.damages.get(e.getEntity().getUniqueId())
//						.containsKey(e.getDamager().getUniqueId())) {
//					MysticEntityUtils.damages.get(e.getEntity().getUniqueId()).put(e.getDamager().getUniqueId(),
//							e.getDamage());
//				} else {
//					double predamage = MysticEntityUtils.damages.get(e.getEntity().getUniqueId())
//							.get(e.getDamager().getUniqueId());
//					MysticEntityUtils.damages.get(e.getEntity().getUniqueId()).put(e.getDamager().getUniqueId(),
//							(predamage + e.getDamage()));
//				}
//			}
	}

	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent e) {
		for (Punishment punish : PunishmentUtils.getPunishments()) {
			if (punish.getUser().toString().equals("" + e.getPlayer().getUniqueId())) {
				if (punish.getType().equals(PunishmentType.BAN)
						&& punish.getDate() + punish.getDuration() >= new Date().getTime()) {
					e.disallow(Result.KICK_BANNED,
							CoreUtils.colorize("&cYou're are banned for "
									+ CoreUtils.getSimpleTimeFormat(
											punish.getDate() + punish.getDuration() - new Date().getTime())
									+ "\n&f[Reason] " + punish.getNotes()));
					return;

				}
			}
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {

//		int v = ProtocolLibrary.getProtocolManager().getProtocolVersion(e.getPlayer());
//		Bukkit.broadcastMessage(v + " <-- V");
//		String version = "";
//		if (v >= 735)
//			version = "1.16";
//		if (v >= 573 && v < 735)
//			version = "1.15";
//		if (v >= 490 && v < 573)
//			version = "1.14";
//		if (v >= 393 && v < 490)
//			version = "1.13";
//		if (v >= 335 && v < 393)
//			version = "1.12";
//		if (v >= 315 && v < 335)
//			version = "1.11";
//		if (v >= 210 && v < 315)
//			version = "1.10";
//		if (v >= 107 && v < 210)
//			version = "1.9";
//		if (v < 107)
//			version = "1.8 or lower";
		e.setJoinMessage(CoreUtils.colorize("&3" + e.getPlayer().getName() + "&7 has joined."));

		Bukkit.getScheduler().runTaskLater(CoreUtils.getPlugin(), () -> {

			Bukkit.getScheduler().runTaskLater(CoreUtils.getPlugin(), new Runnable() {

				@Override
				public void run() {
					MysticAccountManager.updateMysticPlayer(e.getPlayer().getUniqueId());
					Player player = e.getPlayer();

					player.setPlayerListName(
							CoreUtils.colorize(PlaceholderUtils.replace(player, CoreUtils.playerList("name"))));

					player.setPlayerListHeader(CoreUtils.colorize(CoreUtils.playerList("header")));

					player.setPlayerListFooter(CoreUtils.colorize(CoreUtils.playerList("footer")));
				}

			}, 20);

			for (Entry<UUID, String> entry : CoreUtils.offlineTimedUsers.entrySet()) {

				Bukkit.getScheduler().runTaskLater(CoreUtils.getPlugin(), new Runnable() {

					@Override
					public void run() {
						CoreUtils.offlineTimedUsers.remove(entry.getKey());

					}

				}, 20);

				if (!entry.getKey().equals(e.getPlayer().getUniqueId()))
					continue;

				CoreUtils.removeTimedPermission(e.getPlayer(), entry.getValue());
			}
		}, 1 * 20);

	}

	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent e) {
//		if (e.getRightClicked().hasMetadata("pet")) {
//			Pet pet = PetManager.getPet(e.getPlayer().getUniqueId());
//			if (pet.isMountable() && pet.getPetOwner().equals(e.getPlayer().getName())) {
//				e.getRightClicked().addPassenger(e.getPlayer());
//			}
//
//		}
		if (DebugUtils.isDebugger(e.getPlayer().getUniqueId())) {
			CoreUtils.particles.put(e.getRightClicked().getUniqueId(), new CircleFeetFormat());
			CoreUtils.debug("Added entity to particle list.");
		}
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent e) {
		if (e.getEntity().hasMetadata("boss")
				&& ((String) e.getEntity().getMetadata("boss").get(0).value()).equalsIgnoreCase("reaper")) {
			if (e.getCause().equals(DamageCause.FIRE) || e.getCause().equals(DamageCause.FIRE_TICK)
					|| e.getCause().equals(DamageCause.ENTITY_EXPLOSION)) {

				e.setCancelled(true);
			}
		}
		if (e.getEntity().hasMetadata("pet")) {
			e.setCancelled(true);
			return;
		}
	}

	@EventHandler
	public void omPlayerArmorStandManipulate(PlayerArmorStandManipulateEvent e) {
		if (e.getRightClicked().hasMetadata("locked")) {
			e.setCancelled(true);
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (PortalUtils.isEditing(e.getPlayer())) {
			if (e.getClickedBlock() != null) {
				if (e.getAction().name().startsWith("LEFT_CLICK")) {
					e.getPlayer().sendMessage(CoreUtils.prefixes("portals") + CoreUtils.colorize("Position 1 set."));
					PortalUtils.setEditorData(e.getPlayer(), "x1", e.getClickedBlock().getX());
					PortalUtils.setEditorData(e.getPlayer(), "y1", e.getClickedBlock().getY());
					PortalUtils.setEditorData(e.getPlayer(), "z1", e.getClickedBlock().getZ());
					PortalUtils.setEditorData(e.getPlayer(), "world", e.getPlayer().getWorld().getName());
				}
				if (e.getAction().name().startsWith("RIGHT_CLICK")) {
					e.getPlayer().sendMessage(CoreUtils.prefixes("portals") + CoreUtils.colorize("Position 2 set."));
					PortalUtils.setEditorData(e.getPlayer(), "x2", e.getClickedBlock().getX());
					PortalUtils.setEditorData(e.getPlayer(), "y2", e.getClickedBlock().getY());
					PortalUtils.setEditorData(e.getPlayer(), "z2", e.getClickedBlock().getZ());
					PortalUtils.setEditorData(e.getPlayer(), "world", e.getPlayer().getWorld().getName());
				}
				e.setCancelled(true);
				return;
			}
		}
		if (e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
			if (e.getItem() == null)
				return;
			if (!e.getItem().hasItemMeta())
				return;

			if (CoreUtils.isFood(e.getItem())) {
				CoreUtils.debug("This is food.");

				if (e.getPlayer().getFoodLevel() < 20 && !e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
					FoodInfo info = CoreUtils.getFood(e.getItem());
					try {
						e.getPlayer().setHealth(e.getPlayer().getHealth() + info.getHealingFactor());
					} catch (IllegalArgumentException ex) {
						e.getPlayer().setHealth(e.getPlayer().getMaxHealth());
					}
					e.getPlayer().setFoodLevel(e.getPlayer().getFoodLevel() + info.getHungerLevel());
					if (info.getPotionEffects().size() != 0) {
						for (PotionEffect p1 : info.getPotionEffects()) {
							e.getPlayer().addPotionEffect(p1);
						}
					}
					e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_PLAYER_BURP, 1, 1);
					if (e.getItem().getAmount() > 1)
						e.getItem().setAmount(e.getItem().getAmount() - 1);
					else {
						e.getPlayer().setItemInHand(new ItemStack(Material.AIR));
					}
				}
				e.setCancelled(true);
				return;
			}
			if (e.getItem().getItemMeta().hasLore()) {
				for (String s : e.getItem().getItemMeta().getLore()) {
					if (ChatColor.stripColor(s).contains("Fireball Damage:")) {
						e.getPlayer().launchProjectile(Fireball.class);
					}
				}
			}
		}

	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		try {
			GuiManager.closeInventory((Player) e.getPlayer());
		} catch (Exception ex) {
			// this is stupid.
			CoreUtils.debug("Inventories probably didn't update.");
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {

		if (e.getSlot() == -999)
			return;
		if (e.getClickedInventory() == null)
			return;

		if (GuiManager.getOpenInventory(((Player) e.getWhoClicked())) == "tags") {
			if (e.getCurrentItem() == null) {
				e.setCancelled(true);
				return;
			}
			if (!e.getCurrentItem().hasItemMeta()) {
				e.setCancelled(true);
				return;
			}
			if (!e.getCurrentItem().getItemMeta().hasDisplayName()) {
				e.setCancelled(true);
				return;
			}

			if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).contains("Page ")) {
				GuiManager.switchInventory((Player) e.getWhoClicked(),
						GuiManager.getTagsMenu((Player) e.getWhoClicked(), Integer.parseInt(ChatColor
								.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).split("age ")[1])),
						"tags");
				return;
			}

			for (String key : CustomTag.keys()) {
				if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase(key)) {
					CoreUtils.setTag((Player) e.getWhoClicked(), key);
					GuiManager.closeInventory((Player) e.getWhoClicked());
				}
			}
			if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName())
					.equalsIgnoreCase("remove tag")) {
				CoreUtils.removeTag(((Player) e.getWhoClicked()));
				GuiManager.closeInventory((Player) e.getWhoClicked());
			}
			e.setCancelled(true);

		}

		if (GuiManager.getOpenInventory(((Player) e.getWhoClicked())) == "Particle Settings") {
			MysticPlayer mp = MysticAccountManager.getMysticPlayer((Player) e.getWhoClicked());
			if (e.getCurrentItem().getType().equals(Material.FIREWORK_ROCKET)) {
				mp.setSetting(PlayerSettings.HOLIDAY_PARTICLES,
						mp.getSetting(PlayerSettings.HOLIDAY_PARTICLES).equalsIgnoreCase("true") ? "false" : "true");
				GuiManager.openInventory((Player) e.getWhoClicked(),
						GuiManager.getParticleSettingsMenu((Player) e.getWhoClicked()), "Particle Settings");
			}
			if (e.getCurrentItem().getType().equals(Material.CHEST)) {
				mp.setSetting(PlayerSettings.COSMETIC_PARTICLES,
						mp.getSetting(PlayerSettings.COSMETIC_PARTICLES).equalsIgnoreCase("true") ? "false" : "true");
				GuiManager.openInventory((Player) e.getWhoClicked(),
						GuiManager.getParticleSettingsMenu((Player) e.getWhoClicked()), "Particle Settings");
			}
			if (e.getCurrentItem().getType().equals(Material.GRASS_BLOCK)) {
				mp.setSetting(PlayerSettings.REGIONAL_PARTICLES,
						mp.getSetting(PlayerSettings.REGIONAL_PARTICLES).equalsIgnoreCase("true") ? "false" : "true");
				GuiManager.openInventory((Player) e.getWhoClicked(),
						GuiManager.getParticleSettingsMenu((Player) e.getWhoClicked()), "Particle Settings");
			}
		}
		if (GuiManager.getOpenInventory(((Player) e.getWhoClicked())) == "Settings Menu") {
			MysticPlayer mp = MysticAccountManager.getMysticPlayer((Player) e.getWhoClicked());
			if (e.getCurrentItem().getType().equals(Material.DIAMOND)) {
				GuiManager.openInventory((Player) e.getWhoClicked(),
						GuiManager.getParticleSettingsMenu((Player) e.getWhoClicked()), "Particle Settings");
			}
			if (e.getCurrentItem().getType().equals(Material.PAPER)) {
				mp.setSetting(PlayerSettings.SIDEBAR,
						mp.getSetting(PlayerSettings.SIDEBAR).equalsIgnoreCase("true") ? "false" : "true");

				GuiManager.openInventory(((Player) e.getWhoClicked()),
						GuiManager.getSettingsMenu(((Player) e.getWhoClicked())), "Settings Menu");
			}
			if (e.getCurrentItem().getType().equals(Material.BOOK)) {
				mp.setSetting(PlayerSettings.EXTRA_MESSAGES,
						mp.getSetting(PlayerSettings.EXTRA_MESSAGES).equalsIgnoreCase("true") ? "false" : "true");

				GuiManager.openInventory(((Player) e.getWhoClicked()),
						GuiManager.getSettingsMenu(((Player) e.getWhoClicked())), "Settings Menu");
			}
		}

	}

}
