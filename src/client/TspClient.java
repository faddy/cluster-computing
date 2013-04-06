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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import api.Result;
import api.Space;
import api.Task;

import tasks.TspTask;

public class TspClient {
	
	private static final int N_PIXELS = 1024;	
//	private static final int ITERATION_LIMIT = 1000;

public static void main(String[] args) {
	
	long jobTime = System.currentTimeMillis();
	
	if(System.getSecurityManager()==null) System.setSecurityManager(new SecurityManager());
	String name = "Space";
	
	Registry registry;
	
	try{
	    registry = LocateRegistry.getRegistry(args[0], Integer.parseInt(args[1]));		
	    Space<int[]> space = (Space<int[]>)registry.lookup(name);
	
	    double cities[][] = { {6,3},{2,2},{5,8},{1,5},{1,6},{2,7},{2,8},{6,5},{1,3},{6,6} };
	    //double cities[][] = { {1,1},{8,1},{8,8},{1,8},{2,2},{7,2},{7,7},{2,7},{3,3},{6,3},{6,6},{3,6} };
	    //ArrayList<double [][]>  cities_collection = new ArrayList<double[][]>(9);
	    double cities_collection[][][] = new double[cities.length-1][][];
	    HashMap<Integer,double[][]> cities_hash = new HashMap<Integer,double[][]>();
	    HashMap<Integer,int[]> tours_hash = new HashMap<Integer,int[]>();
	    int tours[][] = new int[cities.length-1][];
	    
	    long[] taskTimeForClient = new long[cities.length];	    
	    // create sub tasks
	    for(int i=1;i<cities.length;i++){
		    double [][] iter_cities = cities;
		    double [] temp = iter_cities[0];
		    iter_cities[0] = iter_cities[i];
		    iter_cities[i] = temp;
		    Task<int[]> euclideanTspTask = new TspTask(iter_cities,i);
		    cities_hash.put(i, iter_cities);
		    taskTimeForClient[i] = System.currentTimeMillis();
		    space.put(euclideanTspTask);		    
	    }
	    
	    long totalComputerTime = 0;
	    for(int i=1;i<cities.length;i++){
		    Result<int[]> r = space.takeResult();
		    taskTimeForClient[r.getIdentifier()] = System.currentTimeMillis() - taskTimeForClient[r.getIdentifier()];
		    System.out.println("Computer time: " + r.getTimeElapsed());		    
		    int id = r.getIdentifier();
		    System.out.println("client received: " + id + " " + taskTimeForClient[id]);
		    int[] res = r.getT();
		    tours_hash.put(id, res);
		    totalComputerTime = totalComputerTime + r.getTimeElapsed();
	    }
	    
	    System.out.println("Average time to execute a task as seen by Computer: " + totalComputerTime/(cities.length-1));
	    
		long totalTaskTimeForClient=0;
		for(int i=1; i<cities.length; i++){
			totalTaskTimeForClient = totalTaskTimeForClient + taskTimeForClient[i];
		}
		System.out.println("Average time to execute a task as seen by Client: " + totalTaskTimeForClient/(cities.length-1));
		
	    
	    int tourId = getCheapestTour(tours_hash,cities_hash);	
	    
	    jobTime = System.currentTimeMillis() - jobTime;
		System.out.println("Total time taken by the client (excluding display): " + jobTime);
		
            JLabel euclideanTspLabel = displayEuclideanTspTaskReturnValue( cities_hash.get(tourId), tours_hash.get(tourId) );
                
            // display JLabels: graphic images
            JFrame frame = new JFrame( "Result Visualizations" );
            frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
            java.awt.Container container = frame.getContentPane();
            container.setLayout( new BorderLayout() );
            container.add( new JScrollPane( euclideanTspLabel ), BorderLayout.EAST );
            frame.pack();
            frame.setVisible( true );
	}catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	}


private static double distance (double x1,double y1,double x2, double y2){
	return Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1));		
}

