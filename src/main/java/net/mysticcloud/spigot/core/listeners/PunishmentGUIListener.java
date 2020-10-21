package net.mysticcloud.spigot.core.listeners;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.craftbukkit.v1_16_R2.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.GUIManager;
import net.mysticcloud.spigot.core.utils.pets.Pet;
import net.mysticcloud.spigot.core.utils.pets.PetManager;
import net.mysticcloud.spigot.core.utils.pets.pet.BabyMooshroom;
import net.mysticcloud.spigot.core.utils.pets.pet.BabyPig;
import net.mysticcloud.spigot.core.utils.pets.pet.Snowman;
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
		if (GUIManager.getOpenInventory(((Player) e.getWhoClicked())) == "Pets") {
			e.setCancelled(true);
			Pet pet = null;
			switch (e.getCurrentItem().getType()) {
			default:
				break;
			case PUMPKIN:
				pet = new Snowman(((CraftWorld)((Player)e.getWhoClicked()).getWorld()).getHandle());
				break;
			case SADDLE:
				pet = new BabyPig(((CraftWorld)((Player)e.getWhoClicked()).getWorld()).getHandle());
				break;
			case RED_MUSHROOM_BLOCK:
				pet = new BabyMooshroom(((CraftWorld)((Player)e.getWhoClicked()).getWorld()).getHandle());
				break;
			}
			
			if(pet != null) {
				PetManager.spawnPet(pet, e.getWhoClicked().getLocation(), ((Player)e.getWhoClicked()));
				e.getWhoClicked().closeInventory();
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

	}

}
