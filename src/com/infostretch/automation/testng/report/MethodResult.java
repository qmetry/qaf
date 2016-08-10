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


package com.infostretch.automation.testng.report;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import com.infostretch.automation.core.CheckpointResultBean;
import com.infostretch.automation.core.LoggingBean;

/**
 * { "name":"m1", "groups":"", "desc":"", "doc":"", "startTime":1, "endTime":1,
 * "result":"pass", "seleniumLog":"" }
 * com.infostretch.automation.testng.report.Method.java
 * 
 * @author chirag.jayswal
 */
public class MethodResult {

	private List<LoggingBean> seleniumLog;
	private List<CheckpointResultBean> checkPoints;
	private String errorTrace;

	/**
	 * @return the seleniumLog
	 */
	public List<LoggingBean> getSeleniumLog() {
		return seleniumLog;
	}

	/**
	 * @param list
	 *            the seleniumLog to set
	 */
	public void setSeleniumLog(List<LoggingBean> list) {
		seleniumLog = list;
	}

	/**
	 * @return the checkPoints
	 */
	public List<CheckpointResultBean> getCheckPoints() {
		return checkPoints;
	}

	/**
	 * @param list
	 *            the checkPoints to set
	 */
	public void setCheckPoints(List<CheckpointResultBean> list) {
		checkPoints = list;
	}

	/**
	 * @return the errorTrace
	 */
	public String getErrorTrace() {
		return errorTrace;
	}

	/**
	 * @param errorTrace
	 *            the errorTrace to set
	 */
	public void setErrorTrace(String errorTrace) {
		this.errorTrace = errorTrace;
	}

	/**
	 * @param throwable
	 *            from which the errorTrace to set
	 */
	public void setThrowable(Throwable throwable) {
		if (null == throwable)
			return;
		ByteArrayOutputStream sw = new ByteArrayOutputStream();
		PrintStream pw = new PrintStream(sw, true);
		throwable.printStackTrace(pw);
		setErrorTrace(new String(sw.toByteArray()));
	}
}