private static int getCheapestTour(HashMap<Integer,int[]> tours_hash,HashMap<Integer,double[][]> cities_hash){
	
	
	double least_cost = 10000000; 
	// Create a Set with the keys in the HashMap.
        Set set = tours_hash.keySet();
        // Iterate over the Set to see what it contains.
        Iterator iter = set.iterator();
        int cheapest_tour = 0;
        while (iter.hasNext())
        {
        	Object o = iter.next();
        	int key = Integer.parseInt(o.toString());        	
		int [] iter_tour = tours_hash.get(key);
		double iter_cost = 0;		
		double [][] cities = cities_hash.get(key);
		for(int i=0;i<iter_tour.length-1;i++)
		{
			double dist = distance(cities[iter_tour[i]][0],cities[iter_tour[i]][1],cities[iter_tour[i+1]][0],cities[iter_tour[i+1]][1]);
			iter_cost +=dist;
		}
		if(iter_cost <= least_cost)
		{
			least_cost = iter_cost;
			cheapest_tour = key;	
		}
		
	}
	return cheapest_tour;
	
}

//private stat
	
	private static JLabel displayEuclideanTspTaskReturnValue( double[][] cities, int[] tour ) throws InterruptedException
	{
	    System.out.print( "Tour: ");
	    for ( int city: tour )
	    {
	        System.out.print( city + " ");
	    }
	    System.out.println("");

	    // ** display the graph
	    // get minX, maxX, minY, maxY, assume: 0.0 <= mins
	    double minX = cities[0][0], maxX = cities[0][0];
	    double minY = cities[0][1], maxY = cities[0][1];
	    for ( int i = 0; i < cities.length; i++ )
	    {
	        if ( cities[i][0] < minX ) minX = cities[i][0];
	        if ( cities[i][0] > maxX ) maxX = cities[i][0];
	        if ( cities[i][1] < minY ) minY = cities[i][1];
	        if ( cities[i][1] > maxY ) maxY = cities[i][1];
	    }
		
	    // scale points to fit in unit square
	    double side = Math.max( maxX - minX, maxY - minY );
	    double[][] scaledCities = new double[cities.length][2];
	    for ( int i = 0; i < cities.length; i++ )
	    {
	        scaledCities[i][0] = ( cities[i][0] - minX ) / side;
	        scaledCities[i][1] = ( cities[i][1] - minY ) / side;
	    }

	    Image image = new BufferedImage( N_PIXELS, N_PIXELS, BufferedImage.TYPE_INT_ARGB );
	    Graphics graphics = image.getGraphics();

	    int margin = 10;
	    int field = N_PIXELS - 2*margin;
	    // draw edges
	    graphics.setColor( Color.BLUE );
	    int x1, y1, x2, y2;
	    int city1 = tour[0], city2;
	    x1 = margin + (int) ( scaledCities[city1][0]*field );
	    y1 = margin + (int) ( scaledCities[city1][1]*field );
	    for ( int i = 1; i < cities.length; i++ )
	    {
	        city2 = tour[i];
	        x2 = margin + (int) ( scaledCities[city2][0]*field );
	        y2 = margin + (int) ( scaledCities[city2][1]*field );
	        graphics.drawLine( x1, y1, x2, y2 );       
	        x1 = x2;
	        y1 = y2;
	    }
	    city2 = tour[0];
	    x2 = margin + (int) ( scaledCities[city2][0]*field );
	    y2 = margin + (int) ( scaledCities[city2][1]*field );
	    graphics.drawLine( x1, y1, x2, y2 );

	    // draw vertices
	    int VERTEX_DIAMETER = 6;
	    graphics.setColor( Color.RED );
	    for ( int i = 0; i < cities.length; i++ )
	    {
		int x = margin + (int) ( scaledCities[i][0]*field );
	        int y = margin + (int) ( scaledCities[i][1]*field );
	        graphics.fillOval( x - VERTEX_DIAMETER/2,
	                           y - VERTEX_DIAMETER/2,
	                          VERTEX_DIAMETER, VERTEX_DIAMETER);
	    }
	    ImageIcon imageIcon = new ImageIcon( image );
	    return new JLabel( imageIcon );
	}

}