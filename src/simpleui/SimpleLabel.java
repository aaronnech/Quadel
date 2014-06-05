package simpleui;

import gameabstract.InputEvent;
/*
 * Author: Aaron Nech
 * Project: SJGF
 * Description: Simple Label. A GUI component that is just text. (of course rendering implementation is up to you)
 * 
 */

public class SimpleLabel extends SimpleUIComponent {
	public static final int DEFAULT_FONT_WIDTH = 8;
	public static final int DEFAULT_FONT_SIZE = 14;
	
	private String textValue;
	private int fontWidth;
	private int fontSize;

	
	//Constructs a button from a given name, location, and size
	public SimpleLabel(String n, String val, float x, float y) {
		super(n, x, y, DEFAULT_FONT_WIDTH * val.length(), DEFAULT_FONT_SIZE);
		textValue = val;
		fontWidth = DEFAULT_FONT_WIDTH;
		fontSize = DEFAULT_FONT_SIZE;
		matchTextDimensions();
	}
	
	private void matchTextDimensions() {
		if(textValue != null)
			setWidth(getFontWidth() * textValue.length());
		setHeight(getFontSize());
	}
	
	//sets the current font width
	public void setFontWidth(int value) {
		fontWidth = value;
		matchTextDimensions();
	}
	
	//returns the current font width
	public int getFontWidth() {
		return fontWidth;
	}
	
	//sets the current font size
	public void setFontSize(int value) {
		fontSize = value;
		matchTextDimensions();
	}
	
	//returns the current font size
	public int getFontSize() {
		return fontSize;
	}
	
	//sets the current text value
	public void setTextValue(String value) {
		textValue = value;
		matchTextDimensions();
	}
	
	//gets and returns the value of the label
	public String getTextValue() {
		return textValue;
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
}
