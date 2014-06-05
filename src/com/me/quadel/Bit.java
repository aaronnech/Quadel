package com.me.quadel;
/*
 * Author: Aaron Nech
 * Project: SquirrelBots
 * Description: This class represents a Nut map entity in the game
 *
 */

public class Bit extends Entity {
	private Color nutColor;
	
	//A nut is constructed from a given color and coordinates
	public Bit(Color color, int x, int y) {
		super(x, y);
		nutColor = color;
	}
	
	//gets the color of this nut
	public Color getColor() {
		return nutColor;
	}
	
	//called when another entity pushes on the nut.
	//Nuts will be movable provided there isn't a second-tier moving
	public boolean pushBy(Entity e, Direction d) {
		if(e.getClass() == Bit.class)
			return false;
		return move(d);
	}
	
	//resets the nut state
	public void reset() {

	}
	
}
