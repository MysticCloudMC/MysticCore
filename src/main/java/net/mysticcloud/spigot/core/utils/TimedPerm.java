package main.java.net.mysticcloud.spigot.core.utils;

import java.util.Date;
import java.util.UUID;

public class TimedPerm {
	UUID uid;
	String permission;
	long started;
	long liveFor;
	String display;
	
	TimedPerm(UUID uid, String permission, long liveFor) {
		this.uid = uid;
		this.permission = permission;
		this.liveFor = liveFor;
		this.started = new Date().getTime();
	}
	
	public UUID uid() {
		return uid;
	}
	
	public String display() {
		return display;
	}
	
	public String permission() {
		return permission;
	}
	
	public long started() {
		return started;
	}
	public long liveFor() {
		return liveFor;
	}
	
	

	void started(long started) {
		this.started = started;
	}
	
	void display(String display) {
		this.display = CoreUtils.colorize(display);
	}

}
