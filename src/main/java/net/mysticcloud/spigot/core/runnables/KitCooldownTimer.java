package net.mysticcloud.spigot.core.runnables;

import java.util.Date;
import java.util.UUID;

import org.bukkit.Bukkit;

import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.kits.KitManager;
import net.mysticcloud.spigot.core.kits.KitWrapper;


public class KitCooldownTimer implements Runnable {
	
	UUID uid;
	KitWrapper wrapper;
	
	public KitCooldownTimer(UUID uid, KitWrapper wrapper){
		this.uid = uid;
		this.wrapper = wrapper;
	}

	@Override
	public void run() {
		if((new Date().getTime() - wrapper.started()) >= KitManager.getKit(wrapper.kit()).getCooldown()*1000) {
			KitManager.removeFromCooldown(uid, wrapper.kit());
			return;
		}
		Bukkit.getScheduler().runTaskLaterAsynchronously(Main.getPlugin(), new KitCooldownTimer(uid,wrapper), 20);
	}

}
