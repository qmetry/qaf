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

import com.qmetry.qaf.automation.integration.TestCaseRunResult.Status;

/**
 * @author chirag.jayswal
 *
 */
public class StatusCounter {

	private String name;
	private int pass;
	private int fail;
	private int skip;
	private String file;

	protected StatusCounter(String name) {
		this.name = name;
		this.file = name;
	}

	public static StatusCounter of(String name) {
		return new StatusCounter(name);
	}

	public StatusCounter withFile(String file) {
		this.file = file;
		return this;
	}

	public void add(Status s) {
		if (s.equals(Status.PASS))
			pass++;
		if (s.equals(Status.FAIL))
			fail++;
		if (s.equals(Status.SKIPPED))
			skip++;
	}

	public String getName() {
		return name;
	}

	public int getPass() {
		return pass;
	}

	public int getFail() {
		return fail;
	}

	public int getSkip() {
		return skip;
	}

	public int getTotal() {
		return pass + fail + skip;
	}

	public String getStatus() {
		return getTotal() == pass? "pass": "fail";
	}
	public int getPassRate() {
		return pass > 0 ? pass * 100 / (pass + fail + skip) : 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;

		return this.file.equalsIgnoreCase(obj.toString());
	}

	@Override
	public String toString() {
		return file;
	}

	@Override
	public int hashCode() {
		return file.hashCode();
	}
}
