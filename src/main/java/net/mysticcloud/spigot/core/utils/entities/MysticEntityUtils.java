package net.mysticcloud.spigot.core.utils.entities;

import java.util.Map;

import net.minecraft.server.v1_15_R1.EntityTypes;
import net.mysticcloud.spigot.core.utils.reflection.ReflectionUtils;

public class MysticEntityUtils {

	
	public static void registerEntities(){
		for(MysticEntityType type : MysticEntityType.values()){
			register(type.getName(),type.getId(),type.getCustomClass());
		}
	}
	
	public static void register(String name, int id, Class<?> registryClass) {
		

		try {
			((Map) ReflectionUtils.getPrivateField("c", EntityTypes.class, null)).put(name, registryClass);
			((Map) ReflectionUtils.getPrivateField("d", EntityTypes.class, null)).put(registryClass, name);
			((Map) ReflectionUtils.getPrivateField("f", EntityTypes.class, null)).put(registryClass, id);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
