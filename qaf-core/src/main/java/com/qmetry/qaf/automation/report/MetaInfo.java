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
package com.qmetry.qaf.automation.report;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * { "reports":[{"dir":"test_result_1","status":"pass","tests":["test1"]},{"dir"
 * : "test_result_2","status":"fail"}], "total": 2, "pass": 1, "fail": 1,
 * "skip": 0, "start-time":"Sun18Nov12_0809PM", "end-time":"Sun18Nov12_0809PM" }
 * com.qmetry.qaf.automation.testng.report.MetaInfo.java
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
