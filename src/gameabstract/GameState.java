package gameabstract;
/*
 * Author: Aaron Nech
 * Project: SJGF
 * Description: this class abstracts the GameState. To use, simply extend this GameState
 * and fill in the missing functionality
 * 
 */

public abstract class GameState implements InputHandler{
	private GameInput inputProcessor;
	
	//constructs a GameState with a given input processor
	public GameState(GameInput i) {
		inputProcessor = i;
		inputProcessor.subscribe(this);
	}
	//called every gameloop with the delta from the last frame
	abstract public void update(float delta);
	//called every gameloop with an abstract rendering engine
	abstract public void render(Renderer r);
	//used to reset the state of the gamestate
	abstract public void reset();
	//method to process input, usually called in gameloop
	protected final void processInput() {
		inputProcessor.process();
	}
}
