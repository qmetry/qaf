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
package com.qmetry.qaf.automation.step.client.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.qmetry.qaf.automation.step.client.AbstractScenarioFileParser;
import com.qmetry.qaf.automation.testng.DataProviderException;
import com.qmetry.qaf.automation.util.StringUtil;

/**
 * @author chirag.jayswal
 */
public class KwdFileParser extends AbstractScenarioFileParser {

	@Override
	protected Collection<Object[]> parseFile(String scenarioFile) {
		return getCSVData(scenarioFile, '|');
	}

	public List<Object[]> getCSVData(String strFile, char separatorChar) {
		ArrayList<Object[]> rows = new ArrayList<Object[]>();
		File csvFile;
		BufferedReader br = null;
		try {

			logger.info("loading  keywords/scenarios from file: " + strFile);
			csvFile = new File(strFile);
			br = new BufferedReader(new FileReader(csvFile));
			String strLine = "";
			// read comma separated file line by line
			// exclude blank lines and comments
			int lineNo = 0;
			while ((strLine = br.readLine()) != null) {
				lineNo++;

				if (!("".equalsIgnoreCase(strLine.trim()) || "#!".contains("" + strLine.trim().charAt(0)))) {
					Object[] cols = new Object[] { "", "", "", lineNo };
					Object[] csvcols = StringUtil.parseCSV(strLine, separatorChar);// strLine.split(separatorChar);
					System.arraycopy(csvcols, 0, cols, 0, csvcols.length);

					rows.add(cols);
				}
			}
		} catch (Exception e) {
			logger.error("Exception while reading csv file: " + strFile + e);
			throw new DataProviderException("Error while fetching data from " + strFile, e);

		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		return rows;
	}

}
