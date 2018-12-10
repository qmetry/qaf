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
