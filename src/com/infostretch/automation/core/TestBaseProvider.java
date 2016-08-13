/*******************************************************************************
 * QMetry Automation Framework provides a powerful and versatile platform to author 
 * Automated Test Cases in Behavior Driven, Keyword Driven or Code Driven approach
 *                
 * Copyright 2016 Infostretch Corporation
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT
 * OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE
 *
 * You should have received a copy of the GNU General Public License along with this program in the name of LICENSE.txt in the root folder of the distribution. If not, see https://opensource.org/licenses/gpl-3.0.html
 *
 * See the NOTICE.TXT file in root folder of this source files distribution 
 * for additional information regarding copyright ownership and licenses
 * of other open source software / files used by QMetry Automation Framework.
 *
 * For any inquiry or need additional information, please contact support-qaf@infostretch.com
 *******************************************************************************/


package com.infostretch.automation.core;

import java.util.Iterator;
import java.util.Vector;

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
			}
		});
	}

}
