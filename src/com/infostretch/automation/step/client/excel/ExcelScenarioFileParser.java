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

package com.infostretch.automation.step.client.excel;

import java.io.File;
import java.util.Collection;
import java.util.List;

import com.infostretch.automation.step.client.AbstractScenarioFileParser;
import com.infostretch.automation.step.client.Scenario;
import com.infostretch.automation.util.ExcelUtil;

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
