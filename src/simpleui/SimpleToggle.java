package simpleui;
/*
 * Author: Aaron Nech
 * Project: SJGF
 * Description: Simple Toggle button. Has an on/off state
 * 
 */
import gameabstract.InputEvent;

public class SimpleToggle extends SimpleButton {
	private boolean toggleState;
	
	//toggle buttons have a name, location, and dimension
	//initially toggled to off
	public SimpleToggle(String n, float x, float y, float width, float height) {
		super(n, x, y, width, height);
		toggleState = false;
	}
	
	//called by the ui manager to react to input
	public boolean react(InputEvent e) {
		boolean clicked = super.react(e);
		if(clicked) {
			SimpleEventType type = SimpleEventType.TOGGLE_FALSE;
			if(toggleState)
				type = SimpleEventType.TOGGLE_TRUE;
			SimpleEvent event = new SimpleEvent(this, type, getMessage());
			fireEvent(event);
		}
		return clicked;
	}
	
	//toggles the state of the button
	public void toggle() {
		toggleState = !toggleState;
	}
	
	//sets the state of the button
	public void set(boolean value) {
		toggleState = value;
	}
	
	//returns the state of the button
	public boolean state() {
		return toggleState;
	}
}
