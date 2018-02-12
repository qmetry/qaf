/**
 * 
 */
package com.qmetry.qaf.automation.data;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;

import com.qmetry.qaf.automation.core.AutomationError;

/**
 * @author chirag.jayswal
 *
 */
public class Base64PasswordDecryptor implements PasswordDecryptor {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.qmetry.qaf.automation.data.PasswordDecryptor#getDecryptedPassword(
	 * java.lang.String)
	 */
	@Override
	public String getDecryptedPassword(String encriptedPassword) {
		byte[] decoded = Base64.decodeBase64(encriptedPassword);
		String decrypted;
		try {
			decrypted = new String(decoded, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new AutomationError("Unable to decrypt password", e);
		}
		return decrypted;
	}

	public static String getEncryptedPassword(String plainPassword) {
		String encryptedPassword = "";
		try {
			encryptedPassword = Base64.encodeBase64String(plainPassword.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return encryptedPassword;
	}

}
