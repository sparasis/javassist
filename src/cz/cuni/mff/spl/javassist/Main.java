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
package cz.cuni.mff.spl.javassist;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.Loader;
import javassist.NotFoundException;
import javassist.Translator;

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
		//transform(args);
		//eventTransform(args);
		staticTransform();
	}
	
	/**
	 * Transforms classes and loads them then runs application.
	 * 
	 * @param args command line arguments
	 */
	private static void transform(String[] args){
		// try instrumenting user created class
		//*
		MeasureTransformer.transform("cz.cuni.mff.spl.sortapp.Main",
				"run", "(Lcz/cuni/mff/spl/sortapp/Main$SortHolder;I)V", false);
		/**/

		// try instrumenting java.util class
		// does not work: Prohibited package name: java.util
		/*
		MeasureTransformer.transform("java.util.Arrays",
				"sort", "([I)V", false);
		/**/
		
		System.out.println("Done instrumenting, running application.");
		cz.cuni.mff.spl.sortapp.Main.main(args);
		System.out.println("Exiting.");
		
		Printer.print();
	}

	
	/**
	 * Transforms classes at their load time and runs application by own Loader.
	 * The instrumented application is loaded by class loader that is different
	 * from the loader of this application so the measured data can be accessed 
	 * by different class loaded by the same class loader as the instrumented
	 * application. 
	 * 
	 * @param args command line arguments
	 */
	private static void eventTransform(String[] args){
		Loader cl = new Loader();
		
		// try instrumenting user created class
		//*
		Translator t = new EventTranslator("cz.cuni.mff.spl.sortapp.Main",
				"run", "(Lcz/cuni/mff/spl/sortapp/Main$SortHolder;I)V");
		/**/
		
		// try instrumenting user created class, instrumented method is called many times
		/*
		Translator t = new EventTranslator("cz.cuni.mff.spl.sortapp.QuickSort",
				"sort", "([I)V");
		/**/
		
		// try instrumenting java.util class, this class is not loaded by javassist
		// loader. By default it is passed to its parent class loader so this 
		// delegation is forbidden.
		// Does not work... Prohibited package name: java.lang
		/*
		Translator t = new EventTranslator("java.util.Arrays",
				"sort", "([I)V");
		cl.doDelegation = false;
		 /**/
		
		ClassPool pool = ClassPool.getDefault();
		try {
			cl.addTranslator(pool, t);
			System.out.println("Translator added, running application.");
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CannotCompileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			cl.run("cz.cuni.mff.spl.sortapp.Main", args);
			System.out.println("Exiting.");

			Class dataHolder = cl.loadClass("cz.cuni.mff.spl.javassist.Printer");
			dataHolder.getDeclaredMethod("print", new Class[] {}).invoke(null, new Class[] {});
			
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Statically transforms classes. Should be used for java.* classes that
	 * can not be transformed directly.
	 * Does not work when code using own classes is inserted, those classes are not
	 * found during run.
	 * 
	 * @param args command line arguments
	 */
	private static void staticTransform(){
		// try instrumenting user created class
		// does not work because when running the sorting application inner
		// class is not found
		/*
		MeasureTransformer.transform("cz.cuni.mff.spl.sortapp.Main",
				"run", "(Lcz/cuni/mff/spl/sortapp/Main$SortHolder;I)V", true);
		/**/
		
		// try instrumenting user created class, instrumented method is called many times
		// does not work, DataStore class not found
		/*
		MeasureTransformer.transform("cz.cuni.mff.spl.sortapp.QuickSort",
				"sort", "([I)V", true);
		/**/

		// try instrumenting java.util class
		// does not work, DataStore class not found
		/*
		MeasureTransformer.transform("java.util.Arrays",
				"sort", "([I)V", true);
		/**/
		System.out.println("Done instrumenting, exiting.");
	}

}
