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

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * { "reports":[{"dir":"test_result_1","status":"pass","tests":["test1"]},{"dir"
 * : "test_result_2","status":"fail"}], "total": 2, "pass": 1, "fail": 1,
 * "skip": 0, "start-time":"Sun18Nov12_0809PM", "end-time":"Sun18Nov12_0809PM" }
 * com.infostretch.automation.testng.report.MetaInfo.java
 * 
 * @author chirag
 */
public class MetaInfo {
	private SortedSet<ReportEntry> reports;

	/**
	 * @param reports
	 *            the reports to set
	 */
	public void setReports(Set<ReportEntry> reports) {
		getReports().addAll(reports);
	}

	/**
	 * @return the reports
	 */
	public SortedSet<ReportEntry> getReports() {
		if (null == reports) {
			reports = Collections.synchronizedSortedSet(new TreeSet<ReportEntry>());
		}
		return reports;
	}

	/**
	 * @param reports
	 *            the reports to set
	 */
	public void setReports(List<ReportEntry> reports) {
		getReports().addAll(reports);
	}

}
