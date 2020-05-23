package net.mysticcloud.spigot.core.utils.entities;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.event.entity.CreatureSpawnEvent;

import net.minecraft.server.v1_15_R1.EntityIronGolem;
import net.minecraft.server.v1_15_R1.EntityTypes;
import net.minecraft.server.v1_15_R1.World;
import net.mysticcloud.spigot.core.utils.particles.formats.SelectorFormat;

public class IronBoss extends EntityIronGolem {

	private int z = 1;
	

	private SelectorFormat format = new SelectorFormat();

	public IronBoss(World world, EntityTypes<? extends EntityIronGolem> entityType) {
		this(world);
	}

	public IronBoss(EntityTypes<? extends EntityIronGolem> entityType, World world) {
		this(world);
	}

	public IronBoss(World world) {
		super(EntityTypes.IRON_GOLEM, world);
	}

	public void spawn(Location loc) {
		
		this.setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
		this.world.addEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);
		format.particle(Particle.FLAME);
		format.setHeight(3);
		format.setColumns(4);
		format.setLength(1);
		format.setRadius(1.25);

	}
	
	@Override
	public void movementTick() {
		super.movementTick();
		format.display(new Location(Bukkit.getWorld(world.getWorld().getName()), locX(), locY(), locZ()),z);
		
		
		z = z+1;
		
	}

}
