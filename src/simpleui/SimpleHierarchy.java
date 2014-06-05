package simpleui;
/*
 * Author: Aaron Nech
 * Project: SJGF
 * Description: Allows components to communicate in a standard way to classes that are hierarchy based.
 * For example, SimpleTreeView
 * 
 */
public interface SimpleHierarchy {
	//returns true if the add was accepted
	public boolean notifyAdd(SimpleTreeItem item);
}
