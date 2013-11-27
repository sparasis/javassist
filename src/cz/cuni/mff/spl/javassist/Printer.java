package cz.cuni.mff.spl.javassist;

import java.util.Collection;
import java.util.List;

import cz.cuni.mff.spl.instrumentation.runtime.DataHolder;
import cz.cuni.mff.spl.instrumentation.runtime.DataStore;
import cz.cuni.mff.spl.instrumentation.runtime.Measurement;

public class Printer {

	public static void print(){
		Collection<DataHolder> datas = DataStore.getDatas();
		int count = 0;
		
		for(DataHolder data : datas){
			List<Measurement> values = data.getValues();
			count += values.size();
			/*
			System.out.println("Measurements for " + data.getName());
			for(Measurement measurement : values){
				System.out.println(measurement.toString());
			}/**/
		}

		System.out.println("\nNumber of measurements: " + count);
	}
	
}
