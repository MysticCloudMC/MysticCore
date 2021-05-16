package net.mysticcloud.spigot.core.utils.entities;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_16_R2.DamageSource;
import net.minecraft.server.v1_16_R2.EntityTypes;
import net.minecraft.server.v1_16_R2.EntityZombie;
import net.minecraft.server.v1_16_R2.EnumItemSlot;
import net.minecraft.server.v1_16_R2.Items;
import net.minecraft.server.v1_16_R2.World;
import net.mysticcloud.spigot.core.utils.CoreUtils;

public class ReaperBoss extends EntityZombie {

	private int z = 0;

	private ArmorStand armor;

	public ReaperBoss(World world, EntityTypes<? extends EntityZombie> entityType) {
		this(world);
	}

	public ReaperBoss(EntityTypes<? extends EntityZombie> entityType, World world) {
		this(world);
	}

	public ReaperBoss(World world) {
		super(EntityTypes.ZOMBIE, world);
	}

	public void spawn(Location loc) {
		armor = loc.getWorld().spawn(loc, ArmorStand.class);
		armor.setGravity(false);
		armor.setHelmet(new ItemStack(Material.ZOMBIE_HEAD));
		armor.setVisible(false);
		this.setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
		this.world.addEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);
//		setBaby(true);
		getBukkitEntity().setCustomName(CoreUtils.colorize("&6&l" + Bosses.REAPER_BOSS.getFormattedCallName()));
		setCustomNameVisible(true);
//		setSlot(EnumItemSlot.HEAD, new ItemStack(Items.GOLDEN_HELMET));
//		setSlot(EnumItemSlot.OFFHAND, new ItemStack(Items.GOLDEN_SWORD));
//		setSlot(EnumItemSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
	}

	@Override
	public boolean isBaby() {
		return true;
	}

	@Override
	public void movementTick() {
		super.movementTick();
		armor.teleport(getBukkitEntity().getLocation().clone().add(
				Math.cos(Math.toRadians(getBukkitEntity().getLocation().getYaw())), 0,
				Math.sin(Math.toRadians(getBukkitEntity().getLocation().getYaw()))));
		z = z + 1;
	}

	@Override
	protected void dropDeathLoot(DamageSource damagesource, int i, boolean flag) {
		armor.remove();
		// TODO Auto-generated method stub
		super.dropDeathLoot(damagesource, i, flag);
	}

	@Override
	public boolean damageEntity(DamageSource damagesource, float f) {

		return super.damageEntity(damagesource, f);
	}

}
