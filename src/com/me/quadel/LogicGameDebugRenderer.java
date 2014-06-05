package com.me.quadel;
/*
 * Author: Aaron Nech
 * Project: BotBots
 * Description: This class extends the SJGF's LibGDX 2D renderer 
 * It handles all rendering routines for objects that implement the renderable interface
 */

import simpleui.SimpleButton;
import simpleui.SimpleRadio;
import simpleui.SimpleRadioGroup;
import simpleui.SimpleToggle;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import gameabstract.GDX2DRenderer;
import gameabstract.Renderable;

public class LogicGameDebugRenderer extends GDX2DRenderer {
	public static final int WIDTH = 896;
	public static final int HEIGHT = 544;
	
	public LogicGameDebugRenderer() {
		super(WIDTH, HEIGHT);
	}
	
	//main decision logic to render the correct form factor for each object
	public void render(Renderable e) {
		shape().setProjectionMatrix(camera().combined);
		Class<? extends Renderable> type = e.getClass();
		if(type == Bot.class) {
			renderBot((Bot) e);
		} else if(type == Bit.class) {
			renderNut((Bit) e);
		} else if(type == Hole.class) {
			renderHole((Hole) e);
		} else if(type == GameMap.class) {
			renderMap((GameMap) e);
		} else if(type == SimpleButton.class ||
				type == SimpleToggle.class || 
				type == SimpleRadio.class) {
			renderButton((SimpleButton) e);
		} else if(type == SimpleRadioGroup.class) {
			renderRadios((SimpleRadioGroup) e);
		}
	}

	//before render hook
	public void begin() {
		cls();
		camera().update();
	}

	//after render hook
	public void end() {
		
	}
	
	//renders the game map object
	private void renderMap(GameMap m) {
		for(int x = 0; x < GameMap.WIDTH; x++) {
			for(int y = 0; y < GameMap.HEIGHT; y++) {
				if(m.isWall(x, y)) {
					shape().setColor(0.5f, 0.5f, 0.5f, 1.0f);
				} else {
					shape().setColor(0.2f, 0.2f, 0.2f, 1.0f);
				}
				shape().begin(ShapeType.FilledRectangle);
				shape().filledRect(x * GameMap.TILE_SIZE, y * GameMap.TILE_SIZE,
						GameMap.TILE_SIZE, GameMap.TILE_SIZE);
				shape().setColor(0f, 0f, 0f, 1.0f);
				shape().end();
				shape().begin(ShapeType.Rectangle);
				shape().rect(x * GameMap.TILE_SIZE, y * GameMap.TILE_SIZE,
						GameMap.TILE_SIZE, GameMap.TILE_SIZE);
				shape().end();
			}
		}
	}
	
	//renders a Bot
	private void renderBot(Bot s) {
		shape().begin(ShapeType.FilledCircle);
		switch(s.getTeam()) {
			case RED:
				shape().setColor(1.0f, 0f, 0f, 1.0f);
				break;
			case GREEN:
				shape().setColor(0f, 1.0f, 0f, 1.0f);
				break;
			case BLUE:
				shape().setColor(0f, 0f, 1.0f, 1.0f);
				break;
		}

		shape().translate(s.getX() * GameMap.TILE_SIZE + GameMap.TILE_SIZE / 2,
				s.getY() * GameMap.TILE_SIZE + GameMap.TILE_SIZE / 2, 0f);
		shape().rotate(0f, 0f, 1.0f, Entity.Direction.toDegrees(s.getDirection()));
		shape().translate(-s.getX() * GameMap.TILE_SIZE - GameMap.TILE_SIZE / 2,
				-s.getY() * GameMap.TILE_SIZE - GameMap.TILE_SIZE / 2, 0f);
		shape().filledCircle(s.getX() * GameMap.TILE_SIZE + GameMap.TILE_SIZE / 2,
				s.getY() * GameMap.TILE_SIZE + GameMap.TILE_SIZE / 2, 3);
		shape().filledCircle(s.getX() * GameMap.TILE_SIZE + GameMap.TILE_SIZE / 2 - 3,
				s.getY() * GameMap.TILE_SIZE + GameMap.TILE_SIZE / 2, 4);
		shape().setColor(.7f, .7f, .7f, 1.0f);
		shape().filledCircle(s.getX() * GameMap.TILE_SIZE + GameMap.TILE_SIZE / 2 + 3,
				s.getY() * GameMap.TILE_SIZE + GameMap.TILE_SIZE / 2, 3);
		shape().identity();
		shape().end();
	}
	
