package com.me.quadel;

import gameabstract.GDX2DRenderer;
import gameabstract.GDXGameInput;
import gameabstract.GameInput;
import gameabstract.GameState;
import gameabstract.Renderer;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
/*
 * Author: Aaron Nech
 * Project: SquirrelBots
 * Description: This class acts as the central game platform
 * We build our renderer and logic from this platform
 */
public class GameScreen implements Screen {

	private Game gameHandle;
	private GameState state;
	private GameInput input;
	private Renderer renderer;
	private TextureAtlas atlas;
	
	//Constructs a game screen from with a libgdx game object
	public GameScreen(Game g, String level) {
		atlas = new TextureAtlas(Gdx.files.internal("data/logicgame.pack"));
		gameHandle = g;
		renderer = new LogicGameRenderer(atlas, 0, 0);
		input = new GDXGameInput();
		((GDXGameInput) input).setTouchContext(((GDX2DRenderer) renderer ).viewport(), LogicGameRenderer.WIDTH, LogicGameRenderer.HEIGHT);
		state = new LogicGameState(input, level);
	}
	
	//called one per our specific frame rate. This is the essence of the game loop
	public void render(float delta) {
		state.update(delta);
		state.render(renderer);
	}
	
	public void toMenu() {
		//gameHandle.;
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