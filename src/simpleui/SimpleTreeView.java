package simpleui;
/*
 * Author: Aaron Nech
 * Project: SJGF
 * Description: Simple Tree View. This is a generic tree component for other components.
 * You can nest other components inside, and it'll automagically update positioning information
 * based on the SimpleTreeItem hierarchy
 * 
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import gameabstract.InputEvent;

public class SimpleTreeView extends SimpleUIComponent implements SimpleHierarchy {
	private List<SimpleTreeItem> items;
	private List<SimpleUIComponent> allChildren;
	private int spacing;
	private int indentation;
	
	//Constructs a button from a given name, location, and size
	public SimpleTreeView(String n, float x, float y, float w) {
		super(n, x, y, w, 0);
		items = new ArrayList<SimpleTreeItem>();
		allChildren = new ArrayList<SimpleUIComponent>();
		spacing = 5;
		indentation = 10;
	}
	
	//returns a flat list of all children components (even recursively)
	public List<SimpleUIComponent> allComponents() {
		return children();
	}
	
	//sets the padding between each list item vertically
	public void setSpacing(int space) {
		spacing = space;
		adjustPositions();
	}
	
	//sets the indentation on the x axis for recursive depth of list items
	public void setIndentation(int value) {
		indentation = value;
		adjustPositions();
	}
	
	//adds an item to the tree view
	public void addItem(SimpleTreeItem item) {
		items.add(item);
		allChildren.add(item.getAttached());
		item.setMaster(this);
		adjustPositions();
	}
	
	//removes a specific item from the tree
	public void removeComponent(SimpleTreeItem item) {
		items.remove(item);
		recursiveRemoveChildren(item);
		adjustPositions();
	}
	
	//removes a item by name from the tree
	public void removeComponent(String name) {
		Iterator<SimpleTreeItem> i = items.iterator();
		while(i.hasNext()) {
			SimpleTreeItem item = i.next();
			if(item.getAttached().getName().equals(name)) {
				i.remove();
				recursiveRemoveChildren(item);
			}
		}
		adjustPositions();
	}
	
	//clears all components from the manager
	public void clearItems() {
		items.clear();
		allChildren.clear();
	}
	
	//called by the UI manager when a input event is detected
	//touch events from tree views contain (if clicked) the item clicked as a message
	//with regards to it's flat list of children
	public boolean react(InputEvent e) {
		if(e.getType().equals("tap") && 
				inBoundingBox(((int[]) e.getData())[0], ((int[]) e.getData())[1])) {
			checkForClicks(((int[]) e.getData())[0], ((int[]) e.getData())[1]);
			SimpleEvent event = new SimpleEvent(this, SimpleEventType.TOUCH, getMessage());
			fireEvent(event);
			return true;
		}
		return false;
	}
	
	//appends a message that we clicked a specific child area if needed
	private void checkForClicks(int x, int y) {
		for(SimpleUIComponent child : allChildren) {
			if(child.inBoundingBox(x, y)) {
				setMessage(child);
				break;
			}
		}
	}

	//adjusts the position of the items in the list
	private void adjustPositions() {
		float currentY = y;
		for(SimpleTreeItem item : items) {
			currentY += recursivelyPosition(item, x, currentY) + spacing;
		}
		currentY -= spacing;
		setHeight(currentY - y);
	}
	
	//positions list elements recursively. Returns the collective height of elements positioned
	private float recursivelyPosition(SimpleTreeItem item, float xPosition, float yPosition) {
		float currentHeight = item.getAttached().getHeight();
		if(getWidth() < item.getAttached().getWidth())
			setWidth(item.getAttached().getWidth());
		item.setPosition(xPosition, yPosition);
		//System.out.println("ITEM : " + item.getAttached().getName() + " at " + item.getAttached().getX() + "," + item.getAttached().getY());
		for(SimpleTreeItem child : item.getChildren()) {
			currentHeight += recursivelyPosition(child, xPosition + indentation, yPosition + currentHeight) + spacing;
		}
		return currentHeight;
	}
	
	//removes children from the flat list recursively given an item
	private void recursiveRemoveChildren(SimpleTreeItem item) {
		allChildren.remove((item.getAttached()));
		for(SimpleTreeItem child : item.getChildren())
			recursiveRemoveChildren(child);
	}

	//gets all children components of the view
	protected List<SimpleUIComponent> children() {
		return allChildren;
	}

	//called by list items when they are added to
	//we use this hook to maintain our flat list
	public boolean notifyAdd(SimpleTreeItem member) {
		allChildren.add(member.getAttached());
		member.setMaster(this);
		adjustPositions();
		return true;
	}
}
