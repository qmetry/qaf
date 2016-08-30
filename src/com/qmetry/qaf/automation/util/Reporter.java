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


package com.qmetry.qaf.automation.util;

import static com.qmetry.qaf.automation.core.TestBaseProvider.instance;

import com.qmetry.qaf.automation.core.MessageTypes;

/**
 * This class provides utility methods to log message in report.
 * 
 * @author chirag
 */
public class Reporter {

	private Reporter() {

	}

	/**
	 * You can specify the message type with message. If message is with type
	 * {@link MessageTypes#Fail} the test case will get fail. When you want to
	 * create step within test case you can use {@link MessageTypes#TestStep}
	 * 
	 * @param msg
	 * @param type
	 */
	public static void log(String msg, MessageTypes type) {
		instance().get().addAssertionLog(msg, type);
	}

	// @QAFTestStep
	public static void log(String msg) {
		log(msg, MessageTypes.Info);
	}

	/**
	 * Capture screen shot and attach with message in report.
	 * 
	 * @param msg
	 * @param type
	 */
	public static void logWithScreenShot(String msg, MessageTypes type) {
		instance().get().addAssertionLogWithScreenShot(msg, type);
	}

	/**
	 * Capture screen shot and attach with message in report.
	 * 
	 * @param msg
	 */
	// @QAFTestStep
	public static void logWithScreenShot(String msg) {
		logWithScreenShot(msg, MessageTypes.Info);
	}

}
