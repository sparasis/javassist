package cz.cuni.mff.spl.instrumentation.runtime;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/** Holds data of measurement of single method. Also contains partial times
 * of measurements. For every started measurement the time is stored until
 * it ends and after that the difference of the end-start is stored.
 * 
 * It supports recursion and only single thread.
 * The time of method run is measured including all methods called in the 
 * measured method. If the same method is called in it then new measurement
 * starts but the first one ends after the program backtrack to the level of
 * first method. The measurements are enclosed like parenthesis.
 * 
 * @author Jaroslav Kotrc
 *
 */
public class DataHolder{
	
	private String name;
	
	/** 
	 * Creates new holder with specified name.
	 * @param name name of the holder
	 */
	public DataHolder(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	/** List of completed measurements. */
	private List<Measurement> values = new LinkedList<Measurement>();
	
	public void addValue(Measurement value){
		values.add(value);
	}
	
	public List<Measurement> getValues(){
		return values;
	}
	
	/** List of running measurements. */
	//private LinkedList<Measurement> starts = new LinkedList<Measurement>();
	
	/** Map where for every thread is stored one list of running measurements.
	 * The map corresponds with number of thread where the method runs
	 * and every list corresponds with recursive calls of the method in the same thread.*/
	private HashMap<Long, LinkedList<Measurement>> starts = new HashMap<Long, LinkedList<Measurement>>();

	/** 
	 * Starts next measurement and stores the time in the accumulator.
	 * The time of measurement start is the last command in the method to prevent
	 * measurement error.
	 * The searching for appropriate thread list is synchronized.
	 * */
	public void start(){
		Measurement measurement = new Measurement();
		long threadId = Thread.currentThread().getId();
		LinkedList<Measurement> threadStarts;
		synchronized(starts){
			threadStarts = starts.get(threadId);
			if(threadStarts == null){
				threadStarts = new LinkedList<Measurement>();
				starts.put(threadId, threadStarts);
			}
		}
		threadStarts.addFirst(measurement);
		measurement.setRecursion(starts.size());
		measurement.setThread(threadId);
		
		measurement.setStart(System.nanoTime());
	}
	
	/** 
	 * Stops measurement with passed time and adds it to the list of completed measurements.
	 * For better accuracy the time of measurement end should be obtained before this method
	 * is called and passed as an argument.
	 * Access to the running and completed measurements is synchronized.
	 * 
	 * @param time time in nanoseconds when the measurement stopped
	 * @throws MeasurementException thrown when list of measurement starts is not found for current thread
	 */
	public void stop(long time) throws MeasurementException{
		long threadId = Thread.currentThread().getId();
		LinkedList<Measurement> threadStarts;
		synchronized(starts){
			threadStarts = starts.get(threadId);
		}
		if(threadStarts == null){
			throw new MeasurementException("List of measurements was not found for thread " + threadId);
		}
		Measurement measurement = threadStarts.removeFirst();
		measurement.setStop(time);
		synchronized(values){
			values.add(measurement);
		}
	}
}