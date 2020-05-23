package net.mysticcloud.spigot.core.utils.entities;

import java.lang.reflect.Field;
import java.util.Map;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;

import com.google.common.collect.BiMap;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.types.Type;

import net.minecraft.server.v1_15_R1.BlockPosition;
import net.minecraft.server.v1_15_R1.DataConverterRegistry;
import net.minecraft.server.v1_15_R1.DataConverterTypes;
import net.minecraft.server.v1_15_R1.Entity;
import net.minecraft.server.v1_15_R1.EntityLiving;
import net.minecraft.server.v1_15_R1.EntityTypes;
import net.minecraft.server.v1_15_R1.EnumCreatureType;
import net.minecraft.server.v1_15_R1.EnumMobSpawn;
import net.minecraft.server.v1_15_R1.IRegistry;
import net.minecraft.server.v1_15_R1.MinecraftKey;
import net.minecraft.server.v1_15_R1.RegistryMaterials;
import net.minecraft.server.v1_15_R1.SharedConstants;

public class MysticEntityType<T extends EntityLiving> {
 
    @Nullable private static Field REGISTRY_MAT_MAP;
   
    static {
        try {
            REGISTRY_MAT_MAP = RegistryMaterials.class.getDeclaredField("c");
        } catch (ReflectiveOperationException err) {
            err.printStackTrace();
            REGISTRY_MAT_MAP = null;
            // technically should only occur if server version changes or jar is modified in "weird" ways
        }
    }
 
    private final MinecraftKey key;
    @SuppressWarnings("unused")
	private final Class<T> clazz;
    private final EntityTypes.b<T> maker;
    private EntityTypes<? super T> parentType;
    private EntityTypes<T> entityType;
    private boolean registered;
 
    public MysticEntityType(String name, Class<T> customEntityClass, EntityTypes<? super T> parentType, EntityTypes.b<T> maker) {
        this.key = MinecraftKey.a(name); // returns null if 256+ chars, non-alphanumeric, or contains uppercase chars
        this.clazz = customEntityClass;
        this.parentType = parentType;
        this.maker = maker;
    }

	public org.bukkit.entity.Entity spawn(Location loc) {
        // Parameters: nmsWorld, initialNBT, customName, spawningPlayer, blockPos, spawnReason, fixPos, flag1(?)
        Entity entity = entityType.spawnCreature(((CraftWorld)loc.getWorld()).getHandle(),
                null, null, null, new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()),
                EnumMobSpawn.EVENT, true, false);
        Bukkit.broadcastMessage("A custom chicken has been spawned!");
        return entity == null ? null : entity.getBukkitEntity();
    }
 
    @SuppressWarnings("unchecked")
	public void register() throws IllegalStateException {
        if (registered || IRegistry.ENTITY_TYPE.getOptional(key).isPresent()) {
            throw new IllegalStateException(String.format
                    ("Unable to register entity with key '%s' as it is already registered.", key));
        }
        // Add custom entity to data fixers map with parent entity's data fixer
        Map<Object, Type<?>> dataTypes = (Map<Object, Type<?>>)DataConverterRegistry.a()
                .getSchema(DataFixUtils.makeKey(SharedConstants.getGameVersion().getWorldVersion()))
                .findChoiceType(DataConverterTypes.ENTITY).types(); // DataConverterTypes.ENTITY in < 1.15.2
        dataTypes.put(
        		key.toString(),
        		dataTypes.get(
        				parentType.
        				h()
        				.toString()
        				.replace("entity/", "")));
        // Add our custom entity to the entity registry map
        EntityTypes.a<T> a = EntityTypes.a.a(maker, EnumCreatureType.CREATURE);
        entityType = a.a(key.getKey());
        IRegistry.a(IRegistry.ENTITY_TYPE, key.getKey(), entityType);
        registered = true;
    }
 
    @SuppressWarnings("unchecked")
    public void unregister() throws IllegalStateException {
        if (!registered) {
                throw new IllegalArgumentException(String.format
                        ("Entity with key '%s' could not be unregistered, as it is not in the registry", key));
        }
        // Remove custom entity from data fixers map
        Map<Object, Type<?>> dataTypes = (Map<Object, Type<?>>)DataConverterRegistry.a()
                .getSchema(DataFixUtils.makeKey(SharedConstants.getGameVersion().getWorldVersion()))
                .findChoiceType(DataConverterTypes.ENTITY).types();
        dataTypes.remove(key.toString());
        try {
            // Remove our custom entity from entity registry map, which takes a little reflection in 1.15
            if (REGISTRY_MAT_MAP == null) {
                throw new ReflectiveOperationException("Field not initially found");
            }
            REGISTRY_MAT_MAP.setAccessible(true);
            Object o = REGISTRY_MAT_MAP.get(IRegistry.ENTITY_TYPE);
            ((BiMap<MinecraftKey, ?>)o).remove(key);
            REGISTRY_MAT_MAP.set(IRegistry.ENTITY_TYPE, o);
            REGISTRY_MAT_MAP.setAccessible(false);
            registered = false;
        } catch (ReflectiveOperationException err) {
            err.printStackTrace();
        }
    }
 
}