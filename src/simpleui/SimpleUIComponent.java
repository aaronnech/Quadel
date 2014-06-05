package simpleui;
/*
 * Author: Aaron Nech
 * Project: SJGF
 * Description: The basis for all UI components
 * Implements renderable since ui components are rendereable
 * 
 */
import gameabstract.InputEvent;
import gameabstract.Renderable;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public abstract class SimpleUIComponent implements Renderable {
	protected String name;
	protected float x, y;
	protected float width, height;
	protected boolean hide;
	
	private Object message;
	private boolean markDelete;
	
	private ArrayList<SimpleSubscription> subscriptions;
	
	//UI Components can have a name location, and dimension
	public SimpleUIComponent(String name, float x, float y, float width,
			float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.name = name;
		this.markDelete = false;
		subscriptions = new ArrayList<SimpleSubscription>();
	}
	
	//UI components can also just be constructed with no fields
	public SimpleUIComponent() {
		subscriptions = new ArrayList<SimpleSubscription>();
	}
	
	//called by the UI manager to react to specific input events
	abstract public boolean react(InputEvent event);
	
	//Getters/Setters for ui component properties
	public final String getName() {
		return name;
	}
	public final void setName(String name) {
		this.name = name;
	}
	public final float getX() {
		return x;
	}
	public final float getY() {
		return y;
	}
	public final void setX(float x) {
		this.x = x;
	}
	public final void setY(float y) {
		this.y = y;
	}
	public final float getWidth() {
		return width;
	}
	public final void setWidth(float width) {
		this.width = width;
	}
	public final float getHeight() {
		return height;
	}
	public final void setHeight(float height) {
		this.height = height;
	}
	
	//Hides and shows ui components
	public final void hide() {
		hide = true;
	}
	public final void show() {
		hide = false;
	}
	
	//returns true if the component is not hidden
	public final boolean visible() {
		return !hide;
	}
	
	//marks a component for deletion
	public final void delete() {
		markDelete = true;
	}
	
	//returns true if this component is ready to be deleted
	public final boolean markedDelete() {
		return markDelete;
	}
	
	//sets the message of this component
	public final void setMessage(Object o) {
		message = o;
	}
	
	//gets the message of this component
	public final Object getMessage() {
		return message;
	}
	
	//connects this component to a SimpleUIListener
	public final void subscribe(SimpleUIListener listener, SimpleEventType action,
			String methodName) {
		try {
			Method method = listener.getClass().getMethod(methodName, SimpleEvent.class);
			subscriptions.add(new SimpleSubscription(method, action, listener));
		} catch (Exception e) {
			throw new IllegalArgumentException("Bad subscription! " + methodName);
		}
	}
	
	//clears all connections to listeners
	public final void clearSubscriptions() {
		subscriptions.clear();
	}
	
	//fires event to subscribers
	protected final void fireEvent(SimpleEvent e) {
		for(SimpleSubscription subscription : subscriptions) {
			if(subscription.action == e.getAction()) {
				try {
					subscription.method.invoke(subscription.subscriber, e);
				} catch (Exception ex) {
				}
			}
		}
	}
	
	//Bounding box test to see if a location is within the component
	protected boolean inBoundingBox(int tx, int ty) {
		return tx >= x && ty >= y &&
				tx <= x + width && ty <= y + height;
	}
	
	//Implemented by extenders to allow access to children components of the component
	protected List<SimpleUIComponent> children() {
		return null;
	}
}
