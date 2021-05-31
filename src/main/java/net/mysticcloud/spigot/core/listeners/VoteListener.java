package net.mysticcloud.spigot.core.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.vexsoftware.votifier.model.VotifierEvent;

import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.accounts.MysticAccountManager;
import net.mysticcloud.spigot.core.utils.accounts.MysticPlayer;

public class VoteListener implements Listener {

	public VoteListener(Main plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onVote(final VotifierEvent e) {
		MysticPlayer player = MysticAccountManager.getMysticPlayer(CoreUtils.LookupUUID(e.getVote().getUsername()));
		player.setBalance(player.getBalance()+1000);
		String cc = ChatColor.getLastColors(CoreUtils.prefixes("vote"));
		player.sendMessage("vote", "Thank you for voting on &7" + e.getVote().getServiceName() + cc + "! Each vote earns you &7$1,000" + cc + ".");
	}
}