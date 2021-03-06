package net.mysticcloud.spigot.core.utils.admin;

import java.util.List;
import java.util.UUID;

import org.bukkit.OfflinePlayer;

import net.milkbowl.vault.economy.AbstractEconomy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.accounts.MysticAccountManager;

public class VaultAPI extends AbstractEconomy {
	
	@Override
	public boolean hasAccount(OfflinePlayer player) {
		return hasAccount(player.getUniqueId().toString());
	}

	public boolean hasAccount(String uid) {
		return MysticAccountManager.getMysticPlayer(UUID.fromString(uid)) != null;

	}
	
	@Override
	public double getBalance(OfflinePlayer player) {
		return getBalance(player.getUniqueId().toString());
	}

	public double getBalance(String uid) {
		return MysticAccountManager.getMysticPlayer(UUID.fromString(uid)).getBalance();
	}
	
	@Override
	public boolean has(OfflinePlayer player, double amount) {
		return has(player.getUniqueId().toString(), amount);
	}

	public boolean has(String name, double amount) {
		if (getBalance(name) < CoreUtils.getMoneyFormat(amount)) {
			return false;
		}
		return true;
	}
	
	@Override
	public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
		return withdrawPlayer(player.getUniqueId().toString(), amount);
	}

	public EconomyResponse withdrawPlayer(String uid, double amount) {

		if (!hasAccount(uid)) {
			return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE,
					"The player does not have an account!");
		}
		double balance = getBalance(uid);

		if (getBalance(uid) < CoreUtils.getMoneyFormat(amount)) {
			return new EconomyResponse(0.0D, balance, EconomyResponse.ResponseType.FAILURE,
					"The value is more than the player's balance!");
		}
		balance = CoreUtils.getMoneyFormat(balance-amount);

		MysticAccountManager.getMysticPlayer(UUID.fromString(uid)).setBalance(balance);

		return new EconomyResponse(CoreUtils.getMoneyFormat(amount), CoreUtils.getMoneyFormat(balance),
				EconomyResponse.ResponseType.SUCCESS, "");
	}
	
	@Override
	public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
		return depositPlayer(player.getUniqueId().toString(), amount);
	}

	public EconomyResponse depositPlayer(String uid, double amount) {
		if (!hasAccount(uid)) {
			return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE,
					"The player does not have an account!");
		}
		if (CoreUtils.getMoneyFormat(amount) < 0.0D) {
			return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "Value is less than zero!");
		}

		MysticAccountManager.getMysticPlayer(UUID.fromString(uid)).setBalance(CoreUtils.getMoneyFormat(getBalance(uid) + amount),true);

		return new EconomyResponse(CoreUtils.getMoneyFormat(amount), 0.0D, EconomyResponse.ResponseType.SUCCESS, "");
	}
	
	@Override
	public boolean createPlayerAccount(OfflinePlayer player) {
		return createPlayerAccount(player.getUniqueId().toString());
	}

	public boolean createPlayerAccount(String uid) {
		return MysticAccountManager.getMysticPlayer(UUID.fromString(uid)) != null;
	}

	public String format(double summ) {
		return String.valueOf(summ);
	}
	
	@Override
	public boolean hasAccount(OfflinePlayer player, String worldName) {
		return hasAccount(player.getUniqueId().toString(), worldName);
	}

	public boolean hasAccount(String uid, String world) {
		return hasAccount(uid);
	}
	
	@Override
	public boolean has(OfflinePlayer player, String worldName, double amount) {
		return has(player.getUniqueId().toString(), worldName, amount);
	}

	public boolean has(String uid, String world, double amount) {
		return has(uid, amount);
	}
	
	@Override
	public double getBalance(OfflinePlayer player, String world) {
		return getBalance(player.getUniqueId().toString(), world);
	}

	public double getBalance(String uid, String world) {
		return CoreUtils.getMoneyFormat(getBalance(uid));
	}
	
	@Override
	public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
		return withdrawPlayer(player.getUniqueId().toString(), worldName, amount);
	}

	public EconomyResponse withdrawPlayer(String uid, String world, double amount) {
		return withdrawPlayer(uid, amount);
	}
	
	@Override
	public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
		return depositPlayer(player.getUniqueId().toString(), worldName, amount);
	}

	public EconomyResponse depositPlayer(String name, String world, double amount) {
		return depositPlayer(name, amount);
	}

	@Override
	public EconomyResponse createBank(String name, OfflinePlayer player) {
		return null;
	}

	public EconomyResponse createBank(String s, String s1) {
		return null;
	}

	public EconomyResponse deleteBank(String s) {
		return null;
	}

	public EconomyResponse bankBalance(String s) {
		return null;
	}

	public EconomyResponse bankHas(String s, double v) {
		return null;
	}

	public EconomyResponse bankWithdraw(String s, double v) {
		return null;
	}

	public EconomyResponse bankDeposit(String s, double v) {
		return null;
	}

	public EconomyResponse isBankOwner(String s, String s1) {
		return null;
	}

	public EconomyResponse isBankMember(String s, String s1) {
		return null;
	}
	
	@Override
	public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
		return createPlayerAccount(player.getUniqueId().toString(), worldName);
	}

	public boolean createPlayerAccount(String name, String world) {
		return createPlayerAccount(name);
	}

	public List<String> getBanks() {
		return null;
	}

	public boolean hasBankSupport() {
		return false;
	}

	public boolean isEnabled() {
		return (Main.getPlugin() != null);
	}

	public String getName() {
		return "MysticEconomy";
	}

	public int fractionalDigits() {
		return -1;
	}

	public String currencyNamePlural() {
		return "Unubtaniums";
	}

	public String currencyNameSingular() {
		return "Unubtanium";
	}
}
