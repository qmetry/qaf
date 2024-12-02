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
package com.qmetry.qaf.automation.ws.client;

import java.util.Arrays;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import com.qmetry.qaf.automation.ws.rest.RestClientFactory;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.client.apache4.ApacheHttpClient4Handler;
import static com.qmetry.qaf.automation.core.ConfigurationManager.getBundle;

/**
 * Jersey client to support NTLM authentication. This class uses following properties:
 *<ul>
 *<li><code>ntlm.user </code>- The user name. This should not include the domain to authenticate with. For example: "user" is correct whereas "DOMAIN\\user" is not.
 *<li><code>ntlm.password </code>- The password
 *<li><code>ntlm.workstation </code>- workstation (default is blank) The workstation the authentication request is originating from. Essentially, the computer name for this machine.
 *<li><code>ntlm.domain</code>- domain The domain to authenticate within (default is blank).
 *</ul>
 *
 * For NTLM authentication, register this class using <code>rest.client.impl</code> property as below:
 * <p>
 * <code>rest.client.impl=com.qmetry.qaf.automation.ws.client.NTLMAuthClient</code>
 * 
 * @author Chirag Jayswal
 * @since 2.1.12
 */
public class NTLMAuthClient extends RestClientFactory {

	private static final String USERNAME = getBundle().getString("ntlm.user");
	private static final String PASSWORD = getBundle().getString("ntlm.password");
	private static final String WORKSTATION = getBundle().getString("ntlm.workstation","");
	private static final String DOMAIN = getBundle().getString("ntlm.domain","");

	@Override
	protected Client createClient() {
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
		
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30000)
				.setConnectionRequestTimeout(30000)
				.setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM))
				.setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC))
				.build();
		
		CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(AuthScope.ANY, new NTCredentials(USERNAME, PASSWORD, WORKSTATION, DOMAIN));
		
		CloseableHttpClient closeableHttpClient = HttpClients.custom().setConnectionManager(cm)
				.setDefaultCredentialsProvider(credentialsProvider)
				.setDefaultRequestConfig(requestConfig)
				.build();
		
		ApacheHttpClient4Handler root = new ApacheHttpClient4Handler(closeableHttpClient, new BasicCookieStore(), false);
		Client client = new Client(root);
		return client;
	}

}
