package gameabstract;
/*
 * Author: Aaron Nech
 * Project: SJGF
 * Description: LibGDX 2D implementation of the abstract Renderer, 
 * Uses orthographic camera techniques to render 2 dimensional games
 * 
 */
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class GDX2DRenderer implements Renderer {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private TextureAtlas atlas;
	private ShapeRenderer shape;
	private BitmapFont debug;
	private Rectangle viewport;
	
	//creates a new 2d renderer with a default texture atlas
	public GDX2DRenderer(TextureAtlas a, int w, int h) {
		this(w, h);
		atlas = a;
	}
	
	//creates a new 2d renderer that does not have a texture atlas
	public GDX2DRenderer(int w, int h) {
		camera = new OrthographicCamera();
		viewport = new Rectangle();
		setCameraVirtual(w, h);
		viewport.width = Gdx.graphics.getWidth();
		viewport.height = Gdx.graphics.getHeight();;
		debug = new BitmapFont();
		shape = new ShapeRenderer();
		batch = new SpriteBatch();
		debug.setScale(1.0f, -1.0f);
	}
	
	//Resizes the viewport, but keeps the original aspect ratio
	public void resizeKeepAspect(int width, int height) {
		//old aspect ratio
		double oldAspect = viewport.width / viewport.height;
		// calculate new viewport
		double aspectRatio = (float) width / (float) height;
		float scale = 1f;
		
		Vector2 crop = new Vector2(0f, 0f);
		if(aspectRatio > oldAspect) {
			scale = (float)height/(float)viewport.height;
			crop.x = (width - viewport.width * scale) / 2f;
		} else if(aspectRatio < oldAspect) {
			scale = (float) width / viewport.width;
		    crop.y = (height - viewport.height * scale) / 2f;
		} else {
			scale = (float) width / (float) viewport.width;
		}
		
		float w = (float)viewport.width*scale;
		float h = (float)viewport.height*scale;
		viewport.x = crop.x;
		viewport.y = crop.y;
		viewport.width = w;
		viewport.height = h;
	}
	
	//gets the viewport of the renderer
	public Rectangle viewport() {
		return viewport;
	}
	 
	//sets the camera viewport to a given width and height
	public void setCameraVirtual(int w, int h) {
		camera.setToOrtho(true, w, h);
	}
	
	//returns the SpriteBatch libGDX object for rendering
	public SpriteBatch batch() {
		return batch;
	}
	
	//returns the ShapeRenderer libGDX object for rendering
	public ShapeRenderer shape() {
		return shape;
	}
	
	//returns the TextureAtlas libGDX object for rendering
	public TextureAtlas atlas() {
		return atlas;
	}
	
	
	//returns the Camera libGDX object
	public Camera camera() {
		return camera;
	}
	
	public BitmapFont debugText() {
		return debug;
	}
	
	//clears the screen with black
	public void cls() {
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	}
	
	//recieves notifications from game events such as viewport resizing
	public void notify(String event, Object msg) {
		if(event.equals("resize")) {
			int[] dimensions = (int[]) msg;
			resizeKeepAspect(dimensions[0], dimensions[1]);
		}
	}
	
	//called to render something renderable
	abstract public void render(Renderable e);
	
	//called at the beginning of every render pass
	public void begin() {
		camera().update();
        camera.apply(Gdx.gl10);     
        // set viewport
        Gdx.gl.glViewport((int) viewport.x, (int) viewport.y,
                          (int) viewport.width, (int) viewport.height);
		shape().setProjectionMatrix(camera().combined);
		batch().setProjectionMatrix(camera().combined);
	}
	//called at the end of every render pass
	abstract public void end();
}
