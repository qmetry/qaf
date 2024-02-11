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
