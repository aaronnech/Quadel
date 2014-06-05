package simpleui;
/*
 * Author: Aaron Nech
 * Project: SJGF
 * Description: Simple Radio button with a on/off state inherited from toggle
 * 
 */
import gameabstract.InputEvent;

public class SimpleRadio extends SimpleToggle {
	private SimpleRadioGroup group;
	
	//Radio Buttons have a name, location, and dimension
	public SimpleRadio(String n, float x, float y, float width,
			float height) {
		super(n, x, y, width, height);
	}
	
	//Called by the ui manager to react to a specific input event
	public boolean react(InputEvent e) {
		boolean clicked = super.react(e);
		if(clicked) {
			if(group != null)
				group.select(this);
			SimpleEvent event = new SimpleEvent(this, SimpleEventType.SELECT, getMessage());
			fireEvent(event);
		}
		return clicked;
	}
	
	//sets the group that this button belongs to
	public void setGroup(SimpleRadioGroup simpleRadioGroup) {
		group = simpleRadioGroup;
	}
	
}
