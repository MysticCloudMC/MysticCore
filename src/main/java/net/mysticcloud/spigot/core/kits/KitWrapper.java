package net.mysticcloud.spigot.core.kits;

public class KitWrapper {
	
	private long started;
	private String kit;
	
	public KitWrapper(String kit, long started){
		this.started = started;
		this.kit = kit;
	}
	
	public long started() {
		return started;
	}
	
	public String kit() {
		return kit;
	}
	
}
