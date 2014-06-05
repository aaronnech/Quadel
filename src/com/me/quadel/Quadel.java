package com.me.quadel;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;

public class Quadel extends Game{
	public static final String SCORES_FILE = "data/scores.json";
	private GameScreen game;
	private MainMenuScreen mainMenu;
	
	//creates the game, and sets up the screens
	public void create() {		
		mainMenu = new MainMenuScreen(this);
		setScreen(mainMenu);
	}
	
	//public access to change the screen when needed by game events such as
	//menu clicks
	public void changeScreen(String name, String extra) {
		if(name.equals("Game")) {
			game = new GameScreen(this, extra);
			mainMenu = null;
			setScreen(game);
		}
	}
	//called when finished
	public void dispose() {

	}


}