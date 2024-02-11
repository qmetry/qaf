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
package com.qmetry.qaf.automation.util;

import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;

/**
 * com.qmetry.qaf.automation.util.MalinatorUtil.java
 * 
 * @author chirag
 */
public class MalinatorUtil {

	public static void main(String[] args) throws Exception {

		Properties props = new Properties();

		String host = "pop.mailinator.com";
		String username = "x-test-m";
		String password = "mypwdxxx";
		String provider = "pop3";

		Session session = Session.getDefaultInstance(props, null);
		Store store = session.getStore(provider);
		store.connect(host, username, password);

		Folder inbox = store.getFolder("INBOX");
		if (inbox == null) {
			System.out.println("No INBOX");
			System.exit(1);
		}
		inbox.open(Folder.READ_ONLY);

		Message[] messages = inbox.getMessages();
		for (int i = 0; i < messages.length; i++) {
			System.out.println("Message " + (i + 1));
			messages[i].writeTo(System.out);
		}
		inbox.close(false);
		store.close();

	}
}
