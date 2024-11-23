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

import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import com.qmetry.qaf.automation.impl.ClassWithFields;
import com.qmetry.qaf.automation.util.ClassUtil;
import com.qmetry.qaf.automation.util.Validator;

/**
 * @author chirag.jayswal
 *
 */
public class ClassUtilTest {
	@Test
	public void testFinalFieldAccess() {
		Object cls = new ClassWithFields();
		
		ClassUtil.setField("finalStaticField", cls, "new value");
		Object val = ClassUtil.getField("finalStaticField", cls);
		
		//Validator.assertThat(val, Matchers.equalTo(((Object)"new value")));
		
		ClassUtil.setField("finalField", cls, "new value2");
		val = ClassUtil.getField("finalField", cls);
		//Validator.assertThat(val, Matchers.equalTo(((Object)"new value2")));

		cls = ClassUtil.getField("innerClassObj", cls);
		ClassUtil.setField("field", cls, "new value3");

		val = ClassUtil.getField("field", cls);

		Validator.assertThat(val, Matchers.equalTo(((Object)"new value3")));

	}
}
