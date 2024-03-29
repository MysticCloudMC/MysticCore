package net.mysticcloud.spigot.core.listeners;

import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.vexsoftware.votifier.model.VotifierEvent;

import net.mysticcloud.spigot.core.MysticCore;
import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.accounts.MysticAccountManager;
import net.mysticcloud.spigot.core.utils.accounts.MysticPlayer;
import net.mysticcloud.spigot.core.utils.admin.DebugUtils;

public class VoteListener implements Listener {

	public VoteListener(MysticCore plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onVote(final VotifierEvent e) {
		MysticPlayer player = MysticAccountManager.getMysticPlayer(CoreUtils.LookupUUID(e.getVote().getUsername()));
		player.setBalance(player.getBalance() + 1000);
		String cc = ChatColor.getLastColors(CoreUtils.prefixes("vote"));
		player.sendMessage("vote", "Thank you for voting on &7" + e.getVote().getServiceName() + cc
				+ "! Each vote earns you &7$1,000" + cc + ".");
		DebugUtils.debug("" + CoreUtils.sendInsert("INSERT INTO voters (uuid,service,day) VALUES ('" + player.getUUID()
				+ "','" + e.getVote().getServiceName() + "','" + new Date().getTime() + "');"));
		Bukkit.dispatchCommand(Bukkit.getPlayer(e.getVote().getUsername()), "votetest");
	}
}
