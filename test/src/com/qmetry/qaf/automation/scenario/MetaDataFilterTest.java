/*******************************************************************************
 * Copyright 2016 Infostretch Corporation.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.qmetry.qaf.automation.scenario;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qmetry.qaf.automation.testng.pro.QAFMethodSelector;

public class MetaDataFilterTest {

	@SuppressWarnings("unchecked")
	@Test(dataProvider = "metaFilterDP")
	public void applyMetaFileterTest(String scenarioMetadatastr, String includeMetastr,
			String excludeMetastr, boolean expectedOutcome) {
		Gson gson = new GsonBuilder().create();

		Map<String, Object> includeMeta = gson.fromJson(includeMetastr, Map.class);
		Map<String, Object> excludeMeta = gson.fromJson(excludeMetastr, Map.class);

		Map<String, Object> scenarioMetadata =
				gson.fromJson(scenarioMetadatastr, Map.class);

		boolean binclude = QAFMethodSelector.includeMethod(scenarioMetadata, includeMeta,
				excludeMeta);

		Assert.assertEquals(binclude, expectedOutcome);
	}
	/*
	 * It contains tests with below different scenarios
	 * -
	 * -
	 * -
	 * nothing include - nothing exclude
	 * known metadata include - exclude nothing
	 * nothing include - known metadata exclude
	 * unknown metadata include - exclude nothing
	 * nothing include - unknown metadata exclude
	 * known metadata include - known metadata exclude
	 * same include - same exclude
	 * unknown metadata include - unknown metadata exclude
	 * known metadata include - unknown metadata exclude
	 * unknown metadata include - known metadata exclude
	 * unkown+known metadata include - nothing metadata exclude
	 * nothing metadata include - unkown+known metadata exclude
	 * known metadata include - unkown+known metadata exclude
	 * unknown metadata include - unkown+known metadata exclude
	 * unkown+known metadata include - known metadata exclude
	 * unkown+known metadata include - unkown metadata exclude
	 * unkown+known metadata include - unkown+known metadata exclude
	 * -
	 * -
	 **/
	@DataProvider(name = "metaFilterDP")
	public static Iterator<Object[]> testData() {
		ArrayList<Object[]> data = new ArrayList<Object[]>();

		// scenarioMetadatastr, includeMetastr, excludeMetastr, expectedOutcome

		// nothing include - nothing exclude
		data.add(new Object[]{"{'Brand':['B1'],'Module':'M1','group':['a','b']}", "{}",
				"{}", true});

		// known metadata include - exclude nothing
		data.add(new Object[]{"{'Brand':['B1'],'Module':'M1','group':['a','b']}",
				"{'Module':['M1'],'Brand':['B1'],'group':['b']}", "{}", true});

		// nothing include - known metadata exclude
		data.add(new Object[]{"{'Brand':['B1'],'Module':'M1','group':['a','b']}", "{}",
				"{'Module':['M1'],'group':['b']}", false});
		data.add(new Object[]{"{'Brand':['B1','B2'],'Module':'M1','group':['a','b']}",
				"{}", "{'Brand':['B2']}", false});
		data.add(new Object[]{"{'Brand':['B1','B2'],'Module':'M1','group':['a','b']}",
				"{}", "{'group':['a']}", false});
		data.add(new Object[]{"{'Brand':['B1','B2'],'Module':'M1','group':['a','b']}",
				"{}", "{'group':['a','b']}", false});

		// unknown metadata include - exclude nothing
		data.add(new Object[]{"{'Brand':['B1'],'Module':'M1','group':['a','b']}",
				"{'Module':['M'],'group':['c']}", "{}", false});

		// nothing include - unknown metadata exclude
		data.add(new Object[]{"{'Brand':['B1'],'Module':'M1','group':['a','b']}", "{}",
				"{'Module':['M'],'group':['c']}", true});

		// known metadata include - known metadata exclude
		data.add(new Object[]{"{'Brand':['B1'],'Module':'M1','group':['a','b']}",
				"{'group':['b']}", "{'Module':['M1']}", false});
		data.add(new Object[]{
				"{'Brand':['B1','B2'],'Module':['M1','M2'],'group':['a','b']}",
				"{'Brand':['B1'],'Module':['M1']}", "{'Module':['M2']}", false});
		data.add(new Object[]{
				"{'Brand':['B1','B2'],'Module':['M1','M2'],'group':['a','b']}",
				"{'Module':['M1']}", "{'Module':['M2']}", false});
		data.add(new Object[]{
				"{'Brand':['B1','B2'],'Module':['M1','M2'],'group':['a','b']}",
				"{'group':['a','b']}", "{'Module':['M2'],'Brand':['B1']}", false});
		data.add(new Object[]{
				"{'Brand':['B1','B2'],'Module':['M1','M2'],'group':['a','b']}",
				"{'Module':['M2']}", "{'Brand':['B1']}", false});

		// same include - same exclude
		data.add(new Object[]{"{'Brand':['B1','B2'],'Module':'M1','group':['a','b']}",
				"{'Module':['M1']}", "{'Module':['M1']}", false});

		// unknown metadata include - unknown metadata exclude
		data.add(new Object[]{"{'Price':['100'],'Author':['Atul'],'group':['a','b']}",
				"{'group':['c']}", "{'Author':['Priyesh']}", false});
		data.add(new Object[]{"{'Brand':['B1','B2'],'Module':'M1','group':['a','b']}",
				"{'Module':['M']}", "{'Module':['M2']}", false});
		data.add(new Object[]{"{'Price':['100'],'Author':['Atul'],'group':['a','b']}",
				"{'group':['c']}", "{'Author':['Priyesh']}", false});

		// known metadata include - unknown metadata exclude
		data.add(new Object[]{"{'Brand':['B1'],'Module':'M1','group':['a','b']}",
				"{'group':['b']}", "{'Module':['M']}", true});

		// unknown metadata include - known metadata exclude
		data.add(new Object[]{"{'Brand':['B1'],'Module':'M1','group':['a','b']}",
				"{'group':['c']}", "{'Module':['M1']}", false});
		data.add(new Object[]{"{'Brand':['B1','B2'],'Module':'M1','group':['a','b']}",
				"{'Module':['M']}", "{'Module':['M1']}", false});

		// unkown+known metadata include - nothing metadata exclude
		data.add(new Object[]{"{'Brand':['B1'],'Module':'M1','group':['a','b']}",
				"{'Module':['M1','M'],'Brand':['B1'],'group':['b']}", "{}", true});
		data.add(new Object[]{"{'Brand':['B1'],'Module':'M1','group':['a','b']}",
				"{'Module':['M1'],'Brand':['B1'],'group':['a','c']}", "{}", true});
		data.add(new Object[]{"{'Brand':['B1'],'Module':'M1','group':['a','b']}",
				"{'Module':['M1'],'Brand':['B1'],'group':['c']}", "{}", false});
		data.add(new Object[]{"{'Brand':['B1'],'Module':'M1','group':['a']}",
				"{'Module':['M1','M'],'group':['a','b']}", "{}", true});
		data.add(new Object[]{"{'Price':['100'],'Author':['Atul'],'group':['a','b']}",
				"{'Author':['Priyesh'],'Module':['100']}", "{}", false});
		data.add(new Object[]{"{'Price':['100'],'Author':['Atul'],'group':['a','b']}",
				"{'Author':['Atul'],'Module':['100']}", "{}", false});

		// nothing metadata include - unkown+known metadata exclude
		data.add(new Object[]{"{'Price':['100'],'Author':['Atul'],'group':['a','b']}",
				"{}", "{'Author':['Priyesh','Atul']}", false});
		data.add(new Object[]{"{'Price':['100'],'Author':['Atul'],'group':['a','b']}",
				"{}", "{'Author':['Priyesh'],'Price':['100']}", false});
		data.add(new Object[]{"{'Price':['100'],'Author':['Atul'],'group':['a','b']}",
				"{}", "{'Author':['Priyesh'],'Module':['100']}", true});
		data.add(new Object[]{"{'Price':['100'],'Author':['Atul'],'group':['a','b']}",
				"{}", "{'Author':['Atul'],'Module':['100']}", false});

		// known metadata include - unkown+known metadata exclude
		data.add(new Object[]{"{'Brand':['B1','B2'],'Module':'M1','group':['a','b']}",
				"{'Module':['M1']}", "{'group':['c'],'Brand':['B1']}", false});
		data.add(new Object[]{"{'Brand':['B1','B2'],'Module':'M1','group':['a','b']}",
				"{'Module':['M1']}", "{'group':['c','b']}", false});
		data.add(new Object[]{"{'Brand':['B1','B2'],'Module':'M1','group':['a','b']}",
				"{'Module':['M1']}", "{'group':['c','b'],'Brand':['B1']}", false});
		data.add(new Object[]{"{'Brand':['B1','B2'],'Module':'M1','group':['a','b']}",
				"{'Module':['M1']}", "{'Module':['M'],'group':['c'],'Brand':['B1']}",
				false});
		// unknown metadata include - unkown+known metadata exclude
		data.add(new Object[]{"{'Price':['100'],'Author':['Atul'],'group':['a','b']}",
				"{'group':['c']}", "{'Author':['Priyesh'],'group':['a']}", false});
		// unkown+known metadata include - known metadata exclude
		data.add(new Object[]{"{'Brand':['B1'],'Module':'M1','group':['a','b']}",
				"{'Module':['M1','M'],'Brand':['B1'],'group':['b']}", "{'group':['b']}",
				false});
		// unkown+known metadata include - unkown metadata exclude
		data.add(new Object[]{"{'Brand':['B1'],'Module':'M1','group':['a','b']}",
				"{'Module':['M1','M'],'Brand':['B1'],'group':['b']}", "{'group':['c']}",
				true});
		data.add(new Object[]{"{'Price':['100'],'Author':['Atul'],'group':['a','b']}",
				"{'group':['a','b','c']}", "{'Author':['Priyesh']}", true});

		// unkown+known metadata include - unkown+known metadata exclude
		data.add(new Object[]{"{'Price':['100'],'Author':['Atul'],'group':['a','b']}",
				"{'group':['c','b']}", "{'Author':['Priyesh'],'Price':['100']}", false});

		return data.iterator();
	}

}
