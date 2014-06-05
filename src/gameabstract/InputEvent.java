package gameabstract;
/*
 * Author: Aaron Nech
 * Project: SJGF
 * Description: this class represents a Input Event to be processed by a GameInput object
 * 
 */

public class InputEvent {
	private String type;
	private Object data;
	
	//Stores an abstract object as data
	//Stores a type to distinguish the input type
	public InputEvent(String type, Object data) {
		this.data = data;
		this.type = type;
	}
	public Object getData() {
		return data;
	}
	public String getType() {
		return type;
	}
}
