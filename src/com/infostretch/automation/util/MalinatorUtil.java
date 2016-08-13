/*******************************************************************************
 * QMetry Automation Framework provides a powerful and versatile platform to author 
 * Automated Test Cases in Behavior Driven, Keyword Driven or Code Driven approach
 *                
 * Copyright 2016 Infostretch Corporation
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT
 * OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE
 *
 * You should have received a copy of the GNU General Public License along with this program in the name of LICENSE.txt in the root folder of the distribution. If not, see https://opensource.org/licenses/gpl-3.0.html
 *
 * See the NOTICE.TXT file in root folder of this source files distribution 
 * for additional information regarding copyright ownership and licenses
 * of other open source software / files used by QMetry Automation Framework.
 *
 * For any inquiry or need additional information, please contact support-qaf@infostretch.com
 *******************************************************************************/


package com.infostretch.automation.util;

import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;

/**
 * com.infostretch.automation.util.MalinatorUtil.java
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
