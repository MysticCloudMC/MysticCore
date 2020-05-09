package net.mysticcloud.spigot.core.utils.levels;

public class LevelWorker {
	private long threshhold;
	private long multiplier;
	
	LevelWorker(){
		this(50,8);
	}
	LevelWorker(long threshhold, long multiplier){
		this.threshhold = threshhold;
		this.multiplier = multiplier;
	}
	
	public long getLevel(long xp){
		Double level = (1+Math.sqrt(1+multiplier*xp/threshhold))/2;
		return level.longValue();
	}
	
	public long getTotalForLevel(long level){
		return (((((level*2)-1)*((level*2)-1))-1)/multiplier)*threshhold;
	}
	
	public long untilNextLevel(long xp){
		long level = getLevel(xp);
		long needed = getTotalForLevel(level+1);
		return needed-xp;
	}

}
