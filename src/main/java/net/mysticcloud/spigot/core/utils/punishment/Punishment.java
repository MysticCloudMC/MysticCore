package net.mysticcloud.spigot.core.utils.punishment;

import java.util.UUID;

public class Punishment {
	
	long duration;
	PunishmentType type;
	UUID user;
	long date;
	String notes = "";
	
	public Punishment(UUID user, PunishmentType type, long duration, long date) {
		this.user = user;
		this.type = type;
		this.duration = duration;
		this.date = date;
	}

	public UUID getUser() {
		return user;
	}
	public long getDuration() {
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
	
	public InfringementSeverity getSeverity(){
		if(notes.contains("[SEVERITY ")){
			String sev = notes.replaceAll("[","|");
			sev = sev.replaceAll("]","|");
			sev = sev.split("EVERITY ")[1].split("|")[0];
			return InfringementSeverity.valueOf(sev);
		} 
		return InfringementSeverity.LOW;
	}
	
	
	
	
	public void setNotes(String notes){
		this.notes = notes;
	}
	

}
