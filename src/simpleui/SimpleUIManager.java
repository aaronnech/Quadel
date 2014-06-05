package simpleui;
/*
 * Author: Aaron Nech
 * Project: SJGF
 * Description: Manages UI Components on the screen
 * 
 */
import gameabstract.InputEvent;
import gameabstract.InputHandler;
import gameabstract.Renderer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class SimpleUIManager implements InputHandler, Iterable<SimpleUIComponent> {
	private List<SimpleUIComponent> components;

	//Constructs a new manager
	public SimpleUIManager() {
		components = new ArrayList<SimpleUIComponent>();
	}
	
	//Managers listen for game input to dish to their components
	public void input(InputEvent e) {
		for(SimpleUIComponent component : components) {
			if(component.visible()) {
				component.react(e);
			}
			List<SimpleUIComponent> children = component.children();
			if(children != null)
				recursiveReact(children, e);
		}
		postReact();
	}
	
	//drills down into any children and asks for their reaction too
	private void recursiveReact(List<SimpleUIComponent> comps, InputEvent e) {
		for(SimpleUIComponent c : comps) {
			if(c.visible()) {
				c.react(e);
			}
			List<SimpleUIComponent> children = c.children();
			if(children != null)
				recursiveReact(children, e);
		}
	}
	
	//Post react hook for extensions of this class
	//Checks for UI removal requests
	protected void postReact() {
		checkRemoves();
	}
	
	//pre react hook for extensions of this class
	protected void preReact() {}
	
	//Checks for removal requests in components
	private void checkRemoves() {
		Iterator<SimpleUIComponent> i = components.iterator();
		while(i.hasNext()) {
			if(i.next().markedDelete())
				i.remove();
		}
	}
	
	//renders all components given a rendering engine
	public void renderComponents(Renderer r) {
		for(SimpleUIComponent component : components) {
			if(component.visible())
				r.render(component);
		}
	}
	
	//Adds a component to the manager
	public void addComponent(SimpleUIComponent c) {
		components.add(c);
	}
	
	//Adds a component and subscription to the manager
	public void addComponent(SimpleUIComponent c, SimpleUIListener listener,
			SimpleEventType action, String methodName) {
		c.subscribe(listener, action, methodName);
		addComponent(c);
	}
	
	//adds multiple components at once to the manager
	public void addAllComponents(Collection<? extends SimpleUIComponent> c) {
		components.addAll(c);
	}
	
	//gets a component by name from the manager
	public SimpleUIComponent getComponent(String name) {
		for(SimpleUIComponent component : components) {
			System.out.println(component);
			System.out.println("TEST: " + component.getName() + " == " + name);
			if(component.getName().equals(name))
				return component;
		}
		return null;
	}
	
	//removes a specific component from the manager
	public void removeComponent(SimpleUIComponent c) {
		components.remove(c);
	}
	
	//removes a component by name from the manager
	public void removeComponent(String name) {
		Iterator<SimpleUIComponent> i = components.iterator();
		while(i.hasNext()) {
			if(i.next().getName().equals(name))
				i.remove();
		}
	}
	
	//clears all components from the manager
	public void clearComponents() {
		components.clear();
	}
	
	//counts the number of components
	public int count() {
		return components.size();
	}
	
	//subscribes a specific named component to a listener
	public void subscribe(String name, SimpleUIListener listener,
			SimpleEventType action, String methodName) {
		for(SimpleUIComponent c : components) {
			if(c.getName().equals(name)) {
				c.subscribe(listener, action, methodName);
			}
			List<SimpleUIComponent> children = c.children();
			if(children != null)
				recursiveSubscribeByName(name, children, listener, action, methodName);
		}
	}
	
	//recursively subscribes to children of components by name
	private void recursiveSubscribeByName(String name,
			List<SimpleUIComponent> comps, SimpleUIListener listener,
			SimpleEventType action, String methodName) {
		for(SimpleUIComponent c : comps) {
			if(c.getName().equals(name)) {
				c.subscribe(listener, action, methodName);
			}
			List<SimpleUIComponent> children = c.children();
			if(children != null)
				recursiveSubscribeByName(name, children, listener, action, methodName);
		}
	}

	//subscribes all components to a listener
	public void subscribeToAll(SimpleUIListener listener,
			SimpleEventType action, String methodName) {
		for(SimpleUIComponent c : components) {
			c.subscribe(listener, action, methodName);
			List<SimpleUIComponent> children = c.children();
			if(children != null)
				recursiveSubscribe(children, listener, action, methodName);
		}
	}
	
	//recursively subscribes to children of components
	private void recursiveSubscribe(List<SimpleUIComponent> comps, SimpleUIListener listener,
			SimpleEventType action, String methodName) {
		for(SimpleUIComponent c : comps) {
			c.subscribe(listener, action, methodName);
			List<SimpleUIComponent> children = c.children();
			if(children != null)
				recursiveSubscribe(children, listener, action, methodName);
		}
	}
	
	//clears all component subscriptions
	public void clearSubscriptions() {
		for(SimpleUIComponent c : components) {
			c.clearSubscriptions();
			List<SimpleUIComponent> children = c.children();
			if(children != null)
				recursiveClearSubscriptions(children);
		}
	}
	
	//clears all subscriptions from children recursively
	private void recursiveClearSubscriptions(List<SimpleUIComponent> comps) {
		for(SimpleUIComponent c : comps) {
			c.clearSubscriptions();
			List<SimpleUIComponent> children = c.children();
			if(children != null)
				recursiveClearSubscriptions(children);
		}
	}
	
	//gets a iterator for components
	public Iterator<SimpleUIComponent> iterator() {
		return components.iterator();
	}
}
