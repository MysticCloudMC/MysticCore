package net.mysticcloud.spigot.core.utils.pets.v1_15_R1;

public class MovePet implements Runnable {

	
	Pet v;
	
	public MovePet(Pet v){
		this.v = v;
	}
	@Override
	public void run() {
		v.go();
	}

}