	//renders a nut
	private void renderNut(Bit n) {
		shape().begin(ShapeType.FilledCircle);
		shape().setColor(1.0f, 1.0f, 1.0f, 1.0f);
		shape().filledCircle(n.getX() * GameMap.TILE_SIZE + GameMap.TILE_SIZE / 2,
				n.getY() * GameMap.TILE_SIZE + GameMap.TILE_SIZE / 2, 2);
		shape().end();
	}
	
	//renders a hole
	private void renderHole(Hole h) {
		shape().begin(ShapeType.FilledCircle);
		shape().setColor(0f, 0f, 0f, 1.0f);
		shape().filledCircle(h.getX() * GameMap.TILE_SIZE + GameMap.TILE_SIZE / 2,
				h.getY() * GameMap.TILE_SIZE + GameMap.TILE_SIZE / 2, 5);
		shape().end();
	}
	
	//renders an arbitrary button
	private void renderButton(SimpleButton e) {
		if(e.getClass() == SimpleToggle.class ||
				e.getClass() == SimpleButton.class) {
			shape().begin(ShapeType.Rectangle);
			shape().setColor(1.0f, 1.0f, 1.0f, 1.0f);
		} else if( e.getClass() == SimpleRadio.class) {
			if(((SimpleToggle) e).state()){
				shape().begin(ShapeType.FilledRectangle);
			} else {
				shape().begin(ShapeType.Rectangle);
			}
		}
		
		if(e.getName().equals("toggle")) { 
			renderPlayButton((SimpleToggle) e);
		} else if("redgreenblue".indexOf(e.getName()) != -1) {
			renderProgramColorButton((SimpleRadio) e, e.getName());
		} else {
			shape().rect(e.getX(), e.getY(), e.getWidth(), e.getHeight());
			if(e.getName().equals("stop")) {
				renderStopButton(e);
			} else if(e.getName().startsWith("forward")) {
				renderForwardButton(e);
			} else if(e.getName().startsWith("reverse")) {
				renderSwapButton(e);
			} else if(e.getName().startsWith("turn_ccw")) {
				renderLeftButton(e);
			} else if(e.getName().startsWith("turn_cw")) {
				renderRightButton(e);
			}
		}
		shape().end();
	}
	
