package com.me.quadel;
/*
 * Author: Aaron Nech
 * Project: BotBots
 * Description: This class represents the gamestate of BotBots,
 * the logical and mathematical encapsulation of what it means to run a instance of this game
 */

import simpleui.SimpleButton;
import simpleui.SimpleEvent;
import simpleui.SimpleEventType;
import simpleui.SimpleRadio;
import simpleui.SimpleRadioGroup;
import simpleui.SimpleToggle;
import simpleui.SimpleUIListener;
import simpleui.SimpleUIManager;
import gameabstract.GameInput;
import gameabstract.GameState;
import gameabstract.InputEvent;
import gameabstract.Renderer;

public class LogicGameState extends GameState implements SimpleUIListener {
	private Simulation simulation;
	
	private SimpleUIManager ui;
	
	private Color selectedColor;
	
	//input interaction variables
	private boolean touchDown;
	private Bot lockedBot;
	private int tx, ty;
	
	private boolean levelComplete;
	
	//Instantiates a game state based on a setup game input
	public LogicGameState(GameInput i, String level) {
		super(i);
		simulation = new Simulation(.2f, MapData.fromFile(level), this);
		setupUI(i);
		selectedColor = Color.RED;
		i.subscribe(simulation.getScriptByColor(Color.RED));
		i.subscribe(simulation.getScriptByColor(Color.BLUE));
		i.subscribe(simulation.getScriptByColor(Color.GREEN));
	}
	
	//Sets up the game UI
	private void setupUI(GameInput i) {
		ui = new SimpleUIManager();
		ui.addComponent(
				new SimpleToggle(
					"toggle",
					LogicGameRenderer.WIDTH - 200,
					LogicGameRenderer.HEIGHT - 100,
					68,
					68
				),
				this, SimpleEventType.TOUCH, "buttonClick");
		ui.addComponent(
				new SimpleToggle(
					"stop",
					LogicGameRenderer.WIDTH - 120,
					LogicGameRenderer.HEIGHT - 100,
					68,
					68
				),
				this, SimpleEventType.TOUCH, "buttonClick");
		ui.addComponent(
				new SimpleToggle(
					"turn_cw_add",
					160,
					LogicGameRenderer.HEIGHT - 128,
					68,
					68
				),
				this, SimpleEventType.TOUCH, "buttonClick");
		ui.addComponent(
				new SimpleToggle(
					"turn_ccw_add",
					220,
					LogicGameRenderer.HEIGHT - 64,
					68,
					68
				),
				this, SimpleEventType.TOUCH, "buttonClick");
		ui.addComponent(
				new SimpleToggle(
					"reverse_add",
					160,
					LogicGameRenderer.HEIGHT - 64,
					68,
					68
				),
				this, SimpleEventType.TOUCH, "buttonClick");
		ui.addComponent(
				new SimpleToggle(
					"forward_add",
					220,
					LogicGameRenderer.HEIGHT - 128,
					68,
					68
				),
				this, SimpleEventType.TOUCH, "buttonClick");
		SimpleRadioGroup colorChoices = new SimpleRadioGroup();
		colorChoices.add(new SimpleRadio(
				"red",
				15,
				LogicGameRenderer.HEIGHT - 124,
				100,
				40),
				this, SimpleEventType.SELECT, "buttonClick"
			);
		colorChoices.add(new SimpleRadio(
				"green",
				15,
				LogicGameRenderer.HEIGHT - 82,
				100,
				40),
				this, SimpleEventType.SELECT, "buttonClick"
			);
		colorChoices.add(new SimpleRadio(
				"blue",
				15,
				LogicGameRenderer.HEIGHT - 40,
				100,
				40),
				this, SimpleEventType.SELECT, "buttonClick"
			);
		colorChoices.select("red");
		ui.addComponent(colorChoices);
		ui.getComponent("stop").hide();
		SimpleButton quit = new SimpleButton("quit", LogicGameRenderer.WIDTH / 2 - 200, LogicGameRenderer.HEIGHT - 100, 400, 50);
		quit.setText("To Menu");
		quit.hide();
		ui.addComponent(quit);
		i.subscribe(ui);
	}
	
	//Update is called once per logic loop, handles game update routines
	public void update(float delta) {
		processInput();
		if(!levelComplete)
			simulation.update(delta);
	}

	//Render is called once per render request, we are given a renderer
	//which will render all our game renderables
	public void render(Renderer r) {
		r.begin();
		if(!levelComplete) {
			r.render(simulation.getMap());
			for(Entity e : simulation.getMap().getEntities()) {
				r.render(e);
			}
			simulation.getScriptByColor(selectedColor).renderComponents(r);
		}
		ui.renderComponents(r);
		r.end();
	}
	
