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

import java.io.IOException;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

/**
 * Instruments class method with measurement code. It use {@link DataStore}
 * for data storing. It runs instrumentation only for single method and loads
 * its class.
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
	 * @param writeFile <code>true</code> if class should be written to file
	 * 		<code>false</code> if class should be loaded after transformation
	 */
	public static void transform(String clazz, String method, String descriptor, boolean writeFile){

		ClassPool pool = ClassPool.getDefault();
		try {
			CtClass cc = pool.get(clazz);
			CtMethod cmethod = cc.getMethod(method, descriptor);
			String name = cz.cuni.mff.spl.instrumentation.runtime.DataStore.createDataName(clazz, method, descriptor);
			

			//cmethod.insertAfter("System.out.println(\"Done sorting.\");");
			
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
			
			if(writeFile){
				cc.writeFile("./generated");
			} else {
				cc.toClass();
			}
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CannotCompileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
