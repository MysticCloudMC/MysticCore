package net.mysticcloud.spigot.core.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.kits.Kit;
import net.mysticcloud.spigot.core.kits.KitManager;
import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.GUIManager;
import net.mysticcloud.spigot.core.utils.particles.ParticleFormatEnum;
import net.mysticcloud.spigot.core.utils.pets.v1_15_R1.Pet;
import net.mysticcloud.spigot.core.utils.pets.v1_15_R1.PetManager;
import net.mysticcloud.spigot.core.utils.punishment.InfringementSeverity;
import net.mysticcloud.spigot.core.utils.punishment.InfringementType;
import net.mysticcloud.spigot.core.utils.punishment.PunishmentUtils;

public class PunishmentGUIListener implements Listener {
	
	public PunishmentGUIListener(Main plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {

		if (e.getSlot() == -999)
			return;
		if (e.getClickedInventory() == null)
			return;
		if (e.getInventory().getHolder() == null)
			e.setCancelled(true);
		if (GUIManager.getOpenInventory(((Player) e.getWhoClicked())) == "OffenceTypes") {
			e.setCancelled(true);
			switch (e.getCurrentItem().getType()) {
			default:
				break;
			case ENCHANTED_GOLDEN_APPLE:
				GUIManager.switchInventory((Player) e.getWhoClicked(), PunishmentUtils.getHackingPunishmentGUI(),
						"HackingSeverity");
				break;
			case DIAMOND_SWORD:
				GUIManager.switchInventory((Player) e.getWhoClicked(), PunishmentUtils.getThreatsPunishmentGUI(),
						"ThreatsSeverity");
				break;
			case PAPER:
				GUIManager.switchInventory((Player) e.getWhoClicked(), PunishmentUtils.getChatPunishmentGUI(),
						"ChatSeverity");
				break;
			}
		}
		if (GUIManager.getOpenInventory(((Player) e.getWhoClicked())) == "PunishmentNotes") {
			e.setCancelled(true);
			if (e.getCurrentItem().getType().equals(Material.PAPER)) {
				e.getWhoClicked().closeInventory();

			}
		}
		if (GUIManager.getOpenInventory(((Player) e.getWhoClicked())) == "HackingSeverity") {
			String name = e.getWhoClicked().getMetadata("punish").get(0).value() + "";
			switch (e.getCurrentItem().getType()) {
			default:
				break;
			case CYAN_DYE:
				PunishmentUtils.getNotesGUI(e.getWhoClicked().getName(), CoreUtils.LookupUUID(name),
						InfringementType.HACKING, InfringementSeverity.MINIMAL, "");
				break;
			case LIME_DYE:
				PunishmentUtils.getNotesGUI(e.getWhoClicked().getName(), CoreUtils.LookupUUID(name),
						InfringementType.HACKING, InfringementSeverity.LOW, "");
				break;
			case YELLOW_DYE:
				PunishmentUtils.getNotesGUI(e.getWhoClicked().getName(), CoreUtils.LookupUUID(name),
						InfringementType.HACKING, InfringementSeverity.MEDIUM, "");
				break;
			case ORANGE_DYE:
				PunishmentUtils.getNotesGUI(e.getWhoClicked().getName(), CoreUtils.LookupUUID(name),
						InfringementType.HACKING, InfringementSeverity.HIGH, "");
				break;
			case RED_DYE:
				GUIManager.switchInventory(
						(Player) e.getWhoClicked(), PunishmentUtils.getNotesGUI(e.getWhoClicked().getName(),
								CoreUtils.LookupUUID(name), InfringementType.HACKING, InfringementSeverity.EXTREME, ""),
						"PunishmentNotes");
				break;
			}

		}
		if (GUIManager.getOpenInventory(((Player) e.getWhoClicked())) == "ThreatsSeverity") {
			String name = e.getWhoClicked().getMetadata("punish").get(0).value() + "";
			switch (e.getCurrentItem().getType()) {
			default:
				break;
			case CYAN_DYE:
				PunishmentUtils.getNotesGUI(e.getWhoClicked().getName(), CoreUtils.LookupUUID(name),
						InfringementType.THREAT, InfringementSeverity.MINIMAL, "");
			case LIME_DYE:
				PunishmentUtils.getNotesGUI(e.getWhoClicked().getName(), CoreUtils.LookupUUID(name),
						InfringementType.THREAT, InfringementSeverity.LOW, "");
				break;
			case YELLOW_DYE:
				PunishmentUtils.getNotesGUI(e.getWhoClicked().getName(), CoreUtils.LookupUUID(name),
						InfringementType.THREAT, InfringementSeverity.MEDIUM, "");
				break;
			case ORANGE_DYE:
				PunishmentUtils.getNotesGUI(e.getWhoClicked().getName(), CoreUtils.LookupUUID(name),
						InfringementType.THREAT, InfringementSeverity.HIGH, "");
				break;
			case RED_DYE:
				PunishmentUtils.getNotesGUI(e.getWhoClicked().getName(), CoreUtils.LookupUUID(name),
						InfringementType.THREAT, InfringementSeverity.EXTREME, "");
				break;
			}

		}
		if (GUIManager.getOpenInventory(((Player) e.getWhoClicked())) == "ChatSeverity") {
			e.setCancelled(true);
			String name = e.getWhoClicked().getMetadata("punish").get(0).value() + "";
			switch (e.getCurrentItem().getType()) {
			default:
				break;
			case PINK_DYE:
				PunishmentUtils.getNotesGUI(e.getWhoClicked().getName(), CoreUtils.LookupUUID(name),
						InfringementType.CHAT, InfringementSeverity.LOW, "Spam. ");
				break;
			case ORANGE_DYE:
				PunishmentUtils.getNotesGUI(e.getWhoClicked().getName(), CoreUtils.LookupUUID(name),
						InfringementType.CHAT, InfringementSeverity.LOW, "Being disrespectful. ");
				break;
			case RED_DYE:
				PunishmentUtils.getNotesGUI(e.getWhoClicked().getName(), CoreUtils.LookupUUID(name),
						InfringementType.CHAT, InfringementSeverity.LOW, "Advertising. ");
				break;
			}
		}
		if (GUIManager.getOpenInventory(((Player) e.getWhoClicked())) == "Particles") {
			if (CoreUtils.particles(e.getWhoClicked().getUniqueId()) == null)
				return;
			for (Particle particle : CoreUtils.particles(e.getWhoClicked().getUniqueId()).allowedParticles()) {
				if (e.getCurrentItem().getItemMeta().getDisplayName()
						.equalsIgnoreCase(CoreUtils.colorize(CoreUtils.particlesToString(particle)))) {
					CoreUtils.particles(e.getWhoClicked().getUniqueId()).particle(particle);
					if (particle.getDataType() != Void.class) {
						if (particle.getDataType() == DustOptions.class)
							GUIManager.switchInventory(((Player) e.getWhoClicked()),
									GUIManager.generateParticleColorMenu(((Player) e.getWhoClicked()), particle),
									"Particle Color");

					}

					else
						e.getWhoClicked().closeInventory();
					return;
				}
			}

		}
		if (GUIManager.getOpenInventory(((Player) e.getWhoClicked())) == "Particle Color") {
			e.setCancelled(true);
			switch (e.getCurrentItem().getType()) {
			case PINK_DYE:
				CoreUtils.particles(e.getWhoClicked().getUniqueId())
						.setDustOptions(new DustOptions(Color.fromRGB(255, 124, 163), 1));
				e.getWhoClicked().closeInventory();
				break;
			case RED_DYE:
				CoreUtils.particles(e.getWhoClicked().getUniqueId()).setDustOptions(new DustOptions(Color.RED, 1));
				e.getWhoClicked().closeInventory();
				break;
			case ORANGE_DYE:
				CoreUtils.particles(e.getWhoClicked().getUniqueId()).setDustOptions(new DustOptions(Color.ORANGE, 1));
				e.getWhoClicked().closeInventory();
				break;
			case YELLOW_DYE:
				CoreUtils.particles(e.getWhoClicked().getUniqueId()).setDustOptions(new DustOptions(Color.YELLOW, 1));
				e.getWhoClicked().closeInventory();
				break;
			case LIME_DYE:
				CoreUtils.particles(e.getWhoClicked().getUniqueId()).setDustOptions(new DustOptions(Color.LIME, 1));
				e.getWhoClicked().closeInventory();
				break;
			case GREEN_DYE:
				CoreUtils.particles(e.getWhoClicked().getUniqueId()).setDustOptions(new DustOptions(Color.GREEN, 1));
				e.getWhoClicked().closeInventory();
				break;
			case BLUE_DYE:
				CoreUtils.particles(e.getWhoClicked().getUniqueId()).setDustOptions(new DustOptions(Color.TEAL, 1));
				e.getWhoClicked().closeInventory();
				break;
			case PURPLE_DYE:
				CoreUtils.particles(e.getWhoClicked().getUniqueId()).setDustOptions(new DustOptions(Color.PURPLE, 1));
				e.getWhoClicked().closeInventory();
				break;
			case BROWN_DYE:
				CoreUtils.particles(e.getWhoClicked().getUniqueId())
						.setDustOptions(new DustOptions(Color.fromRGB(84, 53, 0), 1));
				e.getWhoClicked().closeInventory();
				break;
			case BLACK_DYE:
				CoreUtils.particles(e.getWhoClicked().getUniqueId()).setDustOptions(new DustOptions(Color.BLACK, 1));
				e.getWhoClicked().closeInventory();
				break;
			case WHITE_DYE:
				CoreUtils.particles(e.getWhoClicked().getUniqueId()).setDustOptions(new DustOptions(Color.WHITE, 1));
				e.getWhoClicked().closeInventory();
				break;
			}

		}
		if (GUIManager.getOpenInventory(((Player) e.getWhoClicked())) == "Particle Format") {
			e.setCancelled(true);
			for (ParticleFormatEnum format : ParticleFormatEnum.values()) {
				if (e.getCurrentItem().getType().equals(format.formatter().item().getType())) {
					CoreUtils.particles(e.getWhoClicked().getUniqueId(), format);
					if (format.formatter().changeParticle())
						GUIManager.switchInventory(((Player) e.getWhoClicked()),
								GUIManager.generateParticleMenu(((Player) e.getWhoClicked()), format.formatter()),
								"Particles");
					else {
						e.getWhoClicked().closeInventory();
					}
					return;
				}
			}
		}
		if (GUIManager.getOpenInventory(((Player) e.getWhoClicked())) == "Kits") {
			e.setCancelled(true);
			if (e.getCurrentItem().getType().equals(Material.AIR))
				return;
			if (!e.getCurrentItem().hasItemMeta())
				return;
			for (Kit kit : KitManager.getKits()) {
				if (e.getCurrentItem().getItemMeta().getDisplayName().equals(kit.getDisplayName())) {
					KitManager.applyKit(((Player) e.getWhoClicked()), kit.getName());
				}
			}

			e.getWhoClicked().closeInventory();
		}
		if (GUIManager.getOpenInventory(((Player) e.getWhoClicked())) == "Settings") {
			if (e.getCurrentItem().getType().equals(Material.DIAMOND)) {
				CoreUtils.toggleParticles((Player) e.getWhoClicked());
				e.getWhoClicked().closeInventory();
			}
		}

		if (GUIManager.getOpenInventory(((Player) e.getWhoClicked())) == "Pets") {
			e.setCancelled(true);
			if (e.getCurrentItem() == null)
				return;
			if (PetManager.getPetType(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName())) == null)
				return;
			Pet p = PetManager.spawnPet(((Player) e.getWhoClicked()),
					ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()),
					((Player) e.getWhoClicked()).getLocation());
			e.getWhoClicked().closeInventory();
			((ArmorStand) p).setVisible(false);
		}
	}

}
