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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.NewCookie;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.core.util.ReaderWriter;

/**
 * com.qmetry.qaf.ws.rest.client.Response.java
 * 
 * @author chirag
 */
public class Response {

	private transient String messageBody;
	private ClientResponse clientResponse;
	private MultivaluedMap<String, String> headers;
	private Status status;
	private MediaType mediaType;
	private List<NewCookie> cookies;

	private Date lastModified;

	private String language;

	private Date responseDate;

	protected Response() {

	}

	public Response(ClientResponse clientResponse) {
		init(clientResponse);
	}

	protected void init(ClientResponse clientResponse) {

		this.clientResponse = clientResponse;
		// messageBody = clientResponse.getEntity(type);
		setRawMessageBody();
		headers = clientResponse.getHeaders();
		status = clientResponse.getClientResponseStatus();
		cookies = clientResponse.getCookies();
		lastModified = clientResponse.getLastModified();
		responseDate = clientResponse.getResponseDate();
		language = clientResponse.getLanguage();

		try {
			mediaType = clientResponse.getType();
		} catch (Exception e) {
			System.err.println("Unable to parse media type. If want to access media type, you may try using 'Content-Type' from header.");
			e.printStackTrace();
		}

	}

	private void setRawMessageBody() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		InputStream in = clientResponse.getEntityInputStream();
		try {
			ReaderWriter.writeTo(in, out);

			byte[] requestEntity = out.toByteArray();
			if (requestEntity.length == 0) {
				return;
			}
			messageBody = new String(requestEntity);

			clientResponse.setEntityInputStream(new ByteArrayInputStream(requestEntity));
		} catch (IOException ex) {
		}

	}

	public String getMessageBody() {
		return messageBody;
	}

	public MultivaluedMap<String, String> getHeaders() {
		return headers;
	}

	public <T> T getEntity(Class<T> t) {
		return clientResponse.getEntity(t);
	}

	public Status getStatus() {
		return status;
	}

	public MediaType getMediaType() {
		return mediaType;
	}

	public List<NewCookie> getCookies() {
		return cookies;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public String getLanguage() {
		return language;
	}

	public Date getResponseDate() {
		return responseDate;
	}

	@Override
	public String toString() {
		return "";
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		try {
			clientResponse.getEntityInputStream().close();
		} catch (IOException e) {

		}
	}
}
