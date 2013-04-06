package system;


import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


import api.Result;
import api.Task;


public class ComputerImpl extends UnicastRemoteObject implements Computer {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ComputerImpl() throws RemoteException {
		super();
	}

	public <T> Result<T> execute(Task<T> t) throws RemoteException {
		
		long startTime = System.currentTimeMillis();		
		T result = t.execute();
		long elapsedTime = System.currentTimeMillis()-startTime;
		return new Result<T>(result, elapsedTime,t.getIdentifier());		
	}
	
	public static void main(String[] args){
		
		
		if(System.getSecurityManager()==null) System.setSecurityManager(new RMISecurityManager());
		
		try {
			
//			String name = "Computer";
//			Computer computerStub = (Computer) UnicastRemoteObject.exportObject(computer, 0); // create reference stub to the computerImpl object			
//			Naming.rebind("//"+args[0]+":"+args[1]+"/"+name,computerStub);			 // bind the computerStub to the registry
			
			Computer computer = new ComputerImpl();
					
			Registry registry = LocateRegistry.getRegistry(args[0], Integer.parseInt(args[1])); //find registry running on the host mentioned
					
			System.out.println("ComputerImpl bound!");
			
			Computer2Space computer2Space = (Computer2Space) registry.lookup("Space");
			System.out.println("computer is going to register");
			computer2Space.register(computer);
			
		} catch (RemoteException e) {
			e.printStackTrace();
			System.err.println("ComputerImpl remote exception");
		} catch (NotBoundException e) {
			e.printStackTrace();
			System.err.println("ComputerImpl not bound exception");
		}
	}

}