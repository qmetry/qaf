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
package com.qmetry.qaf.automation.core;

import com.qmetry.qaf.automation.util.PropertyUtil;

/**
 * com.qmetry.qaf.automation.core.QAFConfigurationListener This listener can be
 * used to perform specific tasks after resource load or changed. Common
 * use-case is to load addition resources from class path or programmatically.
 * 
 * Listener can be registered using 'qaf.listeners' property or as service
 * 
 * @author chirag.jayswal
 */
public interface QAFConfigurationListener extends QAFListener {

	/**
	 * this method will be called after configuration loaded. Don't use
	 * {@link ConfigurationManager#getBundle()} inside this method implementation,
	 * if required, use bundle parameter of method.
	 * 
	 * @param bundle
	 */
	public void onLoad(PropertyUtil bundle);

	/**
	 * this method will be called when env.resource changed and configuration loaded
	 * with updated env.resource.
	 */
	public void onChange();

}