	//resets the gamestate
	public void reset() {
		
	}
	
	//input listener method, called when a event is fired
	public void input(InputEvent e) {
		if(simulation.stopped() && !levelComplete) {
			if(e.getType().equals("touchDown")) {
				touchDown = true;
				tx = ((int[]) e.getData())[0];
				ty = ((int[]) e.getData())[1];
				lockBot(toGrid(tx), toGrid(ty));
			}
			
			if(touchDown && lockedBot != null 
					&& e.getType().equals("touchDragged")) {
				lockedBot.setX(toGrid(((int[]) e.getData())[0]));
				lockedBot.setY(toGrid(((int[]) e.getData())[1]));
			}
			
			if(touchDown && e.getType().equals("touchUp")) {
				touchDown = false;
				unlockBot();
				if(toGrid(tx) == toGrid(((int[]) e.getData())[0])
						&& toGrid(ty) == toGrid(((int[]) e.getData())[1]))
					clickSpot(toGrid(tx), toGrid(ty));
			}
		}
	}
	
	//listens for UI events, fires when necessary
	public void buttonClick(SimpleEvent e) {
		if(e.getComponent().getName().equals("toggle")) {
			simulation.toggle();
			((SimpleToggle) e.getComponent()).toggle();
			if(!simulation.stopped())
				ui.getComponent("stop").show();
		}
		if(e.getComponent().getName().equals("stop")) {
			stopState();
		}
		if(e.getComponent().getName().equals("red")) {
			selectedColor = Color.RED;
		} else if(e.getComponent().getName().equals("green")) {
			selectedColor = Color.GREEN;
		} else if(e.getComponent().getName().equals("blue")) {
			selectedColor = Color.BLUE;
		}
		if(simulation.stopped()) {
			if(simulation.getScriptByColor(selectedColor).count() < 11) {
				if(e.getComponent().getName().equals("forward_add")) {
					simulation.addCode(Script.OpCode.FORWARD, selectedColor);
				} else if(e.getComponent().getName().equals("turn_ccw_add")) {
					simulation.addCode(Script.OpCode.TURN_CCW, selectedColor);
				} else if(e.getComponent().getName().equals("turn_cw_add")) {
					simulation.addCode(Script.OpCode.TURN_CW, selectedColor);
				} else if(e.getComponent().getName().equals("reverse_add")) {
					simulation.addCode(Script.OpCode.REVERSE, selectedColor);
				}
			}
		}
	}
	
	public void backToMenu() {
		//save game
		
		//exit
		
	}
	
	//stops the game state
	public void stopState() {
		simulation.stop();
		((SimpleToggle) ui.getComponent("toggle")).set(false);
		ui.getComponent("stop").hide();
	}
	
	//removes a specific code from our op code queue
	public void removeCode(SimpleEvent e) {
		int index = Integer.parseInt(e.getComponent().getName().split("@")[1]);
		simulation.removeCode(selectedColor, index);
	}
	
	//lock the squrriel into drag mode at the current location
	private void lockBot(int x, int y) {
		Entity e = simulation.getMap().entityAt(x, y);
		if(e != null && e.getClass() == Bot.class) {
			lockedBot = (Bot) e;
		}
	}
	
	//unlock the Bot currently locked if one is currrently selected(locked)
	private void unlockBot() {
		if(lockedBot != null) {
			if(!simulation.getMap().canMove(lockedBot.getX(), lockedBot.getY()) ||
					simulation.getMap().isWall(lockedBot.getX(), lockedBot.getY()) ||
					simulation.getMap().entityCount(lockedBot.getX(), lockedBot.getY()) > 1) {
				lockedBot.setX(toGrid(tx));
				lockedBot.setY(toGrid(ty));
			}
				
		}
		lockedBot = null;
	}
	
	//handles a tap event on a map spot (things like Bot rotation)
	private void clickSpot(int x, int y) {
		Entity e = simulation.getMap().entityAt(x, y);
		if(e != null && e.getClass() == Bot.class)
			((Bot) e).cw();
	}

	//Helps easily convert a integer pixel coordinate to a map coordinate
	private int toGrid(int x) {
		return x / GameMap.TILE_SIZE;
	}

	//called when a game is won.
	public void win(int score) {
		ui.clearComponents();
		SimpleButton quit = new SimpleButton("quit", LogicGameRenderer.WIDTH / 2 - 200, LogicGameRenderer.HEIGHT - 100, 400, 50);
		quit.setText("To Menu");
		ui.addComponent(quit);
		levelComplete = true;
	}
}
