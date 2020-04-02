package net.mysticcloud.spigot.core.utils.punishment;

import java.util.UUID;

public class Punishment {
	
	int duration;
	PunishmentType type;
	UUID user;
	long date;
	String notes = "";
	
	public Punishment(UUID user, PunishmentType type, int duration, long date) {
		this.user = user;
		this.type = type;
		this.duration = duration;
		this.date = date;
	}

	public UUID getUser() {
		return user;
	}
	public int getDuration() {
		return duration;
	}
	public PunishmentType getType() {
		return type;
	}
	public long getDate() {
		return date;
	}

	public String getNotes() {
		return notes;
	}
	
	
	
	
	public void setNotes(String notes){
		this.notes = notes;
	}

}
