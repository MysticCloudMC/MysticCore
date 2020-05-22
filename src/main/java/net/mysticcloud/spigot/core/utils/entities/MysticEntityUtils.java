package net.mysticcloud.spigot.core.utils.entities;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;

import net.minecraft.server.v1_15_R1.Entity;
import net.minecraft.server.v1_15_R1.EntityTypes;

public class MysticEntityUtils {

	public static Map<UUID, Map<UUID, Double>> damages = new HashMap<>();

	public static void registerEntities() {
		try {
			new MysticEntityType<GoblinBoss>("goblinboss", GoblinBoss.class, EntityTypes.ZOMBIE, GoblinBoss::new)
					.register();
			new MysticEntityType<TestChicken>("testchicken", TestChicken.class, EntityTypes.CHICKEN, TestChicken::new)
					.register();
			new MysticEntityType<IronBoss>("ironboss", IronBoss.class, EntityTypes.IRON_GOLEM, IronBoss::new)
					.register();

		} catch (IllegalStateException ex) {

		}
	}

	public static Entity spawnBoss(Bosses boss, Location loc) {
		switch (boss) {
		case IRON_BOSS:
			return spawnBoss(new IronBoss(((CraftWorld) (loc).getWorld()).getHandle()), loc);
		case TEST_CHICKEN:
			return spawnBoss(new TestChicken(((CraftWorld) (loc).getWorld()).getHandle()), loc);
		case GOBLIN_BOSS:
			return spawnBoss(new GoblinBoss(((CraftWorld) (loc).getWorld()).getHandle()), loc);
		default:
			return spawnBoss(new TestChicken(((CraftWorld) (loc).getWorld()).getHandle()), loc);
		}
	}

	public static Entity spawnBoss(Entity entity, Location loc) {

		if (entity instanceof IronBoss) {
			((IronBoss) entity).spawn(loc);

		}
		if (entity instanceof TestChicken) {
			((TestChicken) entity).spawn(loc);
		}
		if (entity instanceof GoblinBoss) {
			((GoblinBoss) entity).spawn(loc);
		}
		damages.put(entity.getUniqueID(), new HashMap<UUID, Double>());
		return entity;

	}

}
