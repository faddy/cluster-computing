package system;

import java.rmi.Remote;

public interface Computer2Space extends Remote {
	void register( Computer computer ) throws java.rmi.RemoteException;
}
