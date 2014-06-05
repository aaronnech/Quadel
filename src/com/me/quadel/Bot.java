package com.me.quadel;

/*
 * Author: Aaron Nech
 * Project: SquirrelBots
 * Description: This class represents the Squrriel map entity of the game
 * 
 */

public class Bot extends Entity {
	private Direction facing;
	private Script script;
	private Color team;
	
	//A Squirrel is constructed from a Color group and coordinate
	public Bot(Color color, int x, int y) {
		super(x, y);
		facing = Direction.RIGHT;
		team = color;
	}
	
	//gets the current direction a squirrel is facing
	public Direction getDirection() {
		return facing;
	}
	
	//called when a squirrel is pushed on by and entity. Squirrels cannot be pushed
	public boolean pushBy(Entity e, Direction d) {
		return false;
	}
	
	//tics a squirrel forward a logical step
	public void tic() {
		execute(script.peek());
	}
	
	//rotates a squirrel clockwise
	public void cw() {
		turn(Script.OpCode.TURN_CW);
	}
	
	//gets the current script of this squirrel
	public Script getScript() {
		return script;
	}
	
	//sets the current script of this squirrel
	public void setScript(Script s) {
		script = s;
	}
	
	//gets the current color group
	public Color getTeam() {
		return team;
	}
	
	//executes an opcode from a script
	private void execute(Script.OpCode code) {
		if(code == Script.OpCode.FORWARD) {
			move(facing);
		} else {
			turn(code);
		}
	}
	
	//turns a squirrel either 180, clockwise or counterclockwise
	private void turn(Script.OpCode direction) {
		if(direction == Script.OpCode.TURN_CW) {
			facing = Direction.cw(facing);
		} else if(direction == Script.OpCode.TURN_CCW) {
			facing = Direction.ccw(facing);
		} else {
			facing = Direction.cw(Direction.cw(facing));
		}
	}
	
	//resets a squirrel object
	public void reset() {

	}
}
