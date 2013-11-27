package cz.cuni.mff.spl.javassist;

/**
 * Starts the sorting application and prints measured data after
 * it ends.
 * 
 * @author Jaroslav Kotrc
 *
 */
public class Starter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Running application...");
		cz.cuni.mff.spl.sortapp.Main.main(args);
		System.out.println("Exiting.");
		
		Printer.print();

	}

}
