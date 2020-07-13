package net.mysticcloud.spigot.core.utils.vehicles;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class VehicleManager {
	
	static Map<Integer, Vehicle> vehicles = new HashMap<>();
	
	public static Vehicle createVehicle(EntityType type,Location loc) {
		int id = vehicles.size();
		Vehicle vehicle = new Vehicle(id, type, loc);
		return vehicle;
	}

	public static Map<Integer,Vehicle> getVehicles() {
		return vehicles;
	}

}
