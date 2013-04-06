package api;

import java.rmi.Remote;

/**
 * The Space module. It holds the Task and Result objects. 
 * @author fahad
 *
 * @param <T>
 */
public interface Space<T> extends Remote {
	
	public static String SERVICE_NAME = "Space";
	    
	/**
	 * The remote method called by clients to put a task object in the space
	 * @param task
	 * @throws java.rmi.RemoteException
	 */
	void put( Task<T> task ) throws java.rmi.RemoteException;
	    
	/**
	 * The remote method called by clients to take a Result object from the space
	 * @return
	 * @throws java.rmi.RemoteException
	 */
	Result<T> takeResult() throws java.rmi.RemoteException;
}
