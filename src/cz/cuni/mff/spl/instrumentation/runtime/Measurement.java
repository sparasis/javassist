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
	/** Depth of the recursion call when the measured method runs. */
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
				+ ", recursion=" + recursion + ", thread=" + thread + ", time is= "+ (stop-start)  + "]";
	}
	
	
}
