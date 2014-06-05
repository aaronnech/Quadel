package com.me.quadel;
/*
 * Author: Aaron Nech
 * Project: SquirrelBots
 * Description: This class represents an active map object Entity,
 * The class is both renderable and cloneable, and is meant to extended
 */
import gameabstract.Renderable;

public abstract class Entity implements Renderable, Cloneable {
	protected int x, y;
	protected GameMap mapData;
	private boolean markDelete;
	
	//Direction structure -- Enumeration of Up/Down/Left/Right
	//provides some basic constructs to manipulate directions
	public enum Direction {
		LEFT, RIGHT, UP, DOWN;
		public static Direction cw(Direction d) {
			switch(d) {
				case LEFT:
					return UP;
				case UP:
					return RIGHT;
				case RIGHT:
					return DOWN;
				case DOWN:
					return LEFT;
			}
			return UP;
		}
		public static Direction ccw(Direction d) {
			switch(d) {
				case UP:
					return LEFT;
				case RIGHT:
					return UP;
				case DOWN:
					return RIGHT;
				case LEFT:
					return DOWN;
			}
			return UP;
		}
		public static int toDegrees(Direction d) {
			switch(d) {
				case UP:
					return 270;
				case DOWN:
					return 90;
				case LEFT:
					return 180;
				case RIGHT:
					return 0;
			}
			return 0;
		}
	}
	
	//instantiates a entity at a arbitrary location
	public Entity(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	//Getters/Setters
	public final int getX() {
		return x;
	}
	public final int getY() {
		return y;
	}
	public final void setX(int x) {
		this.x = x;
	}
	public final void setY(int y) {
		this.y = y;
	}
	public final void setMap(GameMap map) {
		mapData = map;
	}
	
	//Function to be implemented by extenders. Specifies which logic to
	//follow based on a push. Returning true allows entities to move through others, while returning
	//false halts the movement
	public abstract boolean pushBy(Entity e, Direction d);
	
	//moves an entity in a given direction
	public final boolean move(Direction d) {
		switch(d) {
			case LEFT:
				if( mapData.canMove(x - 1, y) && !mapData.isWall(x - 1, y)) {
					Entity e = mapData.entityAt(x - 1, y);
					if(e == null || e.pushBy(this, d)) {
						x--;
						return true;
					}
				}
				break;
			case RIGHT:
				if(mapData.canMove(x + 1, y) && !mapData.isWall(x + 1, y)) {
					Entity e = mapData.entityAt(x + 1, y);
					if(e == null || e.pushBy(this, d)) {
						x++;
						return true;
					}
				}
				break;
			case UP:
				if(mapData.canMove(x, y - 1) && !mapData.isWall(x, y - 1)) {
					Entity e = mapData.entityAt(x, y - 1);
					if(e == null || e.pushBy(this, d)) {
						y--;
						return true;
					}
				}
				break;
			case DOWN:
				if(mapData.canMove(x, y + 1) && !mapData.isWall(x, y + 1)) {
					Entity e = mapData.entityAt(x, y + 1);
					if(e == null || e.pushBy(this, d)) {
						y++;
						return true;
					}
				}
		}
		return false;
	}
	
	//resets an entity (logic to be determined by children class)
	public abstract void reset();
	
	//Deletes an entity (flags the simulator to remove)
	public final void delete() {
		markDelete = true;
	}
	
	//Clones an entity and returns the value
	public final Entity clone() throws CloneNotSupportedException {
		return (Entity) super.clone();
	}

	//returns true if this entity is marked to be deleted, false otherwise
	public final boolean markedDelete() {
		return markDelete;
	}
}
