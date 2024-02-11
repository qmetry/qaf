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

import static com.qmetry.qaf.automation.ws.rest.RestTestBase.getRequestTracker;
import static com.qmetry.qaf.automation.ws.rest.RestTestBase.getTestBase;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.filter.ConnectionListenerFilter;

/**
 * @author chirag.jayswal
 */
public abstract class RestClientFactory {
	private static final String REST_REQ_LOGGER_KEY = "rest.client.requestlogger";

	abstract protected Client createClient();
	
	public final Client getClient() {
		Client client = createClient();

		client.addFilter(new ConnectionListenerFilter(getRequestListener()));
		client.addFilter(getRequestListener());
		client.addFilter(getRequestTracker());

		return client;
	}

	private RequestLogger getRequestListener() {
		RequestLogger requestLogger =
				(RequestLogger) getTestBase().getContext().getObject(REST_REQ_LOGGER_KEY);

		if (requestLogger == null) {
			//no need to provide print stream as logger will be used if print stream not provided
			requestLogger = new RequestLogger();
			getTestBase().getContext().setProperty(REST_REQ_LOGGER_KEY, requestLogger);
		}

		return requestLogger;
	}
}
