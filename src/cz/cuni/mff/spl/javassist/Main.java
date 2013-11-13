package cz.cuni.mff.spl.javassist;

import java.util.Collection;

import cz.cuni.mff.spl.instrumentation.runtime.DataHolder;
import cz.cuni.mff.spl.instrumentation.runtime.DataStore;
import cz.cuni.mff.spl.instrumentation.runtime.Measurement;

/**
 * Contains method to run instrumentation of sorting application.
 * 
 * @author Jaroslav Kotrc
 *
 */
public class Main {


	/**
	 * Runs instrumentation of sorting application and then runs the sorting itself
	 * with the same arguments as it obtained. 
	 * 
	 * @param args command line arguments passed to the sorting application
	 */
	public static void main(String[] args) {
		System.out.println("Instrumenting...");
		
		/*MeasureTransformer.transform("cz.cuni.mff.spl.sortapp.Main",
				"main", "([Ljava/lang/String;)V");
		*/
		
		MeasureTransformer.transform("cz.cuni.mff.spl.sortapp.Main",
				"run", "(Lcz/cuni/mff/spl/sortapp/Main$SortHolder;I)V");
		
		System.out.println("Done instrumenting, running application.");
		cz.cuni.mff.spl.sortapp.Main.main(args);
		System.out.println("Exiting.");
		
		Collection<DataHolder> datas = DataStore.getDatas();
		datas.size();
		
		for(DataHolder data : datas){
			System.out.println("Measurements for " + data.getName());
			for(Measurement measurement : data.getValues()){
				System.out.println(measurement.toString());
			}
		}
	}

}
