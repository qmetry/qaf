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

import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.util.Validator;

/**
 * @author chirag.jayswal
 *
 */
public class ConfigurationTest {

	@Test
	public void testRand(){

		ConfigurationManager.getBundle().setProperty("rndthree", "${rnd:123}");
		ConfigurationManager.getBundle().setProperty("another", "${rnd:<%rnd:123%>}");

		ConfigurationManager.getBundle().setProperty("ts", "${expr:java.lang.System.currentTimeMillis()}");
		ConfigurationManager.getBundle().setProperty("UTCts", "${expr:java.time.Instant.now()}");

		long ts = System.currentTimeMillis();
		Object val = ConfigurationManager.getBundle().getSubstitutor().replace("${expr:java.lang.System.currentTimeMillis()}");
		System.out.println("expr:java.lang.System.currentTimeMillis(): " + val);
		Validator.assertThat(Long.parseLong(val.toString()), Matchers.greaterThanOrEqualTo(ts));

		val = ConfigurationManager.getBundle().getString("rndthree");
		System.out.println("rndthree: " + val);
		Validator.assertThat(Integer.parseInt(val.toString()), Matchers.lessThan(1000));
		val = ConfigurationManager.getBundle().getInt("rndthree");
		System.out.println("rndthree: " + val);
		Validator.assertThat((int)val, Matchers.lessThan(1000));

		ts = System.currentTimeMillis();
		val = ConfigurationManager.getBundle().getLong("ts");
		System.out.println("ts: " + val);
		Validator.assertThat((Long)val, Matchers.greaterThanOrEqualTo(ts));
		
		ts = System.currentTimeMillis();
		val = ConfigurationManager.getBundle().getLong("ts");
		System.out.println("ts: " + val);
		Validator.assertThat((Long)val, Matchers.greaterThanOrEqualTo(ts));
		
		val = ConfigurationManager.getBundle().getSubstitutor().replace("${expr:java.util.UUID.randomUUID()}");
		System.out.println("expr:java.util.UUID.randomUUID(): " + val);
		
		val = ConfigurationManager.getBundle().getSubstitutor().replace("${expr:java.util.UUID.fromString('d4f7cffe-df93-4ac8-8184-4701064c5bcb')}");
		System.out.println("expr:java.util.UUID.fromString('d4f7cffe-df93-4ac8-8184-4701064c5bcb'): " + val);
		Validator.assertThat(val, Matchers.equalTo(java.util.UUID.fromString("d4f7cffe-df93-4ac8-8184-4701064c5bcb").toString()));
		
		System.out.println("UTCts: " +ConfigurationManager.getBundle().getString("UTCts"));
		val = ConfigurationManager.getBundle().getSubstitutor().replace("${expr:com.qmetry.qaf.automation.util.DateUtil.getDate(0, 'MM/dd/yyyy')}");
		System.out.println("com.qmetry.qaf.automation.util.DateUtil.getDate(0, 'MM/dd/yyyy'): " + val);
		Validator.assertThat(val, Matchers.equalTo(com.qmetry.qaf.automation.util.DateUtil.getDate(0, "MM/dd/yyyy")));
		
		System.out.println(ConfigurationManager.getBundle().getString("another"));
		
		val = ConfigurationManager.getBundle().getSubstitutor().replace("${expr:com.qmetry.qaf.automation.util.DateUtil.getDate(<%rnd:9%>, 'MM/dd/yyyy')}");
		System.out.println("com.qmetry.qaf.automation.util.DateUtil.getDate(0, 'MM/dd/yyyy'): " + val);
		val = ConfigurationManager.getBundle().getSubstitutor().replace("${expr:com.qmetry.qaf.automation.util.DateUtil.getDate(<%rnd:9%>, 'MM/dd/yyyy')}");
		System.out.println("com.qmetry.qaf.automation.util.DateUtil.getDate(0, 'MM/dd/yyyy'): " + val);
		
		ConfigurationManager.getBundle().setProperty("abc.cde", "abcde");
		ConfigurationManager.getBundle().setProperty("xyz.cde", "xyzcde");
		ConfigurationManager.getBundle().setProperty("target", "${<%prefix%>.cde}");
		
		ConfigurationManager.getBundle().setProperty("prefix", "abc");
		val=ConfigurationManager.getBundle().getString("target");
		System.out.println(val);
		Validator.assertThat(val, Matchers.equalTo("abcde"));


		ConfigurationManager.getBundle().setProperty("prefix", "xyz");
		val=ConfigurationManager.getBundle().getString("target");
		System.out.println(val);
		Validator.assertThat(val, Matchers.equalTo("xyzcde"));
		
		ConfigurationManager.getBundle().setProperty("prefix", "${abc}");
		ConfigurationManager.getBundle().setProperty("abc", "abc");
		ConfigurationManager.getBundle().setProperty("abc.cde", "${abc}def");
		ConfigurationManager.getBundle().setProperty("target", "${<%prefix%>.cde}");
		
		val=ConfigurationManager.getBundle().getString("target");
		System.out.println(val);
		Validator.assertThat(val, Matchers.equalTo("abcdef"));

	}
}
