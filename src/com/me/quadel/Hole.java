package com.me.quadel;
/*
 * Author: Aaron Nech
 * Project: SquirrelBots
 * Description: This class represents a hole entity on a map
 */

import java.util.Stack;

public class Hole extends Entity {
	private Stack<Bit> contents;
	private Color[] colorOrder;
	
	//A hole is constructed from a color sequence and location
	public Hole(Color[] sequence, int x, int y) {
		super(x, y);
		contents = new Stack<Bit>();
		colorOrder = sequence;
	}
	
	//Holes are static, but if a squirrel runs into one we kill it.
	public boolean pushBy(Entity e, Direction d) {
		if(e.getClass() == Bit.class) {
			pushNut((Bit) e);
		} else if(e.getClass() == Bot.class) {
			mapData.error("Squirrel died!");
		}
		return true;
	}
	
	//Pushes a nut onto our hole's "nut stack"
	public void pushNut(Bit n) {
		//bad nut input
		if(contents.size() >= colorOrder.length) {
			mapData.error("Hole is full!");
		} else if(colorOrder[contents.size()] != n.getColor()) {
			mapData.error("Wrong order!");
		} else {
			contents.push(n);
			mapData.removeEntity(n);
			System.out.println("Nut in hole!");
		}
	}
	
	//gets the hole's color sequence array by reference
	public Color[] getSequence() {
		return colorOrder;
	}
	
	//get the number of items inside the hole
	public int contentSize() {
		return contents.size();
	}
	
	//resets the hole entity
	public void reset() {
		contents.clear();
	}
}
