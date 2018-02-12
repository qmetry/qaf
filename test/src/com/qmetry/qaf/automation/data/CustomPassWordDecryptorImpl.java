package com.qmetry.qaf.automation.data;

import com.qmetry.qaf.automation.util.StringUtil;

public class CustomPassWordDecryptorImpl implements PasswordDecryptor {

	@Override
	public String getDecryptedPassword(String encriptedPassword) {
		return StringUtil.reverse(encriptedPassword);
	}
}
