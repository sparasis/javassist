package cz.cuni.mff.spl.javassist;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

/**
 * Instruments class method with measurement code. It use {@link DataStore}
 * for data storing.
 * 
 * @author Jaroslav Kotrc
 *
 */
public class MeasureTransformer {
	
	/**
	 * Instruments single method of described class
	 * 
	 * @param clazz the class of instrumented method
	 * @param method the instrumented method
	 * @param descriptor method descriptor
	 */
	public static void transform(String clazz, String method, String descriptor){

		ClassPool pool = ClassPool.getDefault();
		try {
			CtClass cc = pool.get(clazz);
			//CtMethod[] cl = cc.getMethods();
			CtMethod cmethod = cc.getMethod(method, descriptor);
			String name = cz.cuni.mff.spl.instrumentation.runtime.DataStore.createDataName(clazz, method, descriptor);
			
			
			cmethod.insertBefore("cz.cuni.mff.spl.instrumentation.runtime." +
					"DataStore.startMeasurement(\"" + name + "\");");
			cmethod.insertAfter("cz.cuni.mff.spl.instrumentation.runtime." +
					"DataStore.stopMeasurement(\"" + name + "\");");

			/*
			cmethod.addLocalVariable("before", CtClass.longType);
			cmethod.insertBefore(
					"before = System.nanoTime();");
			cmethod.insertAfter(
					"{" +
					"long after = System.nanoTime();" +
					"System.out.println(\"Running time is\");" +
					"System.out.println(after-before);" +
					"}");
					
			 */
			
			cc.toClass();
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CannotCompileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
