package simpleui;

import gameabstract.InputEvent;
/*
 * Author: Aaron Nech
 * Project: SJGF
 * Description: This class represents a button
 * 
 */
public class SimpleButton extends SimpleUIComponent {
	private SimpleLabel label;
	
	//Constructs a button from a given name, location, and size
	public SimpleButton(String n, float x, float y, float width, float height) {
		super(n, x, y, width, height);
		label = new SimpleLabel(n, "", x, y);
	}
	
	//called by the UI manager when a input event is detected
	public boolean react(InputEvent e) {
		if(e.getType().equals("tap") && 
				inBoundingBox(((int[]) e.getData())[0], ((int[]) e.getData())[1])) {
			SimpleEvent event = new SimpleEvent(this, SimpleEventType.TOUCH, getMessage());
			fireEvent(event);
			return true;
		}
		return false;
	}
	
	//sets the text of a button (through the label)
	public void setText(String value) {
		label.setTextValue(value);
	}
	
	//gets the text of a button (through the label)
	public String getText() {
		return label.getTextValue();
	}
	
	//gets the label object of the button
	public SimpleLabel getLabel() {
		return label;
	}
	
	//hugs the label dimensions
	public void fitToText(boolean width, boolean height) {
		if(width)
			this.setWidth(label.getWidth());
		if(height)
			this.setHeight(label.getHeight());
	}

}
