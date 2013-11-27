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

/** 
 * Holds information about single measurement. Includes times and
 * recursion depth.
 * 
 * @author Jaroslav Kotrc
 *
 */
public class Measurement {
	/** Time when the measurement starts. */
	private long start;
	/** Time when the measurement ends. */
	private long stop;
	/** Depth of the recursion call when the measured method runs. For first call it starts at 1. */
	private long recursion;
	/** ID of the thread where the measured method runs. */
	private long thread;
	
	public long getThread() {
		return thread;
	}
	public void setThread(long thread) {
		this.thread = thread;
	}
	public long getStart() {
		return start;
	}
	public void setStart(long start) {
		this.start = start;
	}
	public long getStop() {
		return stop;
	}
	public void setStop(long stop) {
		this.stop = stop;
	}
	public long getRecursion() {
		return recursion;
	}
	public void setRecursion(long recursion) {
		this.recursion = recursion;
	}
	@Override
	public String toString() {
		return "Measurement [start=" + start + ", stop=" + stop
				+ ", recursion=" + recursion + ", thread=" + thread + ", time= "+ (stop-start)  + "]";
	}
	
	
}
