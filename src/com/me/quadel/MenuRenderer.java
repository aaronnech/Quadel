package com.me.quadel;

import java.util.Arrays;

import simpleui.SimpleButton;
import simpleui.SimpleLabel;
import simpleui.SimpleRadio;
import simpleui.SimpleTreeItem;
import simpleui.SimpleTreeView;
import simpleui.SimpleUIComponent;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import gameabstract.GDX2DRenderer;
import gameabstract.Renderable;

public class MenuRenderer extends GDX2DRenderer {
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 768;
	
	public MenuRenderer() {
		super(WIDTH, HEIGHT);
		debugText().setScale(2.0f, -2.0f);
	}

	public void render(Renderable e) {
		shape().setProjectionMatrix(camera().combined);
		batch().setProjectionMatrix(camera().combined);
		if(e.getClass() == SimpleButton.class) {
			renderButton((SimpleButton) e);
		} else if(e.getClass() == SimpleTreeView.class) {
			renderLevels((SimpleTreeView) e);
		} else if(e.getClass() == SimpleLabel.class) {
			renderLabel((SimpleLabel) e);
		} else if(e.getClass() == SimpleRadio.class) {
			renderLevelSelects((SimpleRadio) e);
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

	public void end() {
		
	}
	
	//Render methods
	private void renderButton(SimpleButton e) {
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
	
	private void renderLabel(SimpleLabel e) {
		batch().begin();
		debugText().setColor(1.0f, 1.0f, 1.0f, 1.0f);
		debugText().draw(batch(), e.getTextValue(), e.getX(), e.getY());
		batch().end();
	}
	private void renderLevelSelects(SimpleRadio e) {
		if(e.state()) {
			debugText().setColor(1.0f, 1.0f, 1.0f, 1.0f);
			
		} else {
			debugText().setColor(.7f, .7f, .7f, 1.0f);
		}
		batch().begin();
		debugText().draw(batch(), e.getText(), e.getX(), e.getY());
		batch().end();
	}
	private void renderLevels(SimpleTreeView e) {
		//shape().begin(ShapeType.Rectangle);
		//shape().setColor(1.0f, 1.0f, 1.0f, 1.0f);
		//shape().rect(e.getX(), e.getY(), e.getWidth(), e.getHeight());
		//shape().end();
		for(SimpleUIComponent item : e.allComponents()) {
			render(item);
		}
	}
}
