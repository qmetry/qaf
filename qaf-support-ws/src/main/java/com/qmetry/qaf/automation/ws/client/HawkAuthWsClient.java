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

import com.qmetry.qaf.automation.ws.rest.DefaultRestClient;
import com.sun.jersey.api.client.Client;
import com.wealdtech.hawk.HawkClient;
import com.wealdtech.hawk.HawkCredentials;
import com.wealdtech.hawk.jersey.HawkAuthorizationFilter;

public class HawkAuthWsClient extends DefaultRestClient {

	public static final String REST_CLIENT_HAWK_KEY_ID = "rest.client.hawk.auth.keyId";
	public static final String REST_CLIENT_HAWK_KEY = "rest.client.hawk.auth.key";

	@Override
	protected Client createClient() {

		HawkCredentials hawkCredentials = new HawkCredentials.Builder()
				.keyId(REST_CLIENT_HAWK_KEY_ID).key(REST_CLIENT_HAWK_KEY)
				.algorithm(HawkCredentials.Algorithm.SHA256).build();

		HawkClient hawkClient =
				new HawkClient.Builder().credentials(hawkCredentials).build();

		Client client = super.createClient();
		client.addFilter(new HawkAuthorizationFilter(hawkClient));
		return client;
	}
}
