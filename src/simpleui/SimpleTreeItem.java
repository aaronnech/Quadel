package simpleui;

/*
 * Author: Aaron Nech
 * Project: SJGF
 * Description: Simple Tree Item. A wrapper class for components to exist in a 
 * Tree hierarchy (for embedding in a tree view for example)
 * 
 */

import java.util.ArrayList;
import java.util.List;

public class SimpleTreeItem {
	private List<SimpleTreeItem> children;
	private SimpleUIComponent component;
	private SimpleHierarchy master;
	
	//constructs a tree item from another component to attach
	public SimpleTreeItem(SimpleUIComponent comp) {
		component = comp;
		children = new ArrayList<SimpleTreeItem>();
	}
	//sets the position of the attached component
	public void setPosition(float x, float y) {
		component.setX(x);
		component.setY(y);
	}
	
	//Add sub items to this item, notify any
	//masters that we have changed ourselves
	public void addItem(SimpleTreeItem item) {
		children.add(item);
		if(master != null)
			master.notifyAdd(item);
	}
	
	//returns a list of child items
	public List<SimpleTreeItem> getChildren() {
		return children;
	}
	
	//returns the attached component
	public SimpleUIComponent getAttached() {
		return component;
	}
	
	//sets the active parent of this tree item
	public void setMaster(SimpleHierarchy master) {
		this.master = master;
	}
	
}
