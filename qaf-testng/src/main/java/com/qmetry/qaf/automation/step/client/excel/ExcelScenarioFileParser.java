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
import com.qmetry.qaf.automation.util.PoiExcelUtil;

/**
 * @author chirag.jayswal
 */
public class ExcelScenarioFileParser extends AbstractScenarioFileParser {

	protected void parseFile(String file, List<Scenario> scenarios) {

		try {
			File scenarioFile = new File(file);
			List<String> sheets = PoiExcelUtil.getSheetNames(scenarioFile);
			for (String sheet:sheets) {
				String reference = scenarioFile.getPath() + ":" + sheet;
				Object[][] rows = PoiExcelUtil.getExcelData(scenarioFile.getAbsolutePath(), false, sheet);
				processStatements(rows, reference, scenarios);
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
