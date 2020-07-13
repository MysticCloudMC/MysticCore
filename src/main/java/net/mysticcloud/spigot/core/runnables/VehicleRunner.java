package net.mysticcloud.spigot.core.runnables;

import org.bukkit.Bukkit;

import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.utils.vehicles.Vehicle;
import net.mysticcloud.spigot.core.utils.vehicles.VehicleManager;

public class VehicleRunner implements Runnable {


	public VehicleRunner() {
		
	}

	@Override
	public void run() {
		if(!(VehicleManager.getVehicles().size() == 0)) {
			for(Vehicle v : VehicleManager.getVehicles().values()) {
				v.move();
			}
		}
		Bukkit.getScheduler().runTaskLater(Main.getPlugin(), this, 1);
		
	}

}
