package api;

/**
 * All tasks must implement this interface. The Computer's execute method takes arguments of Task type.
 * @author fahad
 * 
 * @param <T> the type of object the Task object's execute method returns
 */
public interface Task<T> {
	
	
	T execute();
	int getIdentifier();	
}
