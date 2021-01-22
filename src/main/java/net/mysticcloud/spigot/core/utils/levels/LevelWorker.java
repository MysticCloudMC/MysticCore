package net.mysticcloud.spigot.core.utils.levels;

public class LevelWorker {
	private double threshhold;
	private double multiplier;

	LevelWorker() {
		this(50, 8);
	}

	LevelWorker(double threshhold, double multiplier) {
		this.threshhold = threshhold;
		this.multiplier = multiplier;
	}

	public double getLevel(double xp) {
		Double level = (1 + Math.sqrt(1 + multiplier * xp / threshhold)) / 2;
		return level;
	}

	public double getTotalForLevel(double level) {
		return (((((level * 2) - 1) * ((level * 2) - 1)) - 1) / multiplier) * threshhold;
	}

	public double untilNextLevel(double xp) {
		double level = getLevel(xp);
		double needed = getTotalForLevel(level + 1);
		return needed - xp;
	}

}
