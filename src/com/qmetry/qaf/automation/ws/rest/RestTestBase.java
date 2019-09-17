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
package com.qmetry.qaf.automation.ws.rest;

import com.qmetry.qaf.automation.core.AutomationError;
import com.qmetry.qaf.automation.core.QAFTestBase;
import com.qmetry.qaf.automation.core.TestBaseProvider;
import com.qmetry.qaf.automation.keys.ApplicationProperties;
import com.qmetry.qaf.automation.ws.Response;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

/**
 * com.qmetry.qaf.automation.ws.rest.RestTestBase.java
 * 
 * @author chirag
 */
public class RestTestBase {

	private static final String REST_CLIENT_KEY = "rest.client";
	protected static final String REST_REQ_TRACKER_KEY = "rest.client.requesttracker";

	public Client getClient() {
		if (null == getTestBase().getContext().getObject(REST_CLIENT_KEY)) {
			String clientFactoryClass = ApplicationProperties.REST_CLIENT_FACTORY_IMPL
					.getStringVal(DefaultRestClient.class.getName());
			try {
				RestClientFactory clientFactory = (RestClientFactory) Class
						.forName(clientFactoryClass).newInstance();
				getTestBase().getContext().setProperty(REST_CLIENT_KEY,
						clientFactory.getClient());
			} catch (Exception e) {
				new AutomationError("Unable to create rest client", e);
			}
		}
		return (Client) getTestBase().getContext().getObject(REST_CLIENT_KEY);
	}

	public void setClient(Client client) {
		getTestBase().getContext().setProperty(REST_CLIENT_KEY,
				new DefaultRestClient(client).getClient());
	}

	public Response getResponse() {
		return new Response(getRequestTracker().getClientResponse());
	}

	public WebResource getWebResource(String resouce) {
		return getWebResource(ApplicationProperties.SELENIUM_BASE_URL.getStringVal(),
				resouce);
	}

	public WebResource getWebResource(String serviceEndPoint, String resouce) {
		return getClient().resource(serviceEndPoint).path(resouce);
	}

	public void resetClient() {
		getTestBase().getContext().clearProperty(REST_CLIENT_KEY);
	}

	static QAFTestBase getTestBase() {
		return TestBaseProvider.instance().get();
	}

	static RequestTracker getRequestTracker() {
		RequestTracker requestTracker = (RequestTracker) getTestBase().getContext()
				.getObject(REST_REQ_TRACKER_KEY);
		if (requestTracker == null) {
			requestTracker = new RequestTracker();

			getTestBase().getContext().setProperty(REST_REQ_TRACKER_KEY, requestTracker);
		}

		return requestTracker;
	}

}
