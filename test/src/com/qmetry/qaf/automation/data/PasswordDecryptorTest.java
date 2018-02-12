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
		ConfigurationManager.getBundle().setProperty(ApplicationProperties.PASSWOED_DECRYPTOR_IMPL.key, "");

		ConfigurationManager.getBundle().setProperty("encrypted.my.password",
				Base64PasswordDecryptor.getEncryptedPassword("Test@123#"));
		System.out.println(ConfigurationManager.getBundle().getString("my.password"));
		System.out.println(ConfigurationManager.getBundle().getString("encrypted.my.password"));
		Validator.assertThat(ConfigurationManager.getBundle().getString("my.password"),
				Matchers.equalToIgnoringCase("Test@123#"));

	}

	@Test
	public void customDecryptorTest() {
		ConfigurationManager.getBundle().setProperty(ApplicationProperties.PASSWOED_DECRYPTOR_IMPL.key,
				CustomPassWordDecryptorImpl.class.getCanonicalName());

		ConfigurationManager.getBundle().setProperty("encrypted.my.password", StringUtil.reverse("Test@123#"));
		System.out.println(ConfigurationManager.getBundle().getString("my.password"));
		System.out.println(ConfigurationManager.getBundle().getString("encrypted.my.password"));

		Validator.assertThat(ConfigurationManager.getBundle().getString("my.password"),
				Matchers.equalToIgnoringCase("Test@123#"));
	}
}
