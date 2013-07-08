package cz.cuni.mff.spl.instrumentation.runtime;

/** 
 * Exception thrown during measurement of running application.
 * 
 * @author Jaroslav Kotrc
 *
 */
public class MeasurementException extends Exception{

	private static final long serialVersionUID = -6513096950836280659L;

	public MeasurementException() {
		super();
	}

	public MeasurementException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public MeasurementException(String message, Throwable cause) {
		super(message, cause);
	}

	public MeasurementException(String message) {
		super(message);
	}

	public MeasurementException(Throwable cause) {
		super(cause);
	}

}
