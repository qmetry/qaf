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
package com.qmetry.qaf.automation.ws;

import com.qmetry.qaf.automation.testng.TestNGTestCase;
import com.qmetry.qaf.automation.util.Validator;
import com.qmetry.qaf.automation.ws.rest.RestTestBase;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

/**
 * com.qmetry.qaf.automation.ws.WSTestCase.java
 * 
 * @author chirag
 */
public class WSTestCase extends TestNGTestCase {
	private RestTestBase testBase;

	public WSTestCase() {
		testBase = new RestTestBase();
	}

	public Response getResponse() {
		return testBase.getResponse();
	}

	protected WebResource getWebResource(String resouce) {
		return testBase.getWebResource(resouce);
	}

	protected WebResource getWebResource(String serviceEndPoint, String resouce) {
		return testBase.getWebResource(serviceEndPoint, resouce);
	}

	public Client getClient() {
		return testBase.getClient();
	}

	public void resetClient() {
		testBase.resetClient();
	}

	public void setClient(Client client) {
		testBase.setClient(client);
	}
}
