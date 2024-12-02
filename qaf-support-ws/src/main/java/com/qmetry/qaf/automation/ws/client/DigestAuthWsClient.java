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

import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.ws.rest.DefaultRestClient;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.filter.HTTPDigestAuthFilter;

public class DigestAuthWsClient extends DefaultRestClient {

	public static final String REST_CLIENT_DIGEST_AUTH_USER = "rest.client.digest.auth.username";
	public static final String REST_CLIENT_DIGEST_AUTH_PASSWORD = "rest.client.digest.auth.password";

	@Override
	protected Client createClient() {
		Client client = super.createClient();
		client.addFilter(
				new HTTPDigestAuthFilter(ConfigurationManager.getBundle().getString(REST_CLIENT_DIGEST_AUTH_USER, ""),
						ConfigurationManager.getBundle().getString(REST_CLIENT_DIGEST_AUTH_PASSWORD, "")));
		return client;
	}
}
