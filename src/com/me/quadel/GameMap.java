package com.me.quadel;
/*
 * Author: Aaron Nech
 * Project: SquirrelBots
 * Description: This class represents an active map object
 *	each map object is the renderable puppet of a simulation object, and acts as a glue/platform for all
 *  simulation interactions
 */

import gameabstract.Renderable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class GameMap implements Renderable {
	//tile data constants
	private static final int WALL = 1;
	private static final int BLANK = 0;
	
	//constants of map dimensions
	public static final int WIDTH = 20;
	public static final int HEIGHT = 10;
	public static final int TILE_SIZE = 64;
	
	private Integer[][] data;
	private ArrayList<Entity> eList;
	private Simulation simulation;
	
	//constructs a new gamemap from a simulation
	public GameMap(Simulation sim) {
		eList = new ArrayList<Entity>();
		simulation = sim;
	}
	
	//loads in map data from a MapData object
	public void load(MapData map) {
		eList.clear();
		eList.addAll(Arrays.asList(map.getRed()));
		eList.addAll(Arrays.asList(map.getGreen()));
		eList.addAll(Arrays.asList(map.getBlue()));
		eList.addAll(Arrays.asList(map.getHoles()));
		eList.addAll(Arrays.asList(map.getNuts()));
		data = map.getData().clone();
		setMapReferences();
	}
	
	//Clones another game map
	public void load(GameMap map) throws CloneNotSupportedException {
		eList.clear();
		eList.addAll(map.cloneEntities());
		data = map.getData().clone();
		setMapReferences();
	}
	
	//find out if a wall is located at a specific map location
	public boolean isWall(int x, int y) {
		return data[y][x] == 1;
	}
	
	//find out if a entity is located at a specific map location
	public Entity entityAt(int x, int y) {
		for(Entity e : eList) {
			if(e.getX() == x && e.getY() == y)
				return e;
		}
		return null;
	}
	
	//decides if it's possible to move to a location based on the map bounds
	public boolean canMove(int x, int y) {
		return !(x >= WIDTH || y >= HEIGHT || x < 0 || y < 0);
	}
	
	//get the list of entities on the map
	public List<Entity> getEntities() {
		return eList;
	}
	
	//get the integer array of tile data
	public Integer[][] getData() {
		return data;
	}
	
	//clone all entities on this map
	public List<Entity> cloneEntities() throws CloneNotSupportedException {
		ArrayList<Entity> list = new ArrayList<Entity>();
		for(Entity e : eList)
			list.add((Entity) e.clone());
		return list;
	}
	
	//add an entity to the map
	public void addEntity(Entity e) {
		eList.add(e);
	}
	
	//remove the entity from the map
	public void removeEntity(Entity e) {
		e.delete();
	}
	
	//count entities at location
	public int entityCount(int x, int y) {
		int count = 0;
		for(Entity e : eList) {
			if(e.getX() == x && e.getY() == y)
				count++;
		}
		return count;
	}
	
	//tic all entities on the map forward one iteration
	public void tic() {
		for (Entity e : eList) {
			if(e.getClass() == Bot.class)
				((Bot) e).tic();
		}
	}
	
	//ensures that all entities know which map they belong to
	private void setMapReferences() {
		for(Entity e : eList) {
			e.setMap(this);
		}
	}

	//Lets the simulation know there has been a problem
	public void error(String string) {
		resetEntities();
		simulation.error(string);
	}

	//Check if the map is solved (all nuts are in holes)
	public void checkSolved() {
		boolean solved = true;
		for(Entity e : eList) {
			if(e.getClass() == Bit.class) {
				solved = false;
				break;
			}
		}
		if(solved) {
			resetEntities();
			simulation.solved();
		}
	}
	
	//resets all the map entities
	public void resetEntities() {
		for(Entity e : eList) {
			e.reset();
		}
	}
}
