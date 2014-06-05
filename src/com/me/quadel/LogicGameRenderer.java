package com.me.quadel;

import simpleui.SimpleButton;
import simpleui.SimpleRadio;
import simpleui.SimpleRadioGroup;
import simpleui.SimpleToggle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.ScreenUtils;

import gameabstract.GDX2DRenderer;
import gameabstract.Renderable;

//production renderer. Final images in place.
public class LogicGameRenderer extends GDX2DRenderer {
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 768;
	
	//texture regions
	private TextureRegion wall;
	private TextureRegion space;
	private TextureRegion rBot;
	private TextureRegion gBot;
	private TextureRegion bBot;
	private TextureRegion rBit;
	private TextureRegion gBit;
	private TextureRegion bBit;
	private TextureRegion forward;
	private TextureRegion swap;
	private TextureRegion ccw;
	private TextureRegion cw;
	private TextureRegion stop;
	private TextureRegion play;
	private TextureRegion pause;	
	
	public LogicGameRenderer(TextureAtlas a, int w, int h) {
		super(a, WIDTH, HEIGHT);
		wall = a.findRegion("wall");
		space = a.findRegion("space");
		rBot = a.findRegion("rbot");
		gBot = a.findRegion("gbot");
		bBot = a.findRegion("bbot");
		rBit = a.findRegion("rbit");
		gBit = a.findRegion("gbit");
		bBit = a.findRegion("bbit");
		forward = a.findRegion("forward");
		swap = a.findRegion("reverse");
		ccw = a.findRegion("ccw");
		cw = a.findRegion("cw");
		stop = a.findRegion("stop");
		play = a.findRegion("play");
		pause = a.findRegion("pause");
		batch().enableBlending();
		batch().setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
		debugText().setScale(2.0f, -2.0f);
	}
	
	//Rendering hooks
	//---------------
	@Override
	//main router method to draw any renderable item we recognize
	public void render(Renderable e) {
		Class<? extends Renderable> type = e.getClass();
		if(type == Bot.class) {
			renderBot((Bot) e);
		} else if(type == Bit.class) {
			renderBit((Bit) e);
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

	@Override
	//hooks into the beginning before we render things. Called once per loop
	public void begin() {
		super.begin();
		Gdx.gl.glEnable(GL10.GL_BLEND);
		Gdx.gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
		cls();
	}

	@Override
	//hooks into the ending after we render things. Called once per loop
	public void end() {

	}
	
	//Tile Rendering
	//--------------
	private void renderMap(GameMap m) {
		for(int x = 0; x < GameMap.WIDTH; x++) {
			for(int y = 0; y < GameMap.HEIGHT; y++) {
				batch().begin();
					if(m.isWall(x, y)) {
						renderWall(m, x, y);
					} else {
						renderNormal(m, x, y);
					}
				batch().end();
			}
		}
	}
	private void renderNormal(GameMap map, int x, int y) {
		batch().draw(space, x * map.TILE_SIZE - 6, y * map.TILE_SIZE - 6, 0, 0, space.getRegionWidth(),
				space.getRegionHeight(), 1.0f, 1.0f, 0);
	}
	private void renderWall(GameMap map, int x, int y) {
		batch().draw(wall, x * map.TILE_SIZE - 6, y * map.TILE_SIZE - 6, 0, 0, wall.getRegionWidth(),
				wall.getRegionHeight(), 1.0f, 1.0f, 0);
	}
	
	//Button Rendering
	//--------------
	
	//renders an arbitrary button
	private void renderButton(SimpleButton e) {
		if(e.getName().equals("toggle")) { 
			renderPlayButton((SimpleToggle) e);
		} else if("redgreenblue".indexOf(e.getName()) != -1) {
			renderProgramColorButton((SimpleRadio) e, e.getName());
		} else {
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
			} else {
				shape().begin(ShapeType.Rectangle);
				shape().setColor(1.0f, 1.0f, 1.0f, 1.0f);
				shape().rect(e.getX(), e.getY(), e.getWidth(), e.getHeight());
				shape().end();
				if(!e.getText().equals("")) {
					batch().begin();
					debugText().setColor(1.0f, 1.0f, 1.0f, 1.0f);
					debugText().draw(batch(), e.getText(),
							e.getX() + (e.getWidth() / 2) - (e.getLabel().getWidth() / 2),
							e.getY() + (e.getHeight() / 2) - (e.getLabel().getHeight() / 2));
					batch().end();
				}
			}
		}
	}
	
	//Below are the specific button rendering routines
	//Executed for each renderable button
	private void renderLeftButton(SimpleButton e) {
		batch().begin();
		batch().draw(ccw, e.getX(), e.getY(), ccw.getRegionWidth() / 2, ccw.getRegionWidth() / 2, ccw.getRegionWidth(),
				ccw.getRegionHeight(), 1.0f, -1.0f, 0);
		batch().end();
	}
	private void renderRightButton(SimpleButton e) {
		batch().begin();
		batch().draw(cw, e.getX(), e.getY(), cw.getRegionWidth() / 2, cw.getRegionWidth() / 2, cw.getRegionWidth(),
				cw.getRegionHeight(), 1.0f, -1.0f, 0);
		batch().end();
	}
	private void renderForwardButton(SimpleButton e) {
		batch().begin();
		batch().draw(forward, e.getX(), e.getY(), forward.getRegionWidth() / 2, forward.getRegionWidth() / 2, forward.getRegionWidth(),
				forward.getRegionHeight(), 1.0f, -1.0f, 0);
		batch().end();
	}
	private void renderSwapButton(SimpleButton e) {
		batch().begin();
		batch().draw(swap, e.getX(), e.getY(), swap.getRegionWidth() / 2, swap.getRegionWidth() / 2, swap.getRegionWidth(),
				swap.getRegionHeight(), 1.0f, -1.0f, 0);
		batch().end();
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
			shape().begin(ShapeType.FilledRectangle);
			shape().filledRect(e.getX(), e.getY(), e.getWidth(), e.getHeight());
		} else {
			shape().begin(ShapeType.Rectangle);
			shape().rect(e.getX(), e.getY(), e.getWidth(), e.getHeight());
		}
		shape().end();
	}