	//Below are the specific button rendering routines
	//Executed for each renderable button
	private void renderLeftButton(SimpleButton e) {
		shape().end();
		shape().begin(ShapeType.FilledRectangle);
		shape().filledRect(e.getX() + e.getWidth() / 4,
							e.getY() + e.getHeight() / 4,
							2,
							e.getHeight() / 2 - 3);
		shape().filledRect(e.getX() + e.getWidth() / 4 * 3 - 2,
				e.getY() + e.getHeight() / 4,
				2,
				e.getHeight() / 2 - 3);
		shape().filledRect(e.getX() + e.getWidth() / 4,
							e.getY() + e.getHeight() / 4,
							e.getWidth() / 2,
							2);
		shape().end();
		shape().begin(ShapeType.FilledTriangle);
		shape().filledTriangle(
				e.getX() + e.getWidth() / 2 - 3, e.getY() + e.getHeight() / 4 + 8,
				e.getX() + e.getWidth() / 2 - 6, e.getY() + e.getHeight() / 4 + 5,
				e.getX() + e.getWidth() / 2, e.getY() + e.getHeight() / 4 + 5);
	}
	private void renderRightButton(SimpleButton e) {
		shape().end();
		shape().begin(ShapeType.FilledRectangle);
		shape().filledRect(e.getX() + e.getWidth() / 4 * 3 - 2,
							e.getY() + e.getHeight() / 4,
							2,
							e.getHeight() / 2 - 3);
		shape().filledRect(e.getX() + e.getWidth() / 4,
				e.getY() + e.getHeight() / 4,
				2,
				e.getHeight() / 2 - 3);
		shape().filledRect(e.getX() + e.getWidth() / 4,
							e.getY() + e.getHeight() / 4,
							e.getWidth() / 2,
							2);
		shape().end();
		shape().begin(ShapeType.FilledTriangle);
		shape().filledTriangle(
				e.getX() + e.getWidth() / 2 + 3, e.getY() + e.getHeight() / 4 + 8,
				e.getX() + e.getWidth() / 2, e.getY() + e.getHeight() / 4 + 5,
				e.getX() + e.getWidth() / 2 + 6, e.getY() + e.getHeight() / 4 + 5);
	}
	private void renderForwardButton(SimpleButton e) {
		shape().end();
		shape().begin(ShapeType.FilledRectangle);
		shape().filledRect(e.getX() + e.getWidth() / 2 - 1,
							e.getY() + e.getHeight() / 4 + 3,
							2,
							e.getHeight() / 2 - 3);
		shape().end();
		shape().begin(ShapeType.FilledTriangle);
		shape().filledTriangle(
				e.getX() + e.getWidth() / 2, e.getY() + e.getHeight() / 4,
				e.getX() + e.getWidth() / 2 - 3, e.getY() + e.getHeight() / 4 + 3,
				e.getX() + e.getWidth() / 2 + 3, e.getY() + e.getHeight() / 4 + 3);
	}
	private void renderSwapButton(SimpleButton e) {
		shape().end();
		shape().begin(ShapeType.FilledRectangle);
		shape().filledRect(e.getX() + e.getWidth() / 2 - 4,
							e.getY() + e.getHeight() / 4 + 3,
							2,
							e.getHeight() / 2 - 3);
		shape().filledRect(e.getX() + e.getWidth() / 2 + 2,
							e.getY() + e.getHeight() / 4,
							2,
							e.getHeight() / 2 - 3);
		shape().end();
		shape().begin(ShapeType.FilledTriangle);
		shape().filledTriangle(
				e.getX() + e.getWidth() / 2 - 3, e.getY() + e.getHeight() / 4,
				e.getX() + e.getWidth() / 2 - 6, e.getY() + e.getHeight() / 4 + 3,
				e.getX() + e.getWidth() / 2, e.getY() + e.getHeight() / 4 + 3);
		shape().filledTriangle(
				e.getX() + e.getWidth() / 2 + 3, e.getY() + e.getHeight() / 4 + 8,
				e.getX() + e.getWidth() / 2, e.getY() + e.getHeight() / 4 + 5,
				e.getX() + e.getWidth() / 2 + 6, e.getY() + e.getHeight() / 4 + 5);
	}
	private void renderProgramColorButton(SimpleRadio e, String color) {
		if(color.equals("red")) {
			shape().setColor(1.0f, 0f, 0f, 1.0f);
		} else if(color.equals("green")) {
			shape().setColor(0f, 1.0f, 0f, 1.0f);
		} else {
			shape().setColor(0f, 0f, 1.0f, 1.0f);
		}
		if(e.state()) {
			shape().filledRect(e.getX(), e.getY(), e.getWidth(), e.getHeight());
		} else {
			shape().rect(e.getX(), e.getY(), e.getWidth(), e.getHeight());
		}
	}

	private void renderPlayButton(SimpleToggle e) {
		shape().rect(e.getX(), e.getY(), e.getWidth(), e.getHeight());
		shape().end();
		shape().begin(ShapeType.FilledRectangle);
		if(e.state()) {
			shape().filledRect(e.getX() + e.getWidth() / 2 - 5,
					e.getY() + e.getHeight() / 3,
					3, e.getHeight() / 3);
			shape().filledRect(e.getX() + e.getWidth() / 2 + 2,
					e.getY() + e.getHeight() / 3,
					3, e.getHeight() / 3);
		} else {
			shape().end();
			shape().begin(ShapeType.FilledTriangle);
			shape().filledTriangle(
					e.getX() + e.getWidth() / 2 - 5, e.getY() + e.getHeight() / 3,
					e.getX() + e.getWidth() / 2 - 5, e.getY() + e.getHeight() / 3 * 2,
					e.getX() + e.getWidth() / 2 + 5, e.getY() + e.getHeight() / 2);
		}
	}
	private void renderStopButton(SimpleButton e) {
		shape().end();
		shape().begin(ShapeType.FilledRectangle);
		shape().filledRect(e.getX() + e.getWidth() / 3,
				e.getY() + e.getHeight() / 3,
				e.getWidth() / 3, e.getHeight() / 3);
	}
	private void renderRadios(SimpleRadioGroup e) {
		for(SimpleRadio button : e.getRadios()) {
			render(button);
		}
	}
}
