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

package com.qmetry.qaf.automation.ws.rest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.qmetry.qaf.automation.core.LoggingBean;
import com.qmetry.qaf.automation.core.TestBaseProvider;
import com.sun.jersey.api.client.AbstractClientRequestAdapter;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientRequestAdapter;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.ClientFilter;
import com.sun.jersey.api.client.filter.ContainerListener;
import com.sun.jersey.api.client.filter.OnStartConnectionListener;
import com.sun.jersey.core.util.ReaderWriter;

/**
 * A logging filter for request and response.
 * 
 * @author chirag
 */
public class RequestLogger extends ClientFilter implements OnStartConnectionListener {

	private final Log logger = LogFactory.getLog(RequestLogger.class);

	private static final String NOTIFICATION_PREFIX = "* ";

	private static final String REQUEST_PREFIX = ">> ";

	private static final String RESPONSE_PREFIX = "<< ";

	// private long startTime, resStartTime, endTime;

	private final class Adapter extends AbstractClientRequestAdapter {
		private final StringBuilder b;

		Adapter(ClientRequestAdapter cra, StringBuilder b) {
			super(cra);
			this.b = b;
		}

		public OutputStream adapt(ClientRequest request, OutputStream out) throws IOException {
			return new LoggingOutputStream(getAdapter().adapt(request, out), b);
		}

	}

	private final class LoggingOutputStream extends OutputStream {
		private final OutputStream out;

		private final ByteArrayOutputStream baos = new ByteArrayOutputStream();

		private final StringBuilder b;

		LoggingOutputStream(OutputStream out, StringBuilder b) {
			this.out = out;
			this.b = b;
		}

		@Override
		public void write(byte[] b) throws IOException {
			baos.write(b);
			out.write(b);
		}

		@Override
		public void write(byte[] b, int off, int len) throws IOException {
			baos.write(b, off, len);
			out.write(b, off, len);
		}

		@Override
		public void write(int b) throws IOException {
			baos.write(b);
			out.write(b);
		}

		@Override
		public void close() throws IOException {
			printEntity(b, baos.toByteArray());
			log(b.toString());
			out.close();
		}
	}

	private final PrintStream loggingStream;

	private long _id = 0;

	/**
	 * Create a logging filter logging the request and response to a default JDK
	 * logger, named as the fully qualified class name of this class.
	 */
	public RequestLogger() {
		loggingStream = null;
	}

	public RequestLogger(PrintStream loggingStream) {
		this.loggingStream = loggingStream;
	}

	private void log(String b) {
		if (loggingStream != null) {
			loggingStream.print(b);
		} else {
			logger.info(b.toString());
		}
	}

	private StringBuilder prefixId(StringBuilder b, long id) {
		b.append(Long.toString(id)).append(" ");
		return b;
	}

	@Override
	public ClientResponse handle(ClientRequest request) throws ClientHandlerException {
		long id = ++_id;

		StringBuilder requestString = new StringBuilder();
		logRequest(id, request, requestString);

		LoggingBean loggingBean = new LoggingBean();
		loggingBean.setCommandName(request.getMethod() + ":" + request.getURI());

		loggingBean.setArgs(new String[] { request.getURI().getPath(), request.getURI().getQuery() });

		ClientResponse response = getNext().handle(request);
		loggingBean.setResult(response.toString());
		StringBuilder responseString = logResponse(id, response);

		LoggingBean detailsLoggingBean = new LoggingBean();
		detailsLoggingBean.setArgs(new String[] { noPrifix(requestString).toString() });
		detailsLoggingBean.setResult(noPrifix(responseString).toString());

		loggingBean.getSubLogs().add(detailsLoggingBean);

		TestBaseProvider.instance().get().getLog().add(loggingBean);

		return response;
	}

	private void logRequest(long id, ClientRequest request, StringBuilder b) {

		printRequestLine(b, id, request);
		printRequestHeaders(b, id, request.getHeaders());
		if (request.getEntity() != null) {
			prefixId(b, id).append(REQUEST_PREFIX).append(request.getEntity());
			request.setAdapter(new Adapter(request.getAdapter(), b));
		}
		log(b.toString());
	}

	private void printRequestLine(StringBuilder b, long id, ClientRequest request) {
		prefixId(b, id).append(NOTIFICATION_PREFIX).append("Client out-bound request").append("\n");
		prefixId(b, id).append(REQUEST_PREFIX).append(request.getMethod()).append(" ")
				.append(request.getURI().toASCIIString()).append("\n");
	}

