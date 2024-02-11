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

import static com.qmetry.qaf.automation.core.ConfigurationManager.getBundle;

import java.util.Iterator;

import org.apache.commons.configuration.Configuration;

import com.qmetry.qaf.automation.util.JSONUtil;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.apache.ApacheHttpClient;
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
		if(getBundle().getBoolean("rest.client.useApacheHttpClient", true)) {
			client = ApacheHttpClient.create(config);
		}else {
			client = Client.create(config);
		}
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
			client.getProperties().put(REST_CLIENT_PROP_PREFIX+ "." + prop,
					JSONUtil.toObject(props.getString(prop)));
		}
		return client;
	}

}
