package simpleui;

/*
 * Author: Aaron Nech
 * Project: SJGF
 * Description: A subscription object. Connects events to methods of listeners
 * through reflection
 * 
 */
import java.lang.reflect.Method;

public class SimpleSubscription {
	public Method method;
	public SimpleEventType action;
	public Object subscriber;
	
	public SimpleSubscription(Method m, SimpleEventType a, Object s) {
		action = a;
		method = m;
		subscriber = s;
	}
}
