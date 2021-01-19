	package net.mysticcloud.spigot.core;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.mysticcloud.spigot.core.commands.AdminCommands;
import net.mysticcloud.spigot.core.commands.BossCommand;
import net.mysticcloud.spigot.core.commands.ClearCommand;
import net.mysticcloud.spigot.core.commands.EconomyCommand;
import net.mysticcloud.spigot.core.commands.FriendCommand;
import net.mysticcloud.spigot.core.commands.GRLCommand;
import net.mysticcloud.spigot.core.commands.GamemodeCommand;
import net.mysticcloud.spigot.core.commands.ItemCommand;
import net.mysticcloud.spigot.core.commands.KitCommand;
import net.mysticcloud.spigot.core.commands.ParticlesCommand;
import net.mysticcloud.spigot.core.commands.PetCommand;
import net.mysticcloud.spigot.core.commands.PlayerListCommand;
import net.mysticcloud.spigot.core.commands.PunishCommand;
import net.mysticcloud.spigot.core.commands.RegisterCommand;
import net.mysticcloud.spigot.core.commands.SQLCommand;
import net.mysticcloud.spigot.core.commands.SeenCommand;
import net.mysticcloud.spigot.core.commands.SettingsCommand;
import net.mysticcloud.spigot.core.commands.SpawnCommand;
import net.mysticcloud.spigot.core.commands.SudoCommand;
import net.mysticcloud.spigot.core.commands.TagCommand;
import net.mysticcloud.spigot.core.commands.TeleportCommand;
import net.mysticcloud.spigot.core.commands.UUIDCommand;
import net.mysticcloud.spigot.core.commands.UpdateCommand;
import net.mysticcloud.spigot.core.commands.WarpCommand;
import net.mysticcloud.spigot.core.kits.KitManager;
import net.mysticcloud.spigot.core.listeners.ParticleGUIListener;
import net.mysticcloud.spigot.core.listeners.PlayerListener;
import net.mysticcloud.spigot.core.listeners.PunishmentGUIListener;
import net.mysticcloud.spigot.core.runnables.DateChecker;
import net.mysticcloud.spigot.core.runnables.ParticleTimer;
import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.GUIManager;
import net.mysticcloud.spigot.core.utils.placeholder.PlaceholderUtils;
import net.mysticcloud.spigot.core.utils.punishment.PunishmentUtils;

public class Main extends JavaPlugin {
	static Main plugin;

	public void onEnable() {
		plugin = this;
		CoreUtils.start();
		PunishmentUtils.registerPunishments();
		KitManager.registerKits();
		new PlayerListener(this);
		new PunishmentGUIListener(this);
		new ParticleGUIListener(this);
		new KitCommand(this, "kit");
		new SQLCommand("sql", this);
		new SettingsCommand("settings", this);
		new PetCommand(this, "pet");
		new AdminCommands(this, "debug","invsee");
		new ItemCommand(this, "item");
		new GRLCommand(this, "grl");
		new RegisterCommand(this, "register");
		new SpawnCommand(this, "spawn", "setspawn");
		new ParticlesCommand(this, "particles");
		new PlayerListCommand(this, "playerlist");
		new SudoCommand(this, "sudo");
		new EconomyCommand(this, "balance", "pay");
		new PunishCommand(this, "punish");
		new WarpCommand(this, "warp", "addwarp", "removewarp");
		new UUIDCommand(this, "uuid");
		new SeenCommand(this, "seen");
		new FriendCommand(this, "friends", "friend");
		new TeleportCommand(this,"tp","tpa","tpaccept","tpdeny","tphere","tpoff");
		new BossCommand(this,"boss");
		new GamemodeCommand(this,"gamemode","gmc","gms","gmsp","gma");
		new ClearCommand(this,"clear");
		new UpdateCommand(this,"update");
		new TagCommand(this,"tags","tag");
		startDateChecker();
		
		GUIManager.init();
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.setPlayerListName(
					CoreUtils.colorize(PlaceholderUtils.replace(player, CoreUtils.playerList("name"))));

			player.setPlayerListHeader(CoreUtils.colorize(CoreUtils.playerList("header")));
			
			player.setPlayerListFooter(CoreUtils.colorize(CoreUtils.playerList("footer")));
//			player.setPlayerListName(CoreUtils.colorize(CoreUtils.getPlayerPrefix(player) + player.getName()));
			if(CoreUtils.useCoreScoreboard())CoreUtils.enableScoreboard(player);
		}
		
		
		
	}

	public void onDisable() {
		CoreUtils.end();
		for(Player player : Bukkit.getOnlinePlayers()) {
			CoreUtils.saveMysticPlayer(player);
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
