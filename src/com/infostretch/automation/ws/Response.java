/*******************************************************************************
 * QMetry Automation Framework provides a powerful and versatile platform to author 
 * Automated Test Cases in Behavior Driven, Keyword Driven or Code Driven approach
 *                
 * Copyright 2016 Infostretch Corporation
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT
 * OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE
 *
 * You should have received a copy of the GNU General Public License along with this program in the name of LICENSE.txt in the root folder of the distribution. If not, see https://opensource.org/licenses/gpl-3.0.html
 *
 * See the NOTICE.TXT file in root folder of this source files distribution 
 * for additional information regarding copyright ownership and licenses
 * of other open source software / files used by QMetry Automation Framework.
 *
 * For any inquiry or need additional information, please contact support-qaf@infostretch.com
 *******************************************************************************/


package com.infostretch.automation.ws;

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
 * com.infostretch.ws.rest.client.Response.java
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
		headers = clientResponse.getHeaders();
		status = clientResponse.getClientResponseStatus();
		mediaType = clientResponse.getType();
		cookies = clientResponse.getCookies();
		lastModified = clientResponse.getLastModified();
		responseDate = clientResponse.getResponseDate();
		language = clientResponse.getLanguage();

		setRawMessageBody();

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
