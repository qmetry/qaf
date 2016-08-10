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

import static com.infostretch.automation.core.ConfigurationManager.getBundle;

import java.io.InputStream;
import java.io.OutputStream;

import com.sshtools.j2ssh.SshClient;
import com.sshtools.j2ssh.authentication.AuthenticationProtocolState;
import com.sshtools.j2ssh.authentication.PasswordAuthenticationClient;
import com.sshtools.j2ssh.configuration.ConfigurationException;
import com.sshtools.j2ssh.configuration.ConfigurationLoader;
import com.sshtools.j2ssh.connection.ChannelState;
import com.sshtools.j2ssh.io.IOStreamConnector;
import com.sshtools.j2ssh.session.SessionChannelClient;
import com.sshtools.j2ssh.transport.HostKeyVerification;
import com.sshtools.j2ssh.transport.IgnoreHostKeyVerification;

/**
 * com.infostretch.automation.util.SshUtil.java
 * 
 * @author chirag
 */
public class SshUtil {
	static {
		try {
			ConfigurationLoader.initialize(false);
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * connect to host
	 * 
	 * @return
	 * @throws Exception
	 */
	private static SshClient connectSsh() throws Exception {
		SshClient ssh = new SshClient();

		HostKeyVerification host = new IgnoreHostKeyVerification();
		String hostStr = getBundle().getString("ssh.host");
		ssh.connect(hostStr, host);
		PasswordAuthenticationClient auth = new PasswordAuthenticationClient();
		auth.setUsername(getBundle().getString("ssh.user"));
		auth.setPassword(getBundle().getString("ssh.pwd"));
		int result = ssh.authenticate(auth);

		System.out.println("Status " + result);
		if ((result == AuthenticationProtocolState.CANCELLED) || (result == AuthenticationProtocolState.FAILED)) {
			throw new Exception("Authentication Error.");
		}
		return ssh;
	}

	public static String executeCommand(String command) throws Exception {
		SessionChannelClient session = null;
		String res = "";
		try {
			SshClient ssh = connectSsh();
			// String command = getBundle().getString("ssh.cmd");

			session = ssh.openSessionChannel();
			OutputStream out = new java.io.ByteArrayOutputStream();

			session = ssh.openSessionChannel();
			IOStreamConnector output = new IOStreamConnector();
			output.connect(session.getInputStream(), out);

			if (session.executeCommand(command)) {
				session.getState().waitForState(ChannelState.CHANNEL_CLOSED, 15000);
				res = out.toString();
			} else {
				res = "Unable to execute command " + command;
			}
		} finally {
			try {
				session.close();
			} catch (Exception e) {
			}
		}
		return res;
	}

	/**
	 * Get shell and execute command.
	 * 
	 * @param cmd
	 * @return
	 * @throws Exception
	 */
	public String executeSudoCommand(String command) throws Exception {

		String res = "";
		SessionChannelClient session = null;
		try {
			SshClient ssh = connectSsh();
			session = ssh.openSessionChannel();

			if (session.requestPseudoTerminal("isfw", 80, 24, 0, 0, "")) {
				if (session.startShell()) {
					session.getOutputStream().write((command + "\n").getBytes());

					InputStream in = session.getInputStream();
					byte buffer[] = new byte[255];
					int read;

					while ((read = in.read(buffer)) > 0) {

						res += new String(buffer, 0, read);
						System.out.println("res: " + res);

						if (res.contains("password")) {
							session.getOutputStream().write((getBundle().getString("ssh.pwd") + "\n").getBytes());
							res = command + "\n";
						}

						if (res.contains(command)) {
							if (res.endsWith("]$ ")) {
								break;
							}
							if (command.equalsIgnoreCase("status") && res.endsWith("]$ ")) {
								break;
							}
						}
					}

				} else {
					res = "Unable to start shell.";
				}
			} else {
				res = "Unable to request terminal.";
			}
		} finally {
			try {
				session.close();
			} catch (Exception e) {
			}
		}
		return res;
	}
}
