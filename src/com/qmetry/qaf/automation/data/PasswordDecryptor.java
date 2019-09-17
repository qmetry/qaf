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

import com.qmetry.qaf.automation.keys.ApplicationProperties;

/**
 * This interface should be implemented to support encrypted password. You
 * should use any algorithm of your choice to encrypt password and implement
 * this interface to provide decrypted password. You need to register your
 * implementation with {@link ApplicationProperties#PASSWORD_DECRYPTOR_IMPL} property. Configuration manager will use registered implementation to store decrypted value for key starting with prefix dec
 * 
 * @author chirag.jayswal
 *
 */
public interface PasswordDecryptor {
	/**
	 * 
	 * @param encriptedPassword
	 * @return
	 */
	public String getDecryptedPassword(String encriptedPassword);
}
