package net.mysticcloud.spigot.core.utils.entities;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.entity.CreatureSpawnEvent;

import net.minecraft.server.v1_15_R1.DamageSource;
import net.minecraft.server.v1_15_R1.EntityTypes;
import net.minecraft.server.v1_15_R1.EntityZombie;
import net.minecraft.server.v1_15_R1.EnumItemSlot;
import net.minecraft.server.v1_15_R1.ItemStack;
import net.minecraft.server.v1_15_R1.Items;
import net.minecraft.server.v1_15_R1.World;

public class GoblinBoss extends EntityZombie {

	public GoblinBoss(World world, EntityTypes<? extends EntityZombie> entityType) {
		this(world);
	}

	public GoblinBoss(EntityTypes<? extends EntityZombie> entityType, World world) {
		this(world);
	}

	public GoblinBoss(World world) {
		super(EntityTypes.ZOMBIE, world);
	}

	public void spawn(Location loc) {
		Bukkit.broadcastMessage("CustomZombie spawned!");
		this.setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
		this.world.addEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);
		setBaby(true);
		setSlot(EnumItemSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
		setSlot(EnumItemSlot.OFFHAND, new ItemStack(Items.IRON_SWORD));
		setSlot(EnumItemSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
	}
	
	@Override
	public boolean isBaby() {
		return true;
	}
	
	@Override
	public boolean damageEntity(DamageSource damagesource, float f) {
		
		getBukkitEntity().getWorld().dropItem(getBukkitEntity().getLocation(), new org.bukkit.inventory.ItemStack(Material.GOLD_INGOT));
		
		return super.damageEntity(damagesource, f);
	}

	

}
