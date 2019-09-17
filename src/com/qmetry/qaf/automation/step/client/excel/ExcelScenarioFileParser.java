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
package com.qmetry.qaf.automation.step.client.excel;

import java.io.File;
import java.util.Collection;
import java.util.List;

import com.qmetry.qaf.automation.step.client.AbstractScenarioFileParser;
import com.qmetry.qaf.automation.step.client.Scenario;
import com.qmetry.qaf.automation.util.ExcelUtil;

import jxl.Workbook;

/**
 * @author chirag.jayswal
 */
public class ExcelScenarioFileParser extends AbstractScenarioFileParser {

	protected void parseFile(String file, List<Scenario> scenarios) {

		try {
			File scenarioFile = new File(file);

			Workbook workbook = Workbook.getWorkbook(scenarioFile);
			String[] sheets = workbook.getSheetNames();
			for (int sheet = 0; sheet < workbook.getNumberOfSheets(); sheet++) {
				String referece = scenarioFile.getPath() + ":" + sheets[sheet];
				Object[][] rows = ExcelUtil.getExcelData(scenarioFile.getAbsolutePath(), false, sheets[sheet]);
				processStatements(rows, referece, scenarios);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void parse(String scenarioFile, List<Scenario> scenarios) {
		parseFile(scenarioFile, scenarios);
	}

	@Override
	protected Collection<Object[]> parseFile(String scenarioFile) {
		return null;
	}

}
