package system;

import java.rmi.Remote;
import java.rmi.RemoteException;

import api.Result;
import api.Task;

/**
 * The interface that must be implemented by the computer   
 * 
 * @author fahad
 *
 */
public interface Computer extends Remote {

	/**
	 * @param <T>
	 * @param t the task object that needs to be executed on the computer (sent by the client)
	 * @return the type of argument returned by the task object's execute method
	 * @throws RemoteException thrown when unable to open sockets to listen to remote methods
	 */
	<T> Result<T> execute(Task<T> t) throws RemoteException;
}
