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
package com.qmetry.qaf.automation.scenario;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qmetry.qaf.automation.data.MetaDataScanner;

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

		boolean binclude = MetaDataScanner.includeMethod(scenarioMetadata, includeMeta,
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
