package tasks;

import java.io.Serializable;

import api.Task;

/**
 * The task class which implements the brute force algorithm to solve a part of the traveling salesman problem  
 * @author Raviprakash
 *
 */
public class TspTask implements Task<int[]>,Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * the identifier of the task
	 */
	private int identifier;
	/**
	 * the array that contains the cities
	 */
	private double[][] cities;
	/**
	 * total cost of the path
	 */
	public double tsp_cost = 10000000;
	public String ans = "";
	/**
	 * the least cost path
	 */
	private int[] tour;
	/**
	 * Constructor for the TspTask 
	 * @param in_cities 2-D array of co-ordinates of cities
	 */
	public TspTask(double in_cities[][],int taskID)
	{
		cities = in_cities;
		identifier = taskID;
	}
		

	

	/**
	 * Swap v[i] and v[j]
	 * @param v
	 * @param i
	 * @param j
	 */
	public void swap(int v[],int i,int j)
	{	
		int t;
		t = v[i];
		v[i] = v[j];
		v[j] = t;
	}

	/**
	 * Shift the elements of the array v leftwards, moving the first element to the last. 
	 * @param v array
	 * @param start 
	 * @param n length
	 */
	public void rotateLeft(int v[], int start,int n)
	{	
		int tmp = v[start];
		for (int i = start; i < n-1; i++) {
		  v[i] = v[i+1];
		}
		v[n-1] = tmp;
	} 

	/**
	 * Finds the cost of a particular iteration
	 * @param inp current iteration in the form of a string
	 */
	public void do_cost(String inp)
	{
		String res[] = inp.split(" ");
		int iter_cities[] = new int[res.length];
		for(int i=0;i<res.length;i++)
			iter_cities[i] = Integer.parseInt(res[i]);
		double iter_cost = 0;
		for(int i=0;i<iter_cities.length-1;i++)
		{
			double dist = distance(cities[iter_cities[i]][0],cities[iter_cities[i]][1],cities[iter_cities[i+1]][0],cities[iter_cities[i+1]][1]);
			iter_cost +=dist;
		}
		if(iter_cost < tsp_cost)
		{
			tsp_cost = iter_cost;
			ans = inp;
		}
			
	}

	/**
	 * Generates permutations of the elements of v
	 * @param v input array
	 * @param start 
	 * @param n
	 */
	public void permute(int v[],int start, int n)
	{
	  String res= "";
	  for(int k=0;k<v.length;k++)
		res = res + v[k] + " ";          
	  do_cost(res);
	  if (start < n) {
	    int i, j;
	    for (i = n-2; i >= start; i--) {
	      for (j = i + 1; j < n; j++) {
		swap(v, i, j);
		permute(v, i+1, n);
	      } 
	      rotateLeft(v, i, n);
	    } 
	  }
	} 


	/**
	 * finds the distance between two cities expressed by their x and y co-ordinates
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return the distance
	 */
	public double distance (double x1,double y1,double x2, double y2){
		return Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1));		
	}

	
	/* (non-Javadoc)
	 * @see api.Task#execute()
	 */
	public int[] execute() {		
		
		if (cities==null)
			System.out.println("this is null");
		int len = cities.length;
		
		
//		String res = "";
//		double total_cost = 10000, iter_cost =0;
   		int v[] = new int[len];
		for(int i=0;i<len;i++)
			v[i]=i;
		
		permute(v,1,len);
		System.out.println(tsp_cost + " " + ans);
		String res[] = ans.split(" ");
		int tour[] = new int[res.length];
		for (int i=0;i<len;i++)
			tour[i]= Integer.parseInt(res[i]);
		this.setTour(tour);
		return tour;
	}

	public void setTour(int[] tour) {
		this.tour = tour;
	}

	public int[] getTour() {
		return tour;
	}




	public int getIdentifier() {
		// TODO Auto-generated method stub
		return identifier;
	}

}