package net.mysticcloud.spigot.core;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.mysticcloud.spigot.core.commands.AdminCommands;
import net.mysticcloud.spigot.core.commands.BossCommand;
import net.mysticcloud.spigot.core.commands.CoreCommands;
import net.mysticcloud.spigot.core.commands.EconomyCommand;
import net.mysticcloud.spigot.core.commands.FriendCommand;
import net.mysticcloud.spigot.core.commands.GRLCommand;
import net.mysticcloud.spigot.core.commands.GamemodeCommand;
import net.mysticcloud.spigot.core.commands.ItemCommand;
import net.mysticcloud.spigot.core.commands.KitCommand;
import net.mysticcloud.spigot.core.commands.PlayerListCommand;
import net.mysticcloud.spigot.core.commands.RegisterCommand;
import net.mysticcloud.spigot.core.commands.ReportCommand;
import net.mysticcloud.spigot.core.commands.SQLCommand;
import net.mysticcloud.spigot.core.commands.SudoCommand;
import net.mysticcloud.spigot.core.commands.TagCommand;
import net.mysticcloud.spigot.core.commands.TeleportCommand;
import net.mysticcloud.spigot.core.commands.UpdateCommand;
import net.mysticcloud.spigot.core.commands.WarpCommand;
import net.mysticcloud.spigot.core.kits.KitManager;
import net.mysticcloud.spigot.core.listeners.MessageListener;
import net.mysticcloud.spigot.core.listeners.ParticleGUIListener;
import net.mysticcloud.spigot.core.listeners.PlayerListener;
import net.mysticcloud.spigot.core.listeners.ReportGUIListener;
import net.mysticcloud.spigot.core.listeners.VoteListener;
import net.mysticcloud.spigot.core.runnables.DateChecker;
import net.mysticcloud.spigot.core.runnables.ParticleTimer;
import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.CustomTag;
import net.mysticcloud.spigot.core.utils.GUIManager;
import net.mysticcloud.spigot.core.utils.accounts.MysticAccountManager;
import net.mysticcloud.spigot.core.utils.accounts.friends.FriendUtils;
import net.mysticcloud.spigot.core.utils.chat.CoreChatUtils;
import net.mysticcloud.spigot.core.utils.placeholder.PlaceholderUtils;
import net.mysticcloud.spigot.core.utils.portals.PortalUtils;
import net.mysticcloud.spigot.core.utils.punishment.PunishmentUtils;
import net.mysticcloud.spigot.core.utils.skulls.SkullUtils;

public class Main extends JavaPlugin {
	static Main plugin;

	public void onEnable() {
		plugin = this;
		getServer().getMessenger().registerOutgoingPluginChannel(net.mysticcloud.spigot.core.Main.getPlugin(),
				"mystic:mystic");
		getServer().getMessenger().registerOutgoingPluginChannel(net.mysticcloud.spigot.core.Main.getPlugin(),
				"mystic:bungee");
		getServer().getMessenger().registerIncomingPluginChannel(this, "mystic:mystic", new MessageListener());
		CoreUtils.start();

		CoreChatUtils.start();

		SkullUtils.start();
		PunishmentUtils.registerPunishments();
		KitManager.registerKits();
		new PlayerListener(this);
		new ReportGUIListener(this);
		new ParticleGUIListener(this);
		new VoteListener(this);

		new KitCommand(this, "kit");
		new SQLCommand("sql", this);
		new AdminCommands(this, "blockparticles", "portal", "kick", "skull", "votetest", "seen", "uuid", "setspawn", "speed", "debug",
				"invsee", "level", "plugins", "back");
		new CoreCommands(this, "vote", "about", "pet", "rules", "settings", "spawn", "particles", "clear", "afk");
		new ItemCommand(this, "item");
		new GRLCommand(this, "grl");
		new RegisterCommand(this, "register");
		new PlayerListCommand(this, "playerlist");
		new SudoCommand(this, "sudo");
		new EconomyCommand(this, "balance", "pay");
		new ReportCommand(this, "report", "punish");
		new WarpCommand(this, "warp", "addwarp", "removewarp");
		new FriendCommand(this, "friends", "friend");
		new TeleportCommand(this, "tp", "tpa", "tpaccept", "tpdeny", "tphere", "tpoff");
		new BossCommand(this, "boss");
		new GamemodeCommand(this, "gamemode", "gmc", "gms", "gmsp", "gma");
		new UpdateCommand(this, "update");
		new TagCommand(this, "tags", "tag");
		startDateChecker();

		CustomTag.start();

		FriendUtils.start();

		GUIManager.init();
		
		PortalUtils.start();
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.setPlayerListName(
					CoreUtils.colorize(PlaceholderUtils.replace(player, CoreUtils.playerList("name"))));

			player.setPlayerListHeader(CoreUtils.colorize(CoreUtils.playerList("header")));

			player.setPlayerListFooter(CoreUtils.colorize(CoreUtils.playerList("footer")));
//			player.setPlayerListName(CoreUtils.colorize(CoreUtils.getPlayerPrefix(player) + player.getName()));
			if (CoreUtils.useCoreScoreboard())
				CoreUtils.setScoreboard(player);
		}

	}

	public void onDisable() {
		CoreUtils.end();
		for (Player player : Bukkit.getOnlinePlayers()) {
			MysticAccountManager.saveMysticPlayer(player);
		}
	}

	private static void startDateChecker() {
		Bukkit.getScheduler().runTaskLater(getPlugin(), new ParticleTimer(1), 1);
		Bukkit.getScheduler().runTaskLater(getPlugin(), new DateChecker(), 1);
	}

	public static Main getPlugin() {
		return plugin;
	}
}
