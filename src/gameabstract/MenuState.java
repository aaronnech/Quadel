package gameabstract;

/*
 * Author: Aaron Nech
 * Project: SJGF
 * Description: this class abstracts the MenuState. To use, simply extend this MenuState
 * and fill in the missing functionality
 */

import simpleui.SimpleUIListener;
import simpleui.SimpleUIManager;

public abstract class MenuState implements InputHandler, SimpleUIListener {
	private GameInput inputProcessor;
	protected SimpleUIManager ui;

	
	//constructs a GameState with a given input processor
	public MenuState(GameInput i) {
		inputProcessor = i;
		inputProcessor.subscribe(this);
		ui = new SimpleUIManager();
		i.subscribe(ui);
		setupUI();
	}
	//called to setup the UI of the menu
	abstract protected void setupUI();
	//called every gameloop with the delta from the last frame
	public void update(float delta) {
		processInput();
	}
	//menu states have no required traditional input, they are ui driven
	public void input(InputEvent e) {

	}
	//called every gameloop with an abstract rendering engine
	public void render(Renderer r) {
		r.begin();
		ui.renderComponents(r);
		r.end();
	}
	//used to reset the state of the menustate
	abstract public void reset();
	//method to process input, usually called in gameloop
	protected final void processInput() {
		inputProcessor.process();
	}
}