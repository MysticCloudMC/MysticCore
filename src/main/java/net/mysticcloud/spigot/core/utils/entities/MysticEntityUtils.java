package net.mysticcloud.spigot.core.utils.entities;

import net.minecraft.server.v1_15_R1.EntityTypes;

public class MysticEntityUtils {

	
	public static void registerEntities(){
		new MysticEntityType<TestChicken> ("TestChicken", TestChicken.class, EntityTypes.CHICKEN, TestChicken::new).register();
	}
	
	

}