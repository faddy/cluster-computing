package tasks;

import java.io.Serializable;

import api.Task;


/**
 * The task class which constructs a part of MandelbrotSet
 * @author fahad
 *
 */
public final class MandelbrotSetTask implements Task<int[][]>, Serializable {

	/**
	 * 
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * the X co-ordinate the lower left pixel
	 */
	private double X;
	/**
	 * the Y co-ordinate the lower left pixel
	 */
	private double Y;
	/**
	 * the length of the edge of the square image 
	 */
	private double edgeLength;
	/**
	 * the number of pixels in the picture = n x n
	 */
	private int n;  
	/**
	 * The number of time the algorithm must be iterated to determine if the point is in the Mandelbrot set or not
	 */
	private int iterationLimit;
	/**
	 * Identifier of the object
	 */
	private int identifier;
	/**
	 * Constructor for the MandelbrotSet class 
	 * @param x the x co-ordinate of the left and lower most pixel of the picture
	 * @param y the x co-ordinate of the left and lower most pixel of the picture
	 * @param edgeLength the length of the picture
	 * @param n number of pixels in the picture = n x n
	 * @param iterationLimit the max number of times the polynomial 'zn+1 = zn2 + c' is iterated (finer result)
	 * @param identifier 
	 */
	public MandelbrotSetTask(double x, double y, double edgeLength, int n, int iterationLimit, int identifier) {
		super();
		this.X = x; 
		this.Y = y; 
		this.edgeLength = edgeLength;
		this.n = n;
		this.iterationLimit = iterationLimit;
		this.identifier = identifier;
	}
	
	@Override
	public String toString() {
		return "MandelbrotSetTask [X=" + X + ", Y=" + Y + ", edgeLength="
				+ edgeLength + ", identifier=" + identifier + "]";
	}

	/* (non-Javadoc)
	 * @see api.Task#execute()
	 */
	public int[][] execute() {
		
		double x=0, y=0;
		
		int[][] result = new int[n][n];
		
		for(int i=0;i<n;i++){
		
			for(int j=0; j<n; j++){
				
				x = (((i*edgeLength)/n)+X);
				y = (((j*edgeLength)/n)+Y);
				result[i][j] = computeIteration(x, y);
				
				System.out.print(result[i][j] + " ");
			}
			
			System.out.println("\n");
		}
		
		return result;
	}
	
	/**
	 * calculates the value of the polynomial 'zn+1 = zn2 + c' for each pixel i.e. find out if the pixel point is part of the mandelbrotset or not
	 * @param x0 the scaled x co-ordinate of the pixel
	 * @param y0 the scaled y co-ordinate of the pixel
	 * @return returns the number of times the polynomial was iterated (calculated). This value is used to set the color of the particular pixel
	 */
	public int computeIteration(double x0, double y0){
		
		double x=0, y=0; 
		int iteration=0;
		
		while( ((x*x + y*y)<=(2*2)) && iteration<iterationLimit ){
			
			double xtemp =  x*x - y*y + x0;
			y = 2*x*y + y0;
			x = xtemp;
			iteration++;
		}
//		System.out.println("it is: " + it);
		return iteration;
	}

	public int getIdentifier() {
		// TODO Auto-generated method stub
		return identifier;
	}

}



//// dummy execution to see if things work
//System.out.println("yes I have run!!!");
