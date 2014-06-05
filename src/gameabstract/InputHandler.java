package gameabstract;
/*
 * Author: Aaron Nech
 * Project: SJGF
 * Description: An input handler (usually a GameState) will handle input events that are processed by a GameInput object
 * 
 */
public interface InputHandler {
	public void input(InputEvent e);
}
