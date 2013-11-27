/*
 * Copyright (c) 2013, Jaroslav Kotrc
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the author nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF 
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING 
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */
package cz.cuni.mff.spl.instrumentation.runtime;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/** 
 * Holds data of measurement of a single method. Also contains partial times
 * of measurements. For every started measurement the time is stored until
 * it ends and after that the difference of the end-start is stored.
 * 
 * It supports recursion and multiple threads.
 * The time of method run is measured including all methods called in the 
 * measured method. If the same method is called in it then new measurement
 * starts but the first one ends after the program backtrack to the level of
 * first method. The measurements are enclosed like parenthesis.
 * 
 * @author Jaroslav Kotrc
 *
 * TODO review synchronization, is all really needed?
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
		measurement.setRecursion(threadStarts.size());
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