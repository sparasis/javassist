package cz.cuni.mff.spl.javassist;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.Translator;

/**
 * Translator used for instrumenting class at load time. It runs instrumentation
 * for every class so only demanded class is chosen. 
 * 
 * @author Jaroslav Kotrc
 *
 */
public class EventTranslator implements Translator {
	private String clazz;
	private String method;
	private String descriptor;
	

	public EventTranslator(String clazz, String method, String descriptor) {
		super();
		this.clazz = clazz;
		this.method = method;
		this.descriptor = descriptor;
	}

	@Override
	public void start(ClassPool pool) throws NotFoundException,
			CannotCompileException {}

	@Override
	public void onLoad(ClassPool pool, String classname)
			throws NotFoundException, CannotCompileException {
		if(clazz.equals(classname)){
			CtClass cc = pool.get(clazz);
			CtMethod cmethod = cc.getMethod(method, descriptor);
			String name = cz.cuni.mff.spl.instrumentation.runtime.DataStore.createDataName(clazz, method, descriptor);
		
			
			cmethod.insertBefore("cz.cuni.mff.spl.instrumentation.runtime." +
					"DataStore.startMeasurement(\"" + name + "\");");
			cmethod.insertAfter("cz.cuni.mff.spl.instrumentation.runtime." +
					"DataStore.stopMeasurement(\"" + name + "\");");
			
		}

	}

}
