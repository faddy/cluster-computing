package api;

import java.io.Serializable;

/**
 * The Result object holds the result of the execution of the task object
 * @author fahad
 *
 * @param <T>
 */
public class Result<T> implements Serializable {
	/**
	 * the identifier of the result object
	 */
	private final int identifier;
	/**
	 * the result of the execution of the task
	 */
	private final T t;
	/**
	 * time taken by computer to run this task
	 */
	private final long timeElapsed;
	
	public Result(T t, long timE, int ident) {
		this.t=t;
		this.timeElapsed = timE;
		this.identifier = ident;
	}

	/**
	 * @return the identifier of the result object
	 */
	public int getIdentifier() {
		return identifier;
	}

	/**
	 * @return the data stored in the result object
	 */
	public T getT() {
		return t;
	}

	/**
	 * @return the time it took for the computer (worker) to execute this task
	 */
	public long getTimeElapsed() {
		return timeElapsed;
	}
	
	
}
