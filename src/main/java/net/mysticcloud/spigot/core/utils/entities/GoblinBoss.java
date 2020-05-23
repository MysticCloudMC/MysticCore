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
import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.particles.formats.CircleFeetFormat;

public class GoblinBoss extends EntityZombie {

	private int z = 0;

	private CircleFeetFormat format = new CircleFeetFormat();

	private org.bukkit.inventory.ItemStack[] damageDrops = new org.bukkit.inventory.ItemStack[] {
			new org.bukkit.inventory.ItemStack(Material.GOLD_INGOT),
			new org.bukkit.inventory.ItemStack(Material.IRON_INGOT),
			new org.bukkit.inventory.ItemStack(Material.GOLD_NUGGET),
			new org.bukkit.inventory.ItemStack(Material.IRON_NUGGET) };

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
		format.setHeight(1);
		format.setRadius(0.5);
		format.setSpots(20);
		CoreUtils.entityparticles.put(getBukkitEntity().getUniqueId(), format);
		setBaby(true);
		setSlot(EnumItemSlot.HEAD, new ItemStack(Items.GOLDEN_HELMET));
		setSlot(EnumItemSlot.OFFHAND, new ItemStack(Items.GOLDEN_SWORD));
		setSlot(EnumItemSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
	}

	@Override
	public boolean isBaby() {
		return true;
	}

	@Override
	public void movementTick() {
		super.movementTick();

		if (z % 100 == 0) {
			getBukkitEntity().getWorld().dropItem(getBukkitEntity().getLocation(),
					damageDrops[CoreUtils.getRandom().nextInt(damageDrops.length)]);
		}
		
		format.display(getBukkitEntity().getLocation(), z);
		z = z + 1;
	}

	@Override
	public boolean damageEntity(DamageSource damagesource, float f) {

		getBukkitEntity().getWorld().dropItem(getBukkitEntity().getLocation(),
				damageDrops[CoreUtils.getRandom().nextInt(damageDrops.length)]);

		return super.damageEntity(damagesource, f);
	}

}
