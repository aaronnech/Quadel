package gameabstract;
/*
 * Author: Aaron Nech
 * Project: SJGF
 * Description: LibGDX bridge for abstract gameinput engine
 * 
 */
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;


public class GDXGameInput extends GameInput implements InputProcessor {
	private Rectangle touchContext;
	private static final int TAP_PADDING = 8;
	
	//click watching variables
	private boolean touchDown;
	private float tx, ty;
	
	private int virtualWidth, virtualHeight;
	
	//constructs a new gameinput, setting LibGDX's default processor to this
	public GDXGameInput() {
		super();
		Gdx.input.setInputProcessor(this);
		touchContext = new Rectangle();
	}
	//sets the conversion factor for touch events so they report correctly
	public void setTouchContext(Rectangle context, int width, int height) {
		touchContext = context;
		virtualWidth = width;
		virtualHeight = height;
	}
	public boolean tap(int screenX, int screenY, int pointer, int button) {
		/*int[] passed = {(int) (screenX * ((float) touchContext.width / Gdx.graphics.getWidth()) + touchContext.x),
				(int) (screenY * ((float) touchContext.height / Gdx.graphics.getHeight()) + touchContext.y), pointer, button};*/
		Vector3 touched = screenToWorld(screenX, screenY);
		int[] passed = {(int) touched.x, (int) touched.y, pointer, button};
		add(new InputEvent("tap", passed));
		return true;
	}
	
	//bridge methods to libgdx input processor
	//all methods create a new input event to feed to the processor queue.
	public boolean keyDown(int keycode) {
		add(new InputEvent("keyDown", keycode));
		return true;
	}
	public boolean keyUp(int keycode) {
		add(new InputEvent("keyUp", keycode));
		return true;
	}
	public boolean keyTyped(char character) {
		add(new InputEvent("keyTyped", character));
		return true;
	}
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		/*
		int[] passed = {(int) (screenX * ((float) touchContext.width / Gdx.graphics.getWidth()) + touchContext.x),
				(int) (screenY * ((float) touchContext.height / Gdx.graphics.getHeight()) + touchContext.y), pointer, button};
		*/
		Vector3 touched = screenToWorld(screenX, screenY);
		int[] passed = {(int) touched.x, (int) touched.y, pointer, button};
		add(new InputEvent("touchDown", passed));
		tx = screenX;
		ty = screenY;
		touchDown = true;
		return true;
	}
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		/* int[] passed = {(int) (screenX * ((float) touchContext.width / Gdx.graphics.getWidth()) + touchContext.x),
				(int) (screenY * ((float) touchContext.height / Gdx.graphics.getHeight()) + touchContext.y), pointer, button}; */
		Vector3 touched = screenToWorld(screenX, screenY);
		int[] passed = {(int) touched.x, (int) touched.y, pointer, button};
		add(new InputEvent("touchUp", passed));
		if(touchDown) {
			if(tx <= screenX + TAP_PADDING && tx >= screenX - TAP_PADDING &&
					ty <= screenY + TAP_PADDING && ty >= screenY - TAP_PADDING)
				tap(screenX, screenY, pointer, button);
			touchDown = false;
		}
		return true;
	}
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		/*int[] passed = {(int) (screenX * ((float) touchContext.width / Gdx.graphics.getWidth()) + touchContext.x),
				(int) (screenY * ((float) touchContext.height / Gdx.graphics.getHeight()) + touchContext.y), pointer}; */
		Vector3 touched = screenToWorld(screenX, screenY);
		int[] passed = {(int) touched.x, (int) touched.y, pointer};
		add(new InputEvent("touchDragged", passed));
		return true;
	}
	public boolean mouseMoved(int screenX, int screenY) {
		/*int[] passed = {(int) (screenX * ((float) touchContext.width / Gdx.graphics.getWidth()) + touchContext.x),
				(int) (screenY * ((float) touchContext.height / Gdx.graphics.getHeight()) + touchContext.y)};*/
		Vector3 touched = screenToWorld(screenX, screenY);
		int[] passed = {(int) touched.x, (int) touched.y};
		add(new InputEvent("mouseMoved", passed));
		return true;
	}
	public boolean scrolled(int amount) {
		add(new InputEvent("scrolled", amount));
		return true;
	}
	
	private Vector3 screenToWorld(int x, int y) {
		Vector3 pos = new Vector3();
		pos.set((x - touchContext.x) * ((virtualWidth) / touchContext.width), (y - touchContext.y) * ((virtualHeight) / touchContext.height), 0);
		//System.out.println(touchContext.toString());
		//System.out.println(pos.toString());
		return pos;
	}

}
