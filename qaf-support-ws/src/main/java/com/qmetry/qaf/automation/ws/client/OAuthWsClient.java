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
import com.qmetry.qaf.automation.util.StringUtil;
import com.qmetry.qaf.automation.ws.auth.oauth.OAuth2Details;
import com.qmetry.qaf.automation.ws.auth.oauth.OAuthConstants;
import com.qmetry.qaf.automation.ws.auth.oauth.OAuthUtils;
import com.qmetry.qaf.automation.ws.rest.DefaultRestClient;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.ClientFilter;

public class OAuthWsClient extends DefaultRestClient {
	
	@Override
	protected Client createClient() {
		Client client = super.createClient();
		client.addFilter(new ClientFilter() {

			public ClientResponse handle(ClientRequest cr) throws ClientHandlerException {
				cr.getHeaders().add(OAuthConstants.AUTHORIZATION, OAuthUtils
						.getAuthorizationHeaderForAccessToken(getAccessToken()));
				return getNext().handle(cr);
			}

			private String getAccessToken() {
				String accessToken = ConfigurationManager.getBundle()
						.getString(OAuthConstants.ACCESS_TOKEN);
				if (StringUtil.isEmpty(accessToken)) {
					// Generate the OAuthDetails bean from the config properties
					// file
					OAuth2Details oauthDetails = OAuthUtils
							.createOAuthDetails(ConfigurationManager.getBundle());

					System.out.println(oauthDetails);
					// Validate Input
					if (!OAuthUtils.isValidInput(oauthDetails)) {
						System.out.println(
								"Please provide valid config properties to continue.");
						System.exit(0);
					}

					// Generate new Access token
					accessToken = OAuthUtils.getAccessToken(oauthDetails);

					if (OAuthUtils.isValid(accessToken)) {
						ConfigurationManager.getBundle()
								.setProperty(OAuthConstants.ACCESS_TOKEN, accessToken);
						System.out.println(
								"Successfully generated Access token for client_credentials grant_type: "
										+ accessToken);
					} else {
						System.out.println(
								"Could not generate Access token for client_credentials grant_type");
					}
				}
				return accessToken;
			}
		});
		return client;
	}
}
