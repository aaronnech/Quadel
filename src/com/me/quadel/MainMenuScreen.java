package com.me.quadel;

import gameabstract.GDX2DRenderer;
import gameabstract.GDXGameInput;
import gameabstract.GameInput;
import gameabstract.GameState;
import gameabstract.MenuState;
import gameabstract.Renderer;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public class MainMenuScreen implements Screen {
	
	private Quadel gameHandle;
	private MenuState state;
	private GameInput input;
	private Renderer renderer;
	
	//Constructs a Menu screen from with a libgdx game object
	public MainMenuScreen(Quadel g) {
		gameHandle = g;
		renderer = new MenuRenderer();
		input = new GDXGameInput();
		((GDXGameInput) input).setTouchContext(((GDX2DRenderer) renderer ).viewport(), MenuRenderer.WIDTH, MenuRenderer.HEIGHT);
		state = new MainMenuState(input, this);
	}
	
	//called one per our specific frame rate. This is the essence of the game loop
	public void render(float delta) {
		state.update(delta);
		state.render(renderer);
	}
	
	public void toPlay(String level) {
		gameHandle.changeScreen("Game", level);
	}
	
	public void toLevelSelect() {
		
	}

	public void resize(int width, int height) {
		renderer.notify("resize", new int[] {width, height});
	}
	public void show() {
	}
	public void hide() {
	}
	public void pause() {
	}
	public void resume() {
	}
	public void dispose() {
	}

}
