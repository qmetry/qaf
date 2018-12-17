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

import static com.qmetry.qaf.automation.core.ConfigurationManager.getBundle;

import java.util.Iterator;

import org.apache.commons.configuration.Configuration;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.multipart.impl.MultiPartWriter;

/**
 * @author chirag.jayswal
 */
public class DefaultRestClient extends RestClientFactory {
	private static final String REST_CLIENT_PROP_PREFIX =
			"com.sun.jersey.client.property";
	Client client;
	public DefaultRestClient() {
		ClientConfig config = new DefaultClientConfig();
		config.getClasses().add(MultiPartWriter.class);
		client = Client.create(config);
	}

	public DefaultRestClient(Client client) {
		this.client = client;

	}

	/*
	 * (non-Javadoc)
	 * @see com.qmetry.qaf.automation.ws.rest.RestClientFactory#createClient()
	 */
	@Override
	protected Client createClient() {
		Configuration props = getBundle().subset(REST_CLIENT_PROP_PREFIX);
		Iterator<?> iter = props.getKeys();

		while (iter.hasNext()) {
			String prop = (String) iter.next();
			client.getProperties().put(REST_CLIENT_PROP_PREFIX + prop,
					props.getString(prop));
		}
		return client;
	}

}
