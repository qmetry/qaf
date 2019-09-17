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
package com.qmetry.qaf.automation.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import com.qmetry.qaf.automation.util.JSONUtil;
import com.qmetry.qaf.automation.util.Validator;

public class JsonUtilTest {

	@Test
	public void testToMap() {
		String jsonString = "{'locator':'css=footer.footer';'a':1}";
		Map<String, Object> result = JSONUtil.toMap(jsonString);
		Validator.assertThat(result, Matchers.hasKey("locator"));
	}

	// @Test
	public void testGetJsonObjectFromFile() {

	}

	Object[][] getJsonFiles() throws IOException {
		List<String> files = new ArrayList<String>();
		File f = File.createTempFile("file1", ".json");
		final String[] array = {"a", "b", "c"};
		// JSONUtil.writeJsonObjectToFile(f, a);
		files.add(f.getAbsolutePath());
		return null;
	}
}
