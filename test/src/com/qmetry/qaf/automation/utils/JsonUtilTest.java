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
