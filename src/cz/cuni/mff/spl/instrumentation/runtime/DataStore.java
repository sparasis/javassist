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

import java.util.Collection;
import java.util.HashMap;

/**
 * Singleton storage of measured data. Provides simple interface for measuring.
 * 
 * @author Jaroslav Kotrc
 *
 */
public class DataStore {
	/** Main storage of the data. Data are accessed by the name of the data. */
	private static HashMap<String, DataHolder> data = new HashMap<String, DataHolder>();

	/** 
	 * Returns {@link DataHolder} object for the passed name. It is created if it does not exist.
	 * The searching and creating is synchronized.
	 * 
	 * @param name name of the data
	 * @return object for storing data
	 */
	public static DataHolder getData(String name){
		DataHolder holder;
		synchronized(data){
			holder = data.get(name);
			if(holder == null){
				holder = new DataHolder(name);
				data.put(name, holder);
			}
		}
		return holder;
	}
	
	/**
	 * Starts measurement with specified name.
	 * 
	 * @param name name of the measurement
	 */
	public static void startMeasurement(String name){
		getData(name).start();
	}
	
	/**
	 * Stops measurement with the specified name. The time is obtained as a first command
	 * for better accuracy.
	 * 
	 * @param name name of the measurement.
	 */
	public static void stopMeasurement(String name){
		long time = System.nanoTime();
		
		DataHolder holder = data.get(name);
		if(holder == null){
			reportError("Start of the measurement " + name + " does not exist.");
		}
		try {
			holder.stop(time);
		} catch (MeasurementException e) {
			e.printStackTrace();
			reportError(e.getMessage());
		}
	}
	
	/** 
	 * Reports passed error
	 * 
	 * @param err error to report
	 */
	public static void reportError(String err){
		System.err.println(err);
	}
	
	/**
	 * Returns all measured data. It contains only measurements that have successfully ended.
	 * 
	 * @return measured data
	 */
	public static Collection<DataHolder> getDatas(){
		return data.values();
	}
	
	/**
	 * Creates data name unique for method. May be created during instrumentation not
	 * during run time of the application because it stay constant during run time.
	 * 
	 * @param clazz class of the method
	 * @param method name of the method
	 * @param descriptor method descriptor
	 * @return data name for specified method
	 */
	public static String createDataName(String clazz, String method, String descriptor){
		return clazz + '#' + method + '(' + descriptor + ')';
	}
}
