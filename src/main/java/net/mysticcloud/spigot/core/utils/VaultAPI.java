package net.mysticcloud.spigot.core.utils;

import java.io.IOException;
import java.util.List;

import net.milkbowl.vault.economy.AbstractEconomy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.mysticcloud.spigot.core.Main;

public class VaultAPI extends AbstractEconomy {

	public boolean hasAccount(String uid) {

		if (CoreUtils.getEconomyConfig().getStringList("EconomyList").contains(uid))
			return true;
		return false;
	}

	public double getBalance(String uid) {

		if (!hasAccount(uid)) {
			createPlayerAccount(uid);
		}

		return CoreUtils.getMoneyFormat(CoreUtils.getEconomyConfig().getDouble(uid));
	}

	public boolean has(String name, double amount) {
		if (!hasAccount(name)) {
			createPlayerAccount(name);
		}
		if (getBalance(name) < CoreUtils.getMoneyFormat(amount)) {
			return false;
		}
		return true;
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
		balance -= CoreUtils.getMoneyFormat(amount);

		CoreUtils.getEconomyConfig().set(uid, Double.valueOf(CoreUtils.getMoneyFormat(balance)));
		try {
			CoreUtils.getEconomyConfig().save(CoreUtils.getPlayerDatafile());
		} catch (IOException p) {
			p.printStackTrace();
		}

		return new EconomyResponse(CoreUtils.getMoneyFormat(amount), CoreUtils.getMoneyFormat(balance),
				EconomyResponse.ResponseType.SUCCESS, "");
	}

	public EconomyResponse depositPlayer(String uid, double amount) {
		if (!hasAccount(uid)) {
			return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE,
					"The player does not have an account!");
		}
		if (CoreUtils.getMoneyFormat(amount) < 0.0D) {
			return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "Value is less than zero!");
		}

		CoreUtils.getEconomyConfig().set(uid,
				Double.valueOf(CoreUtils.getMoneyFormat(CoreUtils.getEconomyConfig().getDouble(uid))
						+ CoreUtils.getMoneyFormat(amount)));
		try {
			CoreUtils.getEconomyConfig().save(CoreUtils.getPlayerDatafile());
		} catch (IOException p) {
			p.printStackTrace();
		}

		return new EconomyResponse(CoreUtils.getMoneyFormat(amount), 0.0D, EconomyResponse.ResponseType.SUCCESS, "");
	}

	public boolean createPlayerAccount(String uid) {
		if (!hasAccount(uid)) {

			CoreUtils.ecoaccounts.add(uid);
			CoreUtils.getEconomyConfig().set("EconomyList", CoreUtils.ecoaccounts);
			CoreUtils.getEconomyConfig().set(uid, Double.valueOf(CoreUtils.getMoneyFormat(CoreUtils.startingBalance)));
			try {
				CoreUtils.getEconomyConfig().save(CoreUtils.getPlayerDatafile());
			} catch (IOException p) {
				p.printStackTrace();
			}

			return true;
		}

		return false;
	}

	public String format(double summ) {
		return String.valueOf(summ);
	}

	public boolean hasAccount(String uid, String world) {
		return hasAccount(uid);
	}

	public boolean has(String uid, String world, double amount) {
		return has(uid, amount);
	}

	public double getBalance(String uid, String world) {
		return getBalance(uid);
	}

	public EconomyResponse withdrawPlayer(String uid, String world, double amount) {
		return withdrawPlayer(uid, amount);
	}

	public EconomyResponse depositPlayer(String name, String world, double amount) {
		return depositPlayer(name, amount);
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
		return "AConomy";
	}

	public int fractionalDigits() {
		return -1;
	}

	public String currencyNamePlural() {
		return "";
	}

	public String currencyNameSingular() {
		return "";
	}
}
