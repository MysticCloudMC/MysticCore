package net.mysticcloud.spigot.core.utils.entities;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent;

import net.minecraft.server.v1_15_R1.EntityChicken;
import net.minecraft.server.v1_15_R1.EntityTypes;

public class TestChicken extends EntityChicken {

	public TestChicken(org.bukkit.World world) {
        // I like to use the bukkit world since that makes thing easier
        super(EntityTypes.CHICKEN, ((CraftWorld) world).getHandle());
    }

	public void spawn(Location loc) {
		Bukkit.broadcastMessage("CustomChicken spawned!");
		this.setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
		this.world.addEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);
	}

}
