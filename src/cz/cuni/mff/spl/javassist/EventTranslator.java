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
