package net.mysticcloud.spigot.core.utils.placeholder;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface PlaceholderWorker {

	public abstract String run(Player player);

}
