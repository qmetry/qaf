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
package com.qmetry.qaf.automation.data;

import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.keys.ApplicationProperties;
import com.qmetry.qaf.automation.util.StringUtil;
import com.qmetry.qaf.automation.util.Validator;

public class PasswordDecryptorTest {

	@Test
	public void defaultDecryptorTest() {
		ConfigurationManager.getBundle().setProperty(ApplicationProperties.PASSWORD_DECRYPTOR_IMPL.key, "");

		ConfigurationManager.getBundle().setProperty("encrypted.my.password",
				Base64PasswordDecryptor.getEncryptedPassword("Test@123#"));
		System.out.println(ConfigurationManager.getBundle().getString("my.password"));
		System.out.println(ConfigurationManager.getBundle().getString("encrypted.my.password"));
		Validator.assertThat(ConfigurationManager.getBundle().getString("my.password"),
				Matchers.equalToIgnoringCase("Test@123#"));

	}

	@Test
	public void customDecryptorTest() {
		ConfigurationManager.getBundle().setProperty(ApplicationProperties.PASSWORD_DECRYPTOR_IMPL.key,
				CustomPassWordDecryptorImpl.class.getCanonicalName());

		ConfigurationManager.getBundle().setProperty("encrypted.my.password", StringUtil.reverse("Test@123#"));
		System.out.println(ConfigurationManager.getBundle().getString("my.password"));
		System.out.println(ConfigurationManager.getBundle().getString("encrypted.my.password"));

		Validator.assertThat(ConfigurationManager.getBundle().getString("my.password"),
				Matchers.equalToIgnoringCase("Test@123#"));
		System.out.println("encrypted prop: " +ConfigurationManager.getBundle().getString("encrypted.my.pwd"));

		System.out.println("pass:" + ConfigurationManager.getBundle().getString("my.pwd"));

	}
}
