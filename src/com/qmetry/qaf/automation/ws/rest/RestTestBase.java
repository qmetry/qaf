/*******************************************************************************
 * QMetry Automation Framework provides a powerful and versatile platform to
 * author
 * Automated Test Cases in Behavior Driven, Keyword Driven or Code Driven
 * approach
 * Copyright 2016 Infostretch Corporation
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT
 * OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE
 * You should have received a copy of the GNU General Public License along with
 * this program in the name of LICENSE.txt in the root folder of the
 * distribution. If not, see https://opensource.org/licenses/gpl-3.0.html
 * See the NOTICE.TXT file in root folder of this source files distribution
 * for additional information regarding copyright ownership and licenses
 * of other open source software / files used by QMetry Automation Framework.
 * For any inquiry or need additional information, please contact
 * support-qaf@infostretch.com
 *******************************************************************************/

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
