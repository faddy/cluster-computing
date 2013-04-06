package system;

import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.LinkedBlockingQueue;

import api.Result;
import api.Space;
import api.Task;

class ComputerProxy<T> implements Runnable {

	private Computer computer;
	private LinkedBlockingQueue<Result<T>> resultQueue;
	private LinkedBlockingQueue<Task<T>> taskQueue;
		 
	public ComputerProxy(Computer computer, LinkedBlockingQueue<Task<T>> taskQueue, LinkedBlockingQueue<Result<T>> resultQueue) {
		this.computer = computer;
		this.taskQueue = taskQueue;
		this.resultQueue = resultQueue;
	}

	public void run() {
		
		while(true){
			Task<T> t = null;
			try {
				t = taskQueue.take();				
				Result<T> r = computer.execute(t);
				resultQueue.put(r);
				
			} catch (Exception e) {
				//e.printStackTrace();
				System.err.println("Exception type" + e.toString());
				taskQueue.add(t);
				break;
			} 
		}
		
	}
	
};


public class SpaceImpl<T> extends UnicastRemoteObject implements Space<T>, Computer2Space {
	
	private static final long serialVersionUID = 1L;
	
	private LinkedBlockingQueue<Task<T>> taskQ;
	private LinkedBlockingQueue<Result<T>> resultQ;
	
	public SpaceImpl() throws RemoteException {
		super();
		taskQ = new LinkedBlockingQueue<Task<T>>();
		resultQ = new LinkedBlockingQueue<Result<T>>();
	}

	
	public void put(Task<T> task) throws RemoteException {
		// TODO Auto-generated method stub
		
		try {
			taskQ.put(task);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Interrupt Exception in put()");
		}
		
	}

	
	public Result<T> takeResult() throws RemoteException {
		// TODO Auto-generated method stub
		Result<T> r = null;
		try {
			r = resultQ.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.err.println("Interrupt Exception in takeResult()");
		} 
		
		return r; 
	}

	
	public void register(Computer computer) {	
				
		try {
			System.out.println("this is the register function");
			Runnable computerProxy = new ComputerProxy<T>(computer, taskQ, resultQ);
			System.out.println("A new thread is going to start now");
			Thread thread = new Thread(computerProxy);
			thread.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Exception in register:"+e.toString());
		}
		
	}
	
	public static void main(String args[]){
		
		if(System.getSecurityManager()==null) System.setSecurityManager(new RMISecurityManager());
				
		try {
//			Space<Object> spaceStub = (Space<Object>) UnicastRemoteObject.exportObject(space, 0);
			Space<Object> spaceStub  = new SpaceImpl<Object>();
			Registry registry = LocateRegistry.createRegistry(Integer.parseInt(args[0]));
			registry.rebind(SERVICE_NAME, spaceStub);
			System.out.println("SpaceImpl bound!");
			
		} catch (RemoteException e) {
			e.printStackTrace();
			System.out.println("SpaceImpl exception!!");
		}
		
	}

}