	private void printRequestHeaders(StringBuilder b, long id, MultivaluedMap<String, Object> headers) {
		for (Map.Entry<String, List<Object>> e : headers.entrySet()) {
			List<Object> val = e.getValue();
			String header = e.getKey();

			if (val.size() == 1) {
				prefixId(b, id).append(REQUEST_PREFIX).append(header).append(": ")
						.append(ClientRequest.getHeaderValue(val.get(0))).append("\n");
			} else {
				StringBuilder sb = new StringBuilder();
				boolean add = false;
				for (Object o : val) {
					if (add) {
						sb.append(',');
					}
					add = true;
					sb.append(ClientRequest.getHeaderValue(o));
				}
				prefixId(b, id).append(REQUEST_PREFIX).append(header).append(": ").append(sb.toString()).append("\n");
			}
		}
	}

	private StringBuilder logResponse(long id, ClientResponse response) {
		StringBuilder b = new StringBuilder();

		printResponseLine(b, id, response);
		printResponseHeaders(b, id, response.getHeaders());

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		InputStream in = response.getEntityInputStream();
		try {
			ReaderWriter.writeTo(in, out);

			byte[] requestEntity = out.toByteArray();
			printEntity(b, requestEntity);
			response.setEntityInputStream(new ByteArrayInputStream(requestEntity));
		} catch (IOException ex) {
			throw new ClientHandlerException(ex);
		}
		log(b.toString());
		return b;
	}

	private void printResponseLine(StringBuilder b, long id, ClientResponse response) {
		prefixId(b, id).append(NOTIFICATION_PREFIX).append("Client in-bound response").append("\n");
		prefixId(b, id).append(RESPONSE_PREFIX).append(Integer.toString(response.getStatus())).append("\n");
	}

	private void printResponseHeaders(StringBuilder b, long id, MultivaluedMap<String, String> headers) {
		for (Map.Entry<String, List<String>> e : headers.entrySet()) {
			String header = e.getKey();
			for (String value : e.getValue()) {
				prefixId(b, id).append(RESPONSE_PREFIX).append(header).append(": ").append(value).append("\n");
			}
		}
		prefixId(b, id).append(RESPONSE_PREFIX).append("\n");
	}

	private void printEntity(StringBuilder b, byte[] entity) throws IOException {
		if (entity.length == 0) {
			return;
		}
		b.append(new String(entity)).append("\n");
	}

	private StringBuilder noPrifix(StringBuilder sb) {
		deleteAll(sb, Long.toString(_id) + " " + REQUEST_PREFIX);
		deleteAll(sb, Long.toString(_id) + " " + RESPONSE_PREFIX);
		deleteAll(sb, Long.toString(_id) + " " + NOTIFICATION_PREFIX);

		return sb;
	}

	public static void deleteAll(StringBuilder builder, String str) {
		if (null == builder) {
			return;
		}
		int index = builder.indexOf(str);
		while (index != -1) {
			builder.delete(index, index + str.length());
			index = builder.indexOf(str, index);
		}
	}

	@Override
	public ContainerListener onStart(ClientRequest cr) {
		return new ContainerListener() {

			@Override
			public void onSent(long delta, long bytes) {
				// startTime = System.currentTimeMillis();
				log(prefixId(new StringBuilder(), _id).append(NOTIFICATION_PREFIX).append("Sent: delta: ").append(delta)
						.append(" bytes: ").append(bytes).append("\n").toString());

			}

			@Override
			public void onReceiveStart(long totalBytes) {
				// resStartTime = System.currentTimeMillis();
				log(prefixId(new StringBuilder(), _id).append(NOTIFICATION_PREFIX).append("Receive Start: ")
						.append(totalBytes).append("\n").toString());
			}

			@Override
			public void onReceived(long delta, long bytes) {
				log(prefixId(new StringBuilder(), _id).append(NOTIFICATION_PREFIX).append("Received: delta: ")
						.append(delta).append(" bytes: ").append(bytes).append("\n").toString());
			}

			@Override
			public void onFinish() {
				log("Finished");

				log(prefixId(new StringBuilder(), _id).append(NOTIFICATION_PREFIX).append("Finished").append("\n")
						.toString());
			}

		};
	}
}