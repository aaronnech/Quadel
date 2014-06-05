package gameabstract;

/*
 * Author: Aaron Nech
 * Project: SJGF
 * Description: This class abstracts the notion of a queued input processor
 * To use, extend a specific input context to bridge this queue to a
 * known input way
 * 
 */
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public abstract class GameInput {
	private Queue<InputEvent> inputQueue;
	private List<InputHandler> subscribers;
	
	//constructs a input queue
	public GameInput() {
		inputQueue = new LinkedList<InputEvent>();
		subscribers = new ArrayList<InputHandler>();
	}
	
	//Process the queue of input sending events to a input handler
	//implementation (usually a game state)
	public void process() {
		while(!inputQueue.isEmpty()) {
			InputEvent event = inputQueue.remove();
			for(InputHandler i : subscribers)
				i.input(event);
		}
	}
	
	//add a input handler to feed our events to
	public void subscribe(InputHandler subscriber) {
		subscribers.add(subscriber);
	}
	
	//adds a event to the input queue to be processed
	protected void add(InputEvent e) {
		inputQueue.add(e);
	}
}
