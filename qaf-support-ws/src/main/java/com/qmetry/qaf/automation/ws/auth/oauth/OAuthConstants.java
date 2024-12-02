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
package com.qmetry.qaf.automation.ws.auth.oauth;

public class OAuthConstants {
	public static final String PREFIX_KEY = "rest.client.oauth.auth.";
	public static final String ACCESS_TOKEN = PREFIX_KEY + "access_token";
	public static final String CLIENT_ID = PREFIX_KEY + "client_id";
	public static final String CLIENT_SECRET = PREFIX_KEY + "client_secret";
	public static final String REFRESH_TOKEN = PREFIX_KEY + "refresh_token";
	public static final String USERNAME = PREFIX_KEY + "username";
	public static final String PASSWORD = PREFIX_KEY + "password";
	public static final String AUTHENTICATION_SERVER_URL =
			PREFIX_KEY + "authentication_server_url";
	public static final String RESOURCE_SERVER_URL = PREFIX_KEY + "resource_server_url";
	public static final String GRANT_TYPE = PREFIX_KEY + "grant_type";
	public static final String CODE = "code";
	public static final String CALLER = "caller";
	public static final String GRANT_TYPE_PASSWORD = "password";
	public static final String GRANT_TYPE_AUTHORIZATION_CODE = "authorization_code";
	public static final String GRANT_TYPE_CLIENT_CREDENTIALS = "client_credentials";
	public static final String SCOPE = "scope";
	public static final String AUTHORIZATION = "Authorization";
	public static final String BEARER = "Bearer";
	public static final String BASIC = "Basic";
	public static final String JSON_CONTENT = "application/json";
	public static final String XML_CONTENT = "application/xml";
	public static final String URL_ENCODED_CONTENT = "application/x-www-form-urlencoded";

	public static final int HTTP_OK = 200;
	public static final int HTTP_FORBIDDEN = 403;
	public static final int HTTP_UNAUTHORIZED = 401;

}
