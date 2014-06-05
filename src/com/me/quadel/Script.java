package com.me.quadel;
/*
 * Author: Aaron Nech
 * Project: SquirrelBots
 * Description: This class represents a Script that controls the squirrels.
 * We extend a UI manager to visualize/render the queue of commands that
 * make up the script for a specific color of squirrels.
 * 
 */
import gameabstract.GameInput;
import gameabstract.InputEvent;
import gameabstract.InputHandler;
import gameabstract.Renderable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


import simpleui.SimpleButton;
import simpleui.SimpleEvent;
import simpleui.SimpleEventType;
import simpleui.SimpleUIComponent;
import simpleui.SimpleUIListener;
import simpleui.SimpleUIManager;

public class Script extends SimpleUIManager implements SimpleUIListener {
	private Queue<OpCode> script;
	private Simulation simulation;
	
	//OpCode enumeration - OpCodes are specific instructors that are given meaning by their
	//followers
	public enum OpCode {
		FORWARD, TURN_CW, TURN_CCW, REVERSE, START
	}
	
	//A Script is linked to a simulator
	public Script(Simulation sim) {
		script = new LinkedList<OpCode>();
		simulation = sim;
		add(OpCode.START);
	}
	
	//Tics a script one forward
	public void tic() {
		OpCode code = script.remove();
		script.add(code);
		if(script.peek() == OpCode.START) {
			code = script.remove();
			script.add(code);
		}
	}
	
	//Peeks at the current command. If none is available,
	//default to OpCode.FORWARD
	public OpCode peek() {
		if(script.size() > 1) {
			return script.peek();
		} else {
			return OpCode.FORWARD;
		}
	}
	
	//removes a code from an index
	public void remove(int index) {
		reset();
		for(int i = 0; i < script.size(); i++) {
			OpCode code = script.remove();
			if(i != index || code == OpCode.START)
				script.add(code);
		}
	}
	
	//adds a code to the script
	public void add(OpCode code) {
		if(code != OpCode.START)
			addComponent(makeButton(code));
		script.add(code);
		
	}
	
	//resets the script to position itself at OpCode.START
	public void reset() {
		while(script.peek() != OpCode.START) {
			script.add(script.remove());
		}
	}
	
	//gets the count of the script (Number of OpCodes)
	public int count() {
		return script.size() - 1;
	}
	
	//Called when a queue button is clicked
	//removes that button
	public void removeClick(SimpleEvent e) {
		if(simulation.stopped()) {
			int index = ((Integer) e.getMessage()).intValue();
			e.getComponent().delete();
			remove(index);
		}
	}
	
	//hook that is activated after all UI components have reacted
	protected void postReact() {
		if(simulation.stopped()) {
			super.postReact();
			int count = 1;
			for(SimpleUIComponent component : this) {
				component.setMessage(count);
				component.setX(240 + count * 68);
				count++;
			}
		}
	}
	
	//makes a SimpleButton to attach to a OpCode
	private SimpleButton makeButton(OpCode type) {
		String name = "";
		switch(type) {
			case FORWARD:
				name = "forward";
				break;
			case TURN_CCW:
				name = "turn_ccw";
				break;
			case TURN_CW:
				name = "turn_cw";
				break;
			case REVERSE:
				name = "reverse";
				break;
			default:
				break;
		}
		SimpleButton button = new SimpleButton(
				name,
				240 + script.size() * 68,
				LogicGameRenderer.HEIGHT - 100,
				68,
				68);
		button.setMessage(script.size());
		button.subscribe(this, SimpleEventType.TOUCH, "removeClick");
		return button;
	}
}
