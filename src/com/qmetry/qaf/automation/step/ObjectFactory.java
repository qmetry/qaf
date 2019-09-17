/*******************************************************************************
 * Copyright (c) 2019 Infostretch Corporation
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.qmetry.qaf.automation.step;

/**
 * Interface to be implemented for custom implementation of test step provider class object factory. To use your custom implementation set as below:
 * <p>
 * <code>ObjectFactory.INSTANCE.setFactory(myFactory) </code>
 * @since 2.1.15
 * @author chirag.jayswal
 *
 */
public interface ObjectFactory {
	public static final Provider INSTANCE = new Provider();
	public <T> T getObject(Class<T> cls) throws Exception;
	
	public static class Provider{
		private ObjectFactory factory;
		
		private Provider() {
			factory = new DefaultObjectFactory();
		}
		
		public void setFactory(ObjectFactory factory){
			this.factory= factory;
		}
		
		public <T> T getObject(Class<T> cls) throws Exception{
			return factory.getObject(cls);
		}
	}
	
}
