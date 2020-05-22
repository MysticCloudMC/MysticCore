package net.mysticcloud.spigot.core.utils.entities;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;

import net.minecraft.server.v1_15_R1.Entity;
import net.minecraft.server.v1_15_R1.EntityTypes;

public class MysticEntityUtils {
	
	public static Map<UUID,Map<UUID,Double>> damages = new HashMap<>();

	
	public static void registerEntities(){
		try{
			new MysticEntityType<TestChicken> ("testchicken", TestChicken.class, EntityTypes.CHICKEN, TestChicken::new).register();
			new MysticEntityType<TestZombie> ("testzombie", TestZombie.class, EntityTypes.ZOMBIE, TestZombie::new).register();
		} catch(IllegalStateException ex){
			
		}
	}
	
	public static Entity spawnBoss(Entity entity, Location loc) {
		
		if(entity instanceof TestZombie) {
			((TestZombie)entity).spawn(loc);
			
		}
		if(entity instanceof TestChicken) {
			((TestChicken)entity).spawn(loc);
		}
		damages.put(entity.getUniqueID(), new HashMap<UUID,Double>());
		return entity;
		
	}
	
	

}
