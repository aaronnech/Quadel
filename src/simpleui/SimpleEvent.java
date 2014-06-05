package simpleui;
/*
 * Author: Aaron Nech
 * Project: SJGF
 * Description: A UI event that can be fired
 * 
 */
public class  SimpleEvent {
	private SimpleUIComponent origin;
	private SimpleEventType actionType;
	private Object message;
	
	//UI Events must have a source, a type, and a message
	public SimpleEvent(SimpleUIComponent o, SimpleEventType a, Object m) {
		origin = o;
		actionType = a;
		message = m;
	}
	
	//getters for properties of the event
	public SimpleUIComponent getComponent() {
		return origin;
	}
	public SimpleEventType getAction() {
		return actionType;
	}
	public Object getMessage() {
		return message;
	}
}
