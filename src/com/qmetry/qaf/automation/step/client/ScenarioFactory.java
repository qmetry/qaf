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
package com.qmetry.qaf.automation.step.client;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;
import org.testng.ITestContext;
import org.testng.annotations.Factory;

import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.data.MetaDataScanner;
import com.qmetry.qaf.automation.util.FileUtil;
import com.qmetry.qaf.automation.util.StringUtil;

/**
 * Factory class for custom step client.
 * com.qmetry.qaf.automation.step.client.ScenarioFactory.java
 * 
 * @author chirag.jayswal
 */
public abstract class ScenarioFactory {
	public static final String GROUPS = "groups";

	protected final Log logger = LogFactoryImpl.getLog(getClass());

	private List<Scenario> scenarios = new LinkedList<Scenario>();
	private List<String> fileExtension;
	private List<String> includeGroups = new ArrayList<String>();
	private List<String> excludeGroups = new ArrayList<String>();

	public ScenarioFactory(List<String> list) {
		this.fileExtension = list;
	}

	@Factory
	public Object[] getTestsFromFile(ITestContext context) {

		if (null != context) {
			includeGroups = Arrays.asList(context.getIncludedGroups());
			excludeGroups = Arrays.asList(context.getExcludedGroups());
		}

		String sanariosloc = MetaDataScanner.getParameter(context, "scenario.file.loc");
		if (StringUtil.isNotBlank(sanariosloc)) {
			ConfigurationManager.getBundle().setProperty("scenario.file.loc", sanariosloc);
		}

		System.out.printf("include groups %s\n exclude groups: %s Scanarios location: %s \n", includeGroups,
				excludeGroups, sanariosloc);
		logger.info("scenario.file.loc"
				+ ConfigurationManager.getBundle().getStringArray("scenario.file.loc", "./scenarios"));
		for (String fileName : ConfigurationManager.getBundle().getStringArray("scenario.file.loc", "./scenarios")) {
			process(fileName);
		}

		logger.info("total test found: " + scenarios.size());
		return scenarios.toArray(new Object[scenarios.size()]);

	}

	protected abstract ScenarioFileParser getParser();

	public void process(String fileName) {
		ScenarioFileParser parser = getParser();
		parser.setExcludeGroups(excludeGroups);
		parser.setIncludeGroups(includeGroups);

		File fileOrDir = new File(fileName);
		if (fileOrDir.isDirectory()) {
			Collection<File> files = FileUtil.listFiles(fileOrDir, fileExtension.toArray(new String[] {}), true);
			for (File scenarioFile : files) {
				try {
					parser.parse(scenarioFile.getAbsolutePath(), scenarios);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else if (fileExtension.contains(FileUtil.getExtention(fileName.toLowerCase()))) {
			try {
				parser.parse(fileName, scenarios);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

}
