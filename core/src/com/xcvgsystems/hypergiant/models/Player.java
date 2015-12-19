package com.xcvgsystems.hypergiant.models;

import java.util.List;

/**
 * A model that represents a player.
 * @author Chris
 *
 */

//TODO: rework this to support injuries and upgrades
//TODO: rework this to not be an RPG player
public class Player
{
	
	public enum Gender { UNSPECIFIED, FEMALE, MALE }
	
	public final int MAX_HP = 3; //maximum health points
	
	private int hp; //health points
	private int sp; //shield/armor/barrier points
	private int mp; //mana/energy points
	private Gender sex; //what am I?
	
	//private List<InventoryItem> inventory;

	public Player()
	{
		//set initial HP
		hp = MAX_HP;
	}

	public void changeHP(int i) {
		hp += i;
		
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int getHp() {
		return hp;
	}

	public int getSp() {
		return sp;
	}

	public int getMp() {
		return mp;
	}

	public Gender getSex() {
		return sex;
	}
	
	
	
}
