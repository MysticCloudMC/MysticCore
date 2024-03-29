package net.mysticcloud.spigot.core.utils.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class EventUtils {

	static Map<Integer, Event> events = new HashMap<>();

	static List<Integer> events__remove = new ArrayList<>();

	public static Entry<Integer, Event> createEvent(String name, EventType type) {
		Event event = new Event(name, type);
		int id = 0;
		while (true) {
			if (!events.containsKey(id))
				break;
			id = id + 1;
		}
		events.put(id, event);
		Entry<Integer, Event> entry = null;
		while (entry == null) {
			for (Entry<Integer, Event> e : events.entrySet()) {
				if (e.getKey().equals(id) && e.getValue().equals(event)) {
					entry = e;
					break;
				}
			}
		}

		return entry;
	}

	public static Entry<Integer, Event> registerExturnalEvent(Event event) {
		int id = 0;
		while (true) {
			if (!events.containsKey(id))
				break;
			id = id + 1;
		}
		events.put(id, event);
		Entry<Integer, Event> entry = null;
		while (entry == null) {
			for (Entry<Integer, Event> e : events.entrySet()) {
				if (e.getKey().equals(id) && e.getValue().equals(event)) {
					entry = e;
					break;
				}
			}
		}

		return entry;

	}

	public static int getEventID(String name) {
		for (Entry<Integer, Event> e : events.entrySet()) {
			if (e.getValue().getName().equalsIgnoreCase(name)) {
				return e.getKey();
			}
		}
		return -1;
	}

	public static Event getEvent(String name) {
		return getEvent(getEventID(name));
	}

	public static Event getEvent(int id) {
		return events.get(id);
	}

	public static Map<Integer, Event> getEvents() {
		return events;
	}

	public static void addRemoveEvent(Integer key) {
		events__remove.add(key);
	}

	public static void removeEvent(Integer key) {
		events.remove(key);
	}

	public static List<Integer> removeEvents() {
		return events__remove;
	}

	public static void clearRemoveEvents() {
		events__remove.clear();
	}

//	public static void startBossEvent(Bosses bosstype, Location loc) {
//		String name = "";
//		if (bosstype.getCallName().contains("_")) {
//			for (String g : bosstype.getCallName().split("_")) {
//				name = name + g.substring(0, 1).toUpperCase() + g.substring(1, g.length()).toLowerCase() + " ";
//			}
//			name = name + "Boss";
//		} else {
//			name = bosstype.getCallName().substring(0, 1).toUpperCase()
//					+ bosstype.getCallName().substring(1, bosstype.getCallName().length()).toLowerCase() + " Boss";
//		}
//		Event e = EventUtils
//				.createEvent(name, bosstype.equals(Bosses.GOBLIN_BOSS) ? EventType.TIMED : EventType.COMPLETION)
//				.getValue();
//		Entity boss;
//		switch (bosstype) {
//		case GOBLIN_BOSS:
//			boss = new GoblinBoss(((CraftWorld) (loc).getWorld()).getHandle());
//			e.setMetadata("DESCRIPTION", "The longer he lives the more loot he drops!");
//			break;
//		case SPIDER_QUEEN_BOSS:
//			boss = new SpiderQueenBoss(((CraftWorld) (loc).getWorld()).getHandle());
//			e.setMetadata("DESCRIPTION", "She spawns minions!");
//			break;
//		case SPIDER_QUEEN_MINION:
//			boss = new SpiderQueenMinion(((CraftWorld) (loc).getWorld()).getHandle());
//			break;
//		case REAPER_BOSS:
//			boss = new ReaperBoss(((CraftWorld) (loc).getWorld()).getHandle());
//			boss.getBukkitEntity().setMetadata("boss", new FixedMetadataValue(Main.getPlugin(), "Reaper"));
//			break;
//		case IRON_BOSS:
//			boss = new IronBoss(((CraftWorld) (loc).getWorld()).getHandle());
//			e.setMetadata("DESCRIPTION", "Do the most damage to get the best rewards! Watch out for rocks!");
//			break;
//		case TEST_CHICKEN:
//			boss = new TestChicken(((CraftWorld) (loc).getWorld()).getHandle());
//			break;
//		default:
//			boss = new TestChicken(((CraftWorld) (loc).getWorld()).getHandle());
//			break;
//		}
//		
//		e.setMetadata("BOSS", boss);
//		e.setMetadata("UUID", boss.getUniqueID());
//		e.setMetadata("LOCATION", loc);
//		e.setMetadata("DURATION", TimeUnit.MILLISECONDS.convert(30, TimeUnit.SECONDS));
//		e.setMetadata("STARTED", CoreUtils.getDate().getTime());
//		BossBar bossbar = Bukkit.createBossBar("Health", BarColor.PINK, BarStyle.SEGMENTED_20);
//		e.setMetadata("BOSSBAR", bossbar);
//
//		EventCheck check = new EventCheck() {
//
//			@Override
//			public boolean check() {
//				if (Bukkit.getEntity((UUID) e.getMetadata("UUID")) != null) {
//					org.bukkit.entity.Entity bos = Bukkit.getEntity((UUID) e.getMetadata("UUID"));
//					for (org.bukkit.entity.Entity en : bos.getNearbyEntities(40, 40, 40)) {
//						if (en instanceof Player) {
//							((BossBar) e.getMetadata("BOSSBAR")).addPlayer((Player) en);
//						}
//					}
//					((BossBar) e.getMetadata("BOSSBAR")).setProgress((double) (((LivingEntity) bos).getHealth()
//							* (100 / ((LivingEntity) bos).getMaxHealth()) / 100));
//					e.overrideMetadata("LOCATION", Bukkit.getEntity((UUID) e.getMetadata("UUID")).getLocation());
//				}
//				if (e.getEventType().equals(EventType.TIMED))
//					return CoreUtils.getDate().getTime()
//							- ((long) e.getMetadata("DURATION")) >= ((long) e.getMetadata("STARTED"))
//							|| Bukkit.getEntity((UUID) e.getMetadata("UUID")) == null;
//				else
//					return Bukkit.getEntity((UUID) e.getMetadata("UUID")) == null;
//			}
//
//			@Override
//			public void start() {
//				MysticEntityUtils.spawnBoss((Entity) e.getMetadata("BOSS"), (Location) e.getMetadata("LOCATION"));
//			}
//
//			@Override
//			public void end() {
//
//				((BossBar) e.getMetadata("BOSSBAR")).removeAll();
//
//				CoreUtils.spawnGem((Location) e.getMetadata("LOCATION"));
//
//				if (e.getEventType().equals(EventType.TIMED) && Bukkit.getEntity((UUID) e.getMetadata("UUID")) != null)
//					((Entity) e.getMetadata("BOSS")).killEntity();
//				int z = MysticEntityUtils.damages.get(((Entity) e.getMetadata("BOSS")).getBukkitEntity().getUniqueId())
//						.size();
//				for (Entry<UUID, Double> entry : MysticEntityUtils
//						.sortScores(((Entity) e.getMetadata("BOSS")).getBukkitEntity().getUniqueId()).entrySet()) {
//					if (z == 1) {
//						MysticAccountManager.getMysticPlayer(entry.getKey()).gainXP(0.5);
//						Bukkit.getPlayer(entry.getKey()).getInventory().addItem(new ItemStack(Material.DIAMOND, 3));
//						Bukkit.getPlayer(entry.getKey()).sendMessage(CoreUtils.prefixes("boss")
//								+ "You did the most damage! You earned 50xp, and 3 diamonds!");
//					}
//					if (z == 2) {
//						MysticAccountManager.getMysticPlayer(entry.getKey()).gainXP(0.35);
//						Bukkit.getPlayer(entry.getKey())
//								.sendMessage(CoreUtils.prefixes("boss") + "You came in second place. You earned 35xp.");
//					}
//					if (z == 3)
//						Bukkit.getPlayer(entry.getKey())
//								.sendMessage(CoreUtils.prefixes("boss") + "You came in 3rd place.");
//				}
//			}
//
//		};
//
//		e.setEventCheck(check);
//		e.start();
//	}

}
