package com.xcvgsystems.hypergiant.thinkers;

import java.util.EnumSet;

public abstract class Thinker {
	
	protected int id;	
	
	protected int x, y;
	protected int layer;
	protected float velocityX, velocityY; //velocity is now fraction of a tile to move per tick
	protected int facing; //I hope this is faster than enums because it's a lot less obvious
	protected float angle;
	protected int hp;
	
	protected boolean enabled = true;
	protected boolean visible = true;
	protected boolean solid = true;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getLayer() {
		return layer;
	}
	public void setLayer(int layer) {
		this.layer = layer;
	}
	public float getVelocityX() {
		return velocityX;
	}
	public void setVelocityX(float velocityX) {
		this.velocityX = velocityX;
	}
	public float getVelocityY() {
		return velocityY;
	}
	public void setVelocityY(float velocityY) {
		this.velocityY = velocityY;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	public boolean isSolid() {
		return solid;
	}
	public void setSolid(boolean solid) {
		this.solid = solid;
	}
	public int getFacing() {
		return facing;
	}
	public void setFacing(int facing) {
		this.facing = facing;
	}
	public float getAngle() {
		return angle;
	}
	public void setAngle(float angle) {
		this.angle = angle;
	}
	public int getHp() {
		return hp;
	}
	public void setHp(int hp) {
		this.hp = hp;
	}
	
	
	
}
