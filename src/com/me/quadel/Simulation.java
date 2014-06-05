package com.me.quadel;
/*
 * Author: Aaron Nech
 * Project: BotBots
 * Description: This class is the master of the actual game.
 * We create map instances on the fly to execute simulations, and use this class to control
 * map entities and tic rate. This class also controls the simulation scripts for each entity color group
 * It also sends back a score calculation upon winning the simulation
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import simpleui.SimpleEvent;

public class Simulation {
	private int simulationCount;
	private boolean playing;
	private boolean stopped;
	private float frameRate;
	private float current;
	private List<Bot> bots;
	private Script red;
	private Script green;
	private Script blue;
	private GameMap map;
	private GameMap runTimeMap;
	private LogicGameState state;
	
	//A simulation is constructed with a given tic rate, map data, and logic game state reference
	public Simulation(float rate, MapData data, LogicGameState s) {
		map = new GameMap(this);
		bots = new ArrayList<Bot>();
		red = new Script(this);
		green = new Script(this);
		blue = new Script(this);
		loadMap(data);
		frameRate = rate;
		stopped = true;
		playing = false;
		state = s;
		simulationCount = 1;
	}
	
	//Loads a map from a MapData object
	private void loadMap(MapData data) {
		map.load(data);
		for(Bot s : data.getBlue()) {
			s.setScript(blue);
		}
		for(Bot s : data.getRed()) {
			s.setScript(red);
		}
		for(Bot s : data.getGreen()) {
			s.setScript(green);
		}
		bots.clear();
		bots.addAll(Arrays.asList(data.getBlue()));
		bots.addAll(Arrays.asList(data.getRed()));
		bots.addAll(Arrays.asList(data.getGreen()));
	}
	
	//Adds an opcode to a specific color group
	public void addCode(Script.OpCode code, Color color) {
		getScriptByColor(color).add(code);
	}
	
	//removes an opcode from a color group at an index
	public void removeCode(Color color, int index) {
		getScriptByColor(color).remove(index);
	}
	
	//plays the simulation
	public void play() {
		playing = true;
		stopped = false;
		if(runTimeMap == null) {
			runTimeMap = new GameMap(this);
			try {
				runTimeMap.load(map);
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
	}
	
	//pauses the simulation
	public void pause() {
		playing = false;
	}
	
	//stops the simulation
	public void stop() {
		simulationCount = 1;
		playing = false;
		stopped = true;
		runTimeMap = null;
		red.reset();
		green.reset();
		blue.reset();
		map.resetEntities();
	}
	
	//updates the simulation
	public void update(float delta) {
		if(playing) {
			current += delta;
			if(current > frameRate) {
				tic();
				current = 0;
			}
		}
	}
	
	//moves the simulation forward a logical step
	public void tic() {
		red.tic();
		blue.tic();
		green.tic();
		runTimeMap.tic();
		if(playing)
			checkForRemoves();
		simulationCount++;
	}
	
	private void checkForRemoves() {
		boolean check = false;
		Iterator<Entity> i = runTimeMap.getEntities().iterator();
		while(i.hasNext()) {
			Entity e = i.next();
			if(e.markedDelete()) {
				i.remove();
				check = true;
			}
		}
		if(check)
			runTimeMap.checkSolved();
	}
	
	//toggles play and pausing the simulation
	public void toggle() {
		if(playing) {
			pause();
		} else {
			play();
		}
	}
	
	//returns true if the simulation is currently running
	public boolean isRunning() {
		return !stopped && playing;
	}
	
	//gets the current map instance being shown
	public GameMap getMap() {
		if(!stopped)
			return runTimeMap;
		return map;
	}
	
	//returns true if the simulation is stopped
	public boolean stopped() {
		return stopped;
	}
	
	//gets a script based on the scripts color group
	public Script getScriptByColor(Color color) {
		switch(color) {
			case RED:
				return red;
			case GREEN:
				return green;
			case BLUE:
				return blue;
		}
		return null;
	}
	
	//called when the simulation is notified of an error
	public void error(String string) {
		System.out.println(string);
		state.stopState();
	}
	
	//called when the simulation is notified of a winning state
	public void solved() {
		//the game score is:
		//23572 * 1/tics * 1/avg script count
		int meanScriptCount = (red.count() + green.count() + blue.count()) / 3 + 1;
		int score = (int) (23572f * (1f / simulationCount) * (1f / meanScriptCount)); 
		state.stopState();
		state.win(score);
	}
}
