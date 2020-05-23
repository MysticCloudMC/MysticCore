package net.mysticcloud.spigot.core.utils.entities;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent;

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
			new MysticEntityType<SpiderQueenBoss>("spiderqueen", SpiderQueenBoss.class, EntityTypes.SPIDER,
					SpiderQueenBoss::new).register();
			new MysticEntityType<SpiderQueenMinion>("spiderqueenminion", SpiderQueenMinion.class,
					EntityTypes.CAVE_SPIDER, SpiderQueenMinion::new).register();

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
		case SPIDER_QUEEN_BOSS:
			return spawnBoss(new SpiderQueenBoss(((CraftWorld) (loc).getWorld()).getHandle()), loc);
		case SPIDER_QUEEN_MINION:
			return spawnBoss(new SpiderQueenMinion(((CraftWorld) (loc).getWorld()).getHandle()), loc);
		default:
			return spawnBoss(new TestChicken(((CraftWorld) (loc).getWorld()).getHandle()), loc);
		}
	}

	public static LinkedHashMap<UUID, Double> sortScores(UUID entity) {
		List<Entry<UUID, Double>> list = new LinkedList<Entry<UUID, Double>>(damages.get(entity).entrySet());
		Collections.sort(list, new Comparator<Entry<UUID, Double>>() {

			@Override
			public int compare(Entry<UUID, Double> o1, Entry<UUID, Double> o2) {
				// TODO Auto-generated method stub
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});

		LinkedHashMap<UUID, Double> temp = new LinkedHashMap<>();
		for (Entry<UUID, Double> aa : list) {
			temp.put(aa.getKey(), aa.getValue());
		}

		return temp;

	}

	public static Entity spawnBoss(Entity entity, Location loc) {
		boolean spawned = false;

		if (entity instanceof IronBoss) {
			((IronBoss) entity).spawn(loc);
			spawned = true;
		}
		if (entity instanceof TestChicken) {
			((TestChicken) entity).spawn(loc);
			spawned = true;
		}
		if (entity instanceof GoblinBoss) {
			((GoblinBoss) entity).spawn(loc);
			spawned = true;
		}
		if (entity instanceof SpiderQueenBoss) {
			((SpiderQueenBoss) entity).spawn(loc);
			spawned = true;
		}
		if (entity instanceof SpiderQueenMinion) {
			((SpiderQueenMinion) entity).spawn(loc);
			spawned = true;
		}
		if (!spawned) {
			entity.setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
			entity.world.addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
		}
		damages.put(entity.getUniqueID(), new HashMap<UUID, Double>());
		return entity;

	}

}
