package net.mysticcloud.spigot.core.listeners;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import net.mysticcloud.spigot.core.MysticCore;
import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.gui.GuiManager;
import net.mysticcloud.spigot.core.utils.particles.ParticleFormatEnum;
import net.mysticcloud.spigot.core.utils.particles.formats.AngelicFormat;

public class ParticleGUIListener implements Listener {

	public ParticleGUIListener(MysticCore plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {

		if (e.getSlot() == -999)
			return;
		if (e.getClickedInventory() == null)
			return;
		if (GuiManager.getOpenInventory(((Player) e.getWhoClicked())) == "Particles") {
			if (CoreUtils.particles(e.getWhoClicked().getUniqueId()) == null)
				return;
			e.setCancelled(true);
			for (Particle particle : CoreUtils.particles(e.getWhoClicked().getUniqueId()).allowedParticles()) {
				if (e.getCurrentItem().getItemMeta().getDisplayName()
						.equalsIgnoreCase(CoreUtils.colorize(CoreUtils.particlesToString(particle)))) {
					CoreUtils.particles(e.getWhoClicked().getUniqueId()).particle(particle);
					if (particle.getDataType() != Void.class) {
						if (particle.getDataType() == DustOptions.class) {
//							GuiManager.switchInventory(((Player) e.getWhoClicked()),
//									GuiManager.generateParticleColorMenu(((Player) e.getWhoClicked()), particle),
//									"Particle Color");
						}

					}

					else
						e.getWhoClicked().closeInventory();
					return;
				}
			}

		}
		if (GuiManager.getOpenInventory(((Player) e.getWhoClicked())) == "Particle Color") {
			e.setCancelled(true);
			float ps = CoreUtils.particles(e.getWhoClicked().getUniqueId()).getOptions().getFloat("size");
			switch (e.getCurrentItem().getType()) {
			case PINK_DYE:
				CoreUtils.particles(e.getWhoClicked().getUniqueId())
						.setDustOptions(new DustOptions(Color.fromRGB(255, 124, 163), 1));
				e.getWhoClicked().closeInventory();
				break;
			case RED_DYE:
				CoreUtils.particles(e.getWhoClicked().getUniqueId()).setDustOptions(new DustOptions(Color.RED, ps));
				e.getWhoClicked().closeInventory();
				break;
			case ORANGE_DYE:
				CoreUtils.particles(e.getWhoClicked().getUniqueId()).setDustOptions(new DustOptions(Color.ORANGE, ps));
				e.getWhoClicked().closeInventory();
				break;
			case YELLOW_DYE:
				CoreUtils.particles(e.getWhoClicked().getUniqueId()).setDustOptions(new DustOptions(Color.YELLOW, ps));
				e.getWhoClicked().closeInventory();
				break;
			case LIME_DYE:
				CoreUtils.particles(e.getWhoClicked().getUniqueId()).setDustOptions(new DustOptions(Color.LIME, ps));
				e.getWhoClicked().closeInventory();
				break;
			case GREEN_DYE:
				CoreUtils.particles(e.getWhoClicked().getUniqueId()).setDustOptions(new DustOptions(Color.GREEN, ps));
				e.getWhoClicked().closeInventory();
				break;
			case BLUE_DYE:
				CoreUtils.particles(e.getWhoClicked().getUniqueId()).setDustOptions(new DustOptions(Color.TEAL, ps));
				e.getWhoClicked().closeInventory();
				break;
			case PURPLE_DYE:
				CoreUtils.particles(e.getWhoClicked().getUniqueId()).setDustOptions(new DustOptions(Color.PURPLE, ps));
				e.getWhoClicked().closeInventory();
				break;
			case BROWN_DYE:
				CoreUtils.particles(e.getWhoClicked().getUniqueId())
						.setDustOptions(new DustOptions(Color.fromRGB(84, 53, 0), ps));
				e.getWhoClicked().closeInventory();
				break;
			case BLACK_DYE:
				CoreUtils.particles(e.getWhoClicked().getUniqueId()).setDustOptions(new DustOptions(Color.BLACK, ps));
				e.getWhoClicked().closeInventory();
				break;
			case WHITE_DYE:
				CoreUtils.particles(e.getWhoClicked().getUniqueId()).setDustOptions(new DustOptions(Color.WHITE, ps));
				e.getWhoClicked().closeInventory();
				break;
			case MAGMA_CREAM:
				CoreUtils.particles(e.getWhoClicked().getUniqueId()).setDustOptions(new DustOptions(Color.RED, 99));
				e.getWhoClicked().closeInventory();
				break;
			default:
				break;
			}

		}
		if (GuiManager.getOpenInventory(((Player) e.getWhoClicked())) == "Angelic Config") {
			e.setCancelled(true);
			AngelicFormat format = (AngelicFormat) CoreUtils.particles(e.getWhoClicked().getUniqueId());
			switch (e.getCurrentItem().getType()) {
			case WHITE_DYE:
				format.halo.setDustOptions(new DustOptions(Color.YELLOW, format.halo.getOptions().getFloat("size")));
				format.wings.setDustOptions(new DustOptions(Color.WHITE, format.wings.getOptions().getFloat("size")));
				e.getWhoClicked().closeInventory();
				break;
			case RED_DYE:
				format.halo.setDustOptions(new DustOptions(Color.RED, format.halo.getOptions().getFloat("size")));
				format.wings.setDustOptions(new DustOptions(Color.BLACK, format.wings.getOptions().getFloat("size")));
				e.getWhoClicked().closeInventory();
				break;
			case BLACK_DYE:
				format.halo.setDustOptions(new DustOptions(Color.BLACK, format.halo.getOptions().getFloat("size")));
				format.wings.setDustOptions(new DustOptions(Color.RED, format.wings.getOptions().getFloat("size")));
				e.getWhoClicked().closeInventory();
				break;
			case MAGMA_CREAM:
				format.setDustOptions(new DustOptions(Color.BLACK, 99));
				e.getWhoClicked().closeInventory();
				break;
			case FEATHER:
				format.halo.setDustOptions(new DustOptions(Color.WHITE, format.halo.getOptions().getFloat("size")));
				format.wings.setDustOptions(new DustOptions(Color.WHITE, format.wings.getOptions().getFloat("size")));
				e.getWhoClicked().closeInventory();
				break;
			case COAL:
				format.halo.setDustOptions(new DustOptions(Color.BLACK, format.halo.getOptions().getFloat("size")));
				format.wings.setDustOptions(new DustOptions(Color.BLACK, format.wings.getOptions().getFloat("size")));
				e.getWhoClicked().closeInventory();
				break;
			default:
				break;
			}

		}
		if (GuiManager.getOpenInventory(((Player) e.getWhoClicked())) == "Particle Format") {
			e.setCancelled(true);
			if (e.getCurrentItem().getType().equals(Material.BARRIER)) {
				CoreUtils.particlesOff(e.getWhoClicked().getUniqueId());
				e.getWhoClicked().closeInventory();
				return;
			}
			for (ParticleFormatEnum format : ParticleFormatEnum.values()) {
				if (e.getCurrentItem().getType().equals(format.formatter().item().getType())) {
					CoreUtils.particles(e.getWhoClicked().getUniqueId(), format);
					if (format.equals(ParticleFormatEnum.ANGELIC)) {
						GuiManager.switchInventory(((Player) e.getWhoClicked()),
								GuiManager.generateAngelicConfigurations(((Player) e.getWhoClicked())),
								"Angelic Config");
						return;
					}
					if (format.formatter().changeParticle())
						GuiManager.switchInventory(((Player) e.getWhoClicked()),
								GuiManager.generateParticleMenu(((Player) e.getWhoClicked()), format.formatter()),
								"Particles");
					else {
						e.getWhoClicked().closeInventory();
					}
					return;
				}
			}
		}

	}

}
