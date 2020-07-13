package net.mysticcloud.spigot.core.runnables;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.utils.CoreUtils;

public class BlockRunnable implements Runnable {

	Block block;

	public BlockRunnable(Block block) {
		this.block = block;
	}

	@Override
	public void run() {
		int c = 0;
		for (BlockFace face : new BlockFace[] { BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.EAST,
				BlockFace.SOUTH, BlockFace.WEST }) {
			if (block.getRelative(face).getType().name().endsWith("_CONCRETE")
					&& !block.getRelative(face).getType().equals(block.getType())) {
				c = c + 1;
				if (!CoreUtils.debugOn())
					block.getRelative(face).setType(block.getType());
				else
					block.getWorld().getBlockAt(block.getRelative(face).getLocation()).setType(block.getType());
			}
			continue;
		}
		if (c != 0)
			Bukkit.getScheduler().runTaskLater(Main.getPlugin(), this, 5);
	}

}
