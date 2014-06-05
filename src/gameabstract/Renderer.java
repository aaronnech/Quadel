package gameabstract;
/*
 * Author: Aaron Nech
 * Project: SJGF
 * Description: Abstraction of something that can render
 * something that is renderable
 * 
 */
public interface Renderer {
	//called to render something renderable
	public void render(Renderable e);
	//called at the beginning of every render pass
	public void begin();
	//called at the end of every render pass
	public void end();
	//general notification method. Used to notify any renderer type
	public void notify(String eventName, Object msg);
}