	private void renderPlayButton(SimpleToggle e) {
		batch().begin();
		if(e.state()) { //show pause
			batch().draw(pause, e.getX(), e.getY(), pause.getRegionWidth() / 2, pause.getRegionWidth() / 2, pause.getRegionWidth(),
					pause.getRegionHeight(), 1.0f, -1.0f, 0);
		} else { //show play
			batch().draw(play, e.getX(), e.getY(), play.getRegionWidth() / 2, play.getRegionWidth() / 2, play.getRegionWidth(),
					play.getRegionHeight(), 1.0f, -1.0f, 0);
		}
		batch().end();
	}
	private void renderStopButton(SimpleButton e) {
		batch().begin();
		batch().draw(stop, e.getX(), e.getY(), stop.getRegionWidth() / 2, stop.getRegionWidth() / 2, stop.getRegionWidth(),
				stop.getRegionHeight(), 1.0f, -1.0f, 0);
		batch().end();
	}
	private void renderRadios(SimpleRadioGroup e) {
		for(SimpleRadio button : e.getRadios()) {
			render(button);
		}
	}
	
	//Entity Rendering
	//--------------
	
	private void renderBot(Bot e) {
		batch().begin();
		switch(e.getTeam()) {
			case RED:
				batch().draw(rBot, e.getX() *  GameMap.TILE_SIZE, e.getY() * GameMap.TILE_SIZE , rBot.getRegionWidth() / 2, rBot.getRegionWidth() / 2, rBot.getRegionWidth(),
						rBot.getRegionHeight(), 1.0f, -1.0f, Entity.Direction.toDegrees(e.getDirection()) + 90);
				break;
			case GREEN:
				batch().draw(gBot, e.getX() *  GameMap.TILE_SIZE, e.getY() * GameMap.TILE_SIZE, gBot.getRegionWidth() / 2, gBot.getRegionWidth() / 2,  gBot.getRegionWidth(),
						gBot.getRegionHeight(), 1.0f, -1.0f, Entity.Direction.toDegrees(e.getDirection()) + 90);
				break;
			case BLUE:
				batch().draw(bBot, e.getX() *  GameMap.TILE_SIZE , e.getY() * GameMap.TILE_SIZE , bBot.getRegionWidth() / 2, bBot.getRegionWidth() / 2, bBot.getRegionWidth(),
						bBot.getRegionHeight(), 1.0f, -1.0f, Entity.Direction.toDegrees(e.getDirection()) + 90);
				break;
		}
		batch().end();
	}
	private void renderBit(Bit e) {
		batch().begin();
		switch(e.getColor()) {
			case RED:
				batch().draw(rBit, e.getX() *  GameMap.TILE_SIZE + 20, e.getY() * GameMap.TILE_SIZE + 20, rBit.getRegionWidth() / 2 , rBit.getRegionHeight() / 2, rBit.getRegionWidth(),
						rBit.getRegionHeight(), 1.0f, -1.0f, 0);
				break;
			case GREEN:
				batch().draw(gBit, e.getX() *  GameMap.TILE_SIZE + 20, e.getY() * GameMap.TILE_SIZE + 20, gBit.getRegionWidth() / 2 , gBit.getRegionHeight() / 2,  gBit.getRegionWidth(),
						gBit.getRegionHeight(), 1.0f, -1.0f, 0);
				break;
			case BLUE:
				batch().draw(bBit, e.getX() *  GameMap.TILE_SIZE + 20, e.getY() * GameMap.TILE_SIZE + 20, bBit.getRegionWidth() / 2, bBit.getRegionHeight() / 2, bBit.getRegionWidth(),
						bBit.getRegionHeight(), 1.0f, -1.0f, 0);
				break;
		}
		batch().end();
		
	}

	private void renderHole(Hole e) {
		int count = 0;
		int containCount = e.contentSize();
		for(Color color : e.getSequence()) {
			switch(color) {
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
			int x = e.getX() * GameMap.TILE_SIZE + 4;
			int w = GameMap.TILE_SIZE - 8;
			int y = e.getY() * GameMap.TILE_SIZE + (count * 20) + 6;
			int h = (GameMap.TILE_SIZE - 6) / 3;
			if(containCount > count) {
				shape().begin(ShapeType.FilledRectangle);
				shape().filledRect(x, y, w, h);
			} else {
				shape().begin(ShapeType.Rectangle);
				shape().rect(x, y, w, h);				
			}
			count++;
		}
		shape().end();
	}
}
