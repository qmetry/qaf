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

import static com.qmetry.qaf.automation.core.ConfigurationManager.getBundle;

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
 * com.qmetry.qaf.automation.util.SshUtil.java
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
