package net.mysticcloud.spigot.core.utils.entities;

public enum MysticEntityType {
	
	TEST_CHICKEN("TestChicken", 93, TestChicken.class);
	
	
	String name;
	int id;
	Class<?> customClass;
	MysticEntityType(String name, int id, Class<?> customClass){
		this.name = name;
		this.id = id;
		this.customClass = customClass;
	}
	
	public String getName(){
		return name;
	}
	
	public int getId(){
		return id;
	}
	
	public Class<?> getCustomClass(){
		return customClass;
	}

}
