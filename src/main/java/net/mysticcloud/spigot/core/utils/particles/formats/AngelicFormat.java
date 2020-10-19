package net.mysticcloud.spigot.core.utils.particles.formats;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.inventory.ItemStack;

import net.mysticcloud.spigot.core.utils.particles.ParticleFormat;
import net.mysticcloud.spigot.core.utils.particles.ParticleFormatEnum;

public class AngelicFormat extends ParticleFormat {

	HaloFormat halo = new HaloFormat();
	WingsFormat wings = (WingsFormat) ParticleFormatEnum.WINGS.formatter();

	public AngelicFormat() {
		changeParticle = true;
		allowedParticles.add(Particle.REDSTONE);

		name = "&e&lDouble &eHelix";
		guiItem = new ItemStack(Material.BONE);

		particle = Particle.REDSTONE;
	}

	@Override
	public void setDustOptions(DustOptions dustoptions) {
		super.setDustOptions(dustoptions);
		halo.setDustOptions(dustoptions);
		wings.setDustOptions(dustoptions);
	}

	@Override
	public void display(UUID uid) {
		if (Bukkit.getPlayer(uid) != null) {
			display(Bukkit.getPlayer(uid).getLocation());
		}
	}

	@Override
	public void display(Location loc) {
		super.display(loc);
		halo.setLifetime(i);
		wings.setLength(i);
		halo.display(loc);
		wings.display(loc);
	}

}
