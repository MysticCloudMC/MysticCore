package net.mysticcloud.spigot.core.runnables;

import java.util.Date;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;

import net.mysticcloud.spigot.core.utils.CoreUtils;

public class GenericCooldownRunnable implements Runnable {

	BossBar bar;
	UUID uid;
	long started;
	double cooldown;
	String name;
	Runnable finish;
	boolean perm;

	public GenericCooldownRunnable(BossBar bar, String name, UUID uid, long started, double cooldown, Runnable finish) {
		this(bar, name, uid, started, cooldown, finish, true);
	}

	public GenericCooldownRunnable(BossBar bar, String name, UUID uid, long started, double cooldown, Runnable finish,
			boolean perm) {
		this.uid = uid;
		this.bar = bar;
		this.started = started;
		this.name = name;
		this.cooldown = cooldown;
		this.finish = finish;
		this.perm = perm;
	}

	@Override
	public void run() {
		double percent = /*
							 * perm ? (Bukkit.getPlayer(uid).hasPermission("mysticcloud.hub." + name +
							 * ".override") ? ((new Date().getTime() - started) / (0.4)) / 10 : ((new
							 * Date().getTime() - started) / (cooldown)) / 10) :
							 */ ((new Date().getTime() - started) / (cooldown * 1000));
		if (Bukkit.getPlayer(uid) == null || percent >= 1) {
			CoreUtils.removeGenericCooldown(uid, name);
			bar.setProgress(0);
			bar.removeAll();
			Bukkit.getScheduler().runTaskLater(CoreUtils.getPlugin(), finish, 1);
			return;
		}
		bar.setProgress(1 - percent);

		Bukkit.getScheduler().runTaskLater(CoreUtils.getPlugin(),
				new GenericCooldownRunnable(bar, name, uid, started, cooldown, finish), 1);

	}

}
