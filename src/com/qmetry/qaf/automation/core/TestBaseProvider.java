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

import java.util.Iterator;
import java.util.Vector;

import com.qmetry.qaf.automation.integration.ResultUpdator;
import com.qmetry.qaf.automation.ui.webdriver.ChromeDriverHelper;

/**
 * This class provides thread-local {@link #QAFTestBase}.
 * 
 * @author chirag.jayswal
 */
public class TestBaseProvider extends ThreadLocal<QAFTestBase> {
	private final Vector<QAFTestBase> lst = new Vector<QAFTestBase>();

	@Override
	protected QAFTestBase initialValue() {
		QAFTestBase stb = new QAFTestBase();
		lst.add(stb);
		return stb;
	}

	@Override
	public void remove() {
		get().tearDown();
		super.remove();
	}

	@Override
	public void set(QAFTestBase value) {
		if (null == value) {
			remove();
		} else {
			super.set(value);
		}
	}

	public Vector<QAFTestBase> getAll() {
		return lst;
	}

	public void stopAll() {
		Iterator<QAFTestBase> iter = lst.iterator();
		while (iter.hasNext()) {
			iter.next().tearDown();
			iter.remove();
		}
	}

	public void prepareForShutDown() {
		Iterator<QAFTestBase> iter = lst.iterator();
		while (iter.hasNext()) {
			System.out.println("Preparing For Shut Down...");

			iter.next().setPrepareForShutdown(true);
		}
	}

	private static final TestBaseProvider INSTANCE = new TestBaseProvider();

	public static TestBaseProvider instance() {
		return INSTANCE;
	}

	private TestBaseProvider() {
	}

	static {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				TestBaseProvider.instance().prepareForShutDown();
				TestBaseProvider.instance().stopAll();
				ChromeDriverHelper.teardownService();
				ResultUpdator.awaitTermination();
			}
		});
	}

}
