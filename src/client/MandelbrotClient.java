package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import api.Result;
import api.Space;
import api.Task;

import system.Computer;
import tasks.MandelbrotSetTask;


public class MandelbrotClient {
	
	private static final int N_PIXELS = 1024;
	private static final int ITERATION_LIMIT = 512;
	private static final double X = -0.7510975859375;
	private static final double Y = 0.1315680625;
	private static final double edgeLength = 0.01611;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		long jobTime = System.currentTimeMillis();
		
		if(System.getSecurityManager()==null) System.setSecurityManager(new SecurityManager());
		String name = "Space";
		
		Registry registry;
		try {
			registry = LocateRegistry.getRegistry(args[0], Integer.parseInt(args[1]));
			
			Space<int[][]> space = (Space<int[][]>) registry.lookup(name);
			long[] taskTimeForClient = new long[16];
			
			int identifier=0;
			for (int j=0; j<4; j++){
				
				for(int i=0; i<4; i++){
										
					double eLength=edgeLength/4;
					int pixels = N_PIXELS/4;
					double x=(i*eLength)+X;
					double y=(j*eLength)+Y;
									
					Task<int[][]> mandelbrotSetTask = new MandelbrotSetTask(x, y, eLength, pixels, ITERATION_LIMIT , identifier);
					System.out.println("Putting task: " + mandelbrotSetTask.toString());
					
					taskTimeForClient[identifier]=System.currentTimeMillis();
					
					// put the task in the space
					space.put(mandelbrotSetTask);
					
					identifier++;
				}
				
			}
			
			Result[] resultsArray = new Result[16];
			long totalComputerTime = 0;
						
			for(int i=0;  i<16; i++){
				
				// wait to get result from the space
				Result<int[][]> r = space.takeResult();	
				
				taskTimeForClient[r.getIdentifier()] = System.currentTimeMillis() - taskTimeForClient[r.getIdentifier()];
				System.out.println("Computer time: " + r.getTimeElapsed());
				System.out.println("Taken result: "+r.getIdentifier() + " " + taskTimeForClient[r.getIdentifier()]);
				
				resultsArray[r.getIdentifier()] = r;
				totalComputerTime = totalComputerTime + r.getTimeElapsed();
			}
			
			
			// combine the results 
			identifier = 0;
			int[][] counts = new int[N_PIXELS][N_PIXELS];
			
			for (int j=0; j<4; j++){
				
				for (int i=0; i<4; i++){
					
					int[][] taskPixels = (int[][]) resultsArray[identifier].getT();
					
					for (int x=0; x<256; x++){
						for(int y=0; y<256; y++){
							counts[x+i*256][y+j*256] = taskPixels[x][y];
						}
					}
					identifier++;
				}
				
			}
			
			System.out.println("Average time to execute a task as seen by Computer: " + totalComputerTime/16.0);
			
			long totalTaskTimeForClient=0;
			for(int i=0; i<16; i++){
				totalTaskTimeForClient = totalTaskTimeForClient + taskTimeForClient[i];
			}
			System.out.println("Average time to execute a task as seen by Client: " + totalTaskTimeForClient/16.0);
			
		    jobTime = System.currentTimeMillis() - jobTime;
			System.out.println("Total time taken by the client (excluding display): " + jobTime);
			
			JLabel mandelbrotLabel = displayMandelbrotSetTaskReturnValue( counts );
	
		    // display JLabels: graphic images
		    JFrame frame = new JFrame( "Result Visualizations" );
		    frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		    java.awt.Container container = frame.getContentPane();
		    container.setLayout( new BorderLayout() );
		    container.add( new JScrollPane( mandelbrotLabel ), BorderLayout.WEST );
		    frame.pack();
		    frame.setVisible( true );
		    
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
//	private static Object runTask( Task<int[][]> task, Computer computer ) throws RemoteException
//	{
//		
//		ArrayList<Long> runTimes = new ArrayList<Long>();
//		Result<int[][]> result = new Result<int[][]>(null, 0, 0);
//		
//		// print task class name
//		System.out.println("Task class name: "+task.getClass());
//		
//		
//		// run task 5 times
//		for(int i=0; i<5; i++){
//			
//			long startTime = System.nanoTime();
//			
//			//result = (int[][]) task.execute();
//			result = computer.execute(task);
//
//			// collect/print run times	
//			long elapsedTime = System.nanoTime() - startTime;
//			System.out.println("time elapsed: "+ elapsedTime);
//			runTimes.add(elapsedTime);
//		}
//		
//		
//		// compute/print average time
//		long totalTime = 0;
//		for(long t : runTimes) {
//			System.out.println(t);
//			totalTime = t + totalTime;
//		}
//		System.out.println("Average time (ns): " + (totalTime/5.0));
//		
//		/* 
//		* return task.execute()
//		*/
//		System.out.println(result);
//		return result;
//	}
	
	private static JLabel displayMandelbrotSetTaskReturnValue( int[][] counts )
	{
	    Image image = new BufferedImage( N_PIXELS, N_PIXELS, BufferedImage.TYPE_INT_ARGB );
	    Graphics graphics = image.getGraphics();
	    for ( int i = 0; i < counts.length; i++ )
	    for ( int j = 0; j < counts.length; j++ )
	    {
	        graphics.setColor( getColor( counts[i][j] ) );
	        graphics.fillRect(i, j, 1, 1);
	    }
	    ImageIcon imageIcon = new ImageIcon( image );
	    return new JLabel( imageIcon );
	}
	
	private static Color getColor( int i )
	{
	    if ( i == ITERATION_LIMIT )
	        return Color.BLACK;
	    else {
	    	return new Color(i);
	    }
	    	
	}

}