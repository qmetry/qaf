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
package com.qmetry.qaf.automation.step;

import static com.qmetry.qaf.automation.core.ConfigurationManager.getBundle;
import static com.qmetry.qaf.automation.util.Validator.assertFalse;
import static com.qmetry.qaf.automation.util.Validator.assertThat;
import static com.qmetry.qaf.automation.util.Validator.assertTrue;
import static com.qmetry.qaf.automation.util.Validator.verifyFalse;
import static com.qmetry.qaf.automation.util.Validator.verifyThat;
import static com.qmetry.qaf.automation.util.Validator.verifyTrue;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.io.FileUtils;
import org.hamcrest.Matchers;
import org.json.JSONObject;

import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.google.gson.Gson;
import com.jayway.jsonpath.JsonPath;
import com.qmetry.qaf.automation.core.AutomationError;
import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.core.MessageTypes;
import com.qmetry.qaf.automation.keys.ApplicationProperties;
import com.qmetry.qaf.automation.util.JSONUtil;
import com.qmetry.qaf.automation.util.Reporter;
import com.qmetry.qaf.automation.util.StringMatcher;
import com.qmetry.qaf.automation.util.StringUtil;
import com.qmetry.qaf.automation.util.Validator;
import com.qmetry.qaf.automation.util.XPathUtils;
import com.qmetry.qaf.automation.ws.WSCRepositoryConstants;
import com.qmetry.qaf.automation.ws.WsRequestBean;
import com.qmetry.qaf.automation.ws.rest.RestTestBase;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;

/**
 * com.qmetry.qaf.automation.step.CommonStep.java
 * 
 * @author chirag
 */
public final class WsStep {

	/**
	 * This method check for the response status of web service
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * response should have status 'ResponceStatus'<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param status
	 *            : {0} : Status String for Exampe: OK, CREATED
	 * @see Status
	 */
	@QAFTestStep(description = "response should have status {status}")
	public static void responseShouldHaveStatus(String status) {
		assertThat("Response Status", new RestTestBase().getResponse().getStatus().name(),
				Matchers.equalToIgnoringCase(status));
	}

	/**
	 * This method check for the response status of web service
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * response should have status 'ResponceStatus'<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param status
	 *            : {0} : Status code, for example 200, 301
	 * @see Status
	 */
	@QAFTestStep(description = "response should have status code {statusCode}")
	public static void responseShouldHaveStatusCode(int statusCode) {
		assertThat("Response Status", new RestTestBase().getResponse().getStatus().getStatusCode(),
				Matchers.equalTo(statusCode));
	}

	/**
	 * This method request for the given parameters
	 * 
	 * @param request
	 *            key or map
	 * @return
	 */
	@QAFTestStep(description = "user requests {0}")
	public static ClientResponse userRequests(Object request) {
		WsRequestBean bean = new WsRequestBean();
		bean.fillData(request);
		bean.resolveParameters(null);
		return request(bean);
	}

	/**
	 * This method request for given parameters with given dataset
	 * 
	 * @param request
	 *            key or map
	 * @param data
	 *            data set of key value pair
	 * @return
	 */
	@QAFTestStep(description = "user requests {request} with data {data}", stepName = "userRequestsWithData")
	public static ClientResponse userRequests(Object request, Map<String, Object> data) {
		WsRequestBean bean = new WsRequestBean();
		bean.fillData(request);
		bean.resolveParameters(data);
		return request(bean);
	}

	/**
	 * This method check given header is there in response of web service
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * response should have header 'Content-Type'<br/>
	 * </code> <br/>
	 * 
	 * @param header
	 *            : header to be verified in respose
	 */
	@QAFTestStep(description = "response should have header {0}")
	public static void responseShouldHaveHeader(String header) {
		assertThat(new RestTestBase().getResponse().getHeaders(), Matchers.hasKey(header));
	}

	/**
	 * This method check given header with specific value present in response of
	 * web service
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * response should have header 'Content-Type' with value 'application/json'<br/>
	 * </code> <br/>
	 * 
	 * @param header
	 *            : header to be present in respose
	 * @param value
	 *            : value to be verified for header
	 */
	@QAFTestStep(description = "response should have header {0} with value {1}")
	public static void responseShouldHaveHeaderWithValue(String header, String value) {
		assertThat(new RestTestBase().getResponse().getHeaders(), hasEntry(equalTo(header), Matchers.hasItem(value)));
	}

	/**
	 * This method check given jsonpath is there in response of web service
	 * <p>
	 * Example:
	 * </p>
	 * <code>
	 * response should have 'user.username'
	 * </code>
	 * <p />
	 * 
	 * @param path
	 *            : jsonpath
	 */
	@QAFTestStep(description = "response should have {jsonpath}")
	public static void responseShouldHaveJsonPath(String path) {
		if (!path.startsWith("$"))
			path = "$." + path;
		assertThat("Response Body has " + path, hasJsonPath(new RestTestBase().getResponse().getMessageBody(), path),
				Matchers.equalTo(true));
	}

	/**
	 * This method check given jsonpath is not in response of web service
	 * <p>
	 * Example:
	 * </p>
	 * <code>
	 * response should not have 'user.username'
	 * </code>
	 * <p />
	 * 
	 * @param path
	 *            : jsonpath
	 */
	@QAFTestStep(description = "response should not have {jsonpath}")
	public static void responseShouldNotHaveJsonPath(String path) {
		if (!path.startsWith("$"))
			path = "$." + path;
		assertThat("Response Body has not " + path,
				hasJsonPath(new RestTestBase().getResponse().getMessageBody(), path), Matchers.equalTo(false));
	}

	/**
	 * This method validates value for given jsonpath
	 * <p>
	 * Example:
	 * </p>
	 * <code>
	 * response should have 'admin' at 'user.username'
	 * </code>
	 * <p />
	 * 
	 * @param expectedValue
	 *            : expected value
	 * @param path
	 *            : jsonpath
	 */
	@QAFTestStep(description = "response should have {expectedvalue} at {jsonpath}")
	public static void responseShouldHaveKeyWithValue(Object expectedValue, String path) {
		if (!path.startsWith("$"))
			path = "$." + path;
		Object actual = JsonPath.read(new RestTestBase().getResponse().getMessageBody(), path);
		if (null != actual && Number.class.isAssignableFrom(actual.getClass())) {
			assertThat(new BigDecimal(String.valueOf(actual)),
					Matchers.equalTo(new BigDecimal(String.valueOf(expectedValue))));
		} else {
			assertThat(actual, Matchers.equalTo((Object) expectedValue));
		}
	}

	/**
	 * This method store value of given json path to
	 * {@link ConfigurationManager}
	 * <p>
	 * Example:
	 * </p>
	 * <code>
	 * store response body 'user.username' to 'loginuser'
	 * </code>
	 * <p />
	 * 
	 * @deprecated user {@link #sayValueAtJsonPath(String, String)}
	 * @param path
	 *            jsonpath
	 * @param variable
	 *            variable that can be use later
	 */
	@QAFTestStep(description = "store response body {0} (in)to {1}")
	public static void storeResponseBodyto(String path, String variable) {
		if (!path.startsWith("$"))
			path = "$." + path;
		Object value = JsonPath.read(new RestTestBase().getResponse().getMessageBody(), path);
		getBundle().setProperty(variable, value);
	}

	/**
	 * This method validates value of jsonpath contains expected value or not
	 * <p>
	 * Example:
	 * </p>
	 * <code>
	 * response should have value contains 'admin' at 'user.username'
	 * </code>
	 * <p />
	 * 
	 * @param value
	 *            : expected value
	 * @param path
	 *            : jsonpath
	 */
	@QAFTestStep(description = "response should have value contains {expectedvalue} at {jsonpath}")
	public static void responseShouldHaveKeyAndValueContains(String value, String path) {
		if (!path.startsWith("$"))
			path = "$." + path;
		Object actual = JsonPath.read(new RestTestBase().getResponse().getMessageBody(), path);
		assertThat(String.valueOf(actual), Matchers.containsString(value));
	}

	/**
	 * This method store value of given header to {@link ConfigurationManager}
	 * <p>
	 * Example:
	 * </p>
	 * <code>
	 * store response header 'X-Auth-token' to 'token'
	 * </code>
	 * <p />
	 * 
	 * @param header
	 *            header
	 * @param property
	 *            variable that can be use later
	 */
	@QAFTestStep(description = "store response header {0} (in)to {1}")
	public static void storeResponseHeaderTo(String header, String property) {
		getBundle().setProperty(property, new RestTestBase().getResponse().getHeaders().get(header));
	}

	/**
	 * This method validates that value at jsonpath should be less than
	 * expectedvalue
	 * <p>
	 * Example:
	 * </p>
	 * <code>
	 * response should be less than 500 at 'student.totalmarks'
	 * </code>
	 * <p />
	 * 
	 * @param value
	 *            : expected value
	 * @param path
	 *            : jsonpath
	 */
	@QAFTestStep(description = "response should be less than {expectedvalue} at {jsonpath}")
	public static void responseShouldLessThan(double expectedValue, String path) {
		Object actual = JsonPath.read(new RestTestBase().getResponse().getMessageBody(), getPath(path));
		assertThat(Double.parseDouble(String.valueOf(actual)), Matchers.lessThan(expectedValue));
	}

	/**
	 * This method downloads and save file from the response in report directory for current execution.
	 * <p>
	 * BDD Example:
	 * </p>
	 * <pre>
	 * <code>
	 *    Given user requests "download.file.req"
	 *    And store into 'file.response'
	 *    Then save file as "logo.png" from "${file.response}"
	 * </code>
	 * </pre>
	 * <p>
	 * Java Example:
	 * </p>
	 * <pre>
	 * <code>
	 *    ClientResponse response  = userRequests("download.file.req");
	 *    downloadFileFromResponse("logo.png", response);
	 * </code>
	 * </pre>
	 * @param name
	 * @param response
	 * @throws IOException
	 */
	@QAFTestStep(description = "download file as {file-name} from {response}")
	public static void downloadFileFromResponse(String name, ClientResponse response) throws IOException {
		InputStream in = response.getEntityInputStream();
		File logo = new File(ApplicationProperties.JSON_REPORT_DIR.getStringVal("."), name);
		Files.copy(in, logo.toPath());
	}

	/**
	 * This method validates that value at jsonpath should be less than or equal
	 * to expectedvalue
	 * <p>
	 * Example:
	 * </p>
	 * <code>
	 * response should be less than or equals to 500 at 'student.totalmarks'
	 * </code>
	 * <p />
	 * 
	 * @param value
	 *            : expected value
	 * @param path
	 *            : jsonpath
	 */
	@QAFTestStep(description = "response should be less than or equals to {expectedvalue} at {jsonpath}")
	public static void responseShouldLessThanOrEqualsTo(double expectedValue, String path) {
		Object actual = JsonPath.read(new RestTestBase().getResponse().getMessageBody(), getPath(path));
		assertThat(Double.parseDouble(String.valueOf(actual)), Matchers.lessThanOrEqualTo(expectedValue));
	}

	/**
	 * This method validates that value at jsonpath should be greater than
	 * expectedvalue
	 * <p>
	 * Example:
	 * </p>
	 * <code>
	 * response should be greater than 500 at 'student.totalmarks'
	 * </code>
	 * <p />
	 * 
	 * @param value
	 *            : expected value
	 * @param path
	 *            : jsonpath
	 */
	@QAFTestStep(description = "response should be greater than {expectedvalue} at {jsonpath}")
	public static void responseShouldGreaterThan(double expectedValue, String path) {
		Object actual = JsonPath.read(new RestTestBase().getResponse().getMessageBody(), getPath(path));
		assertThat(Double.parseDouble(String.valueOf(actual)), Matchers.greaterThan(expectedValue));
	}

	/**
	 * This method validates that value at jsonpath should be greater than or
	 * equal to expectedvalue
	 * <p>
	 * Example:
	 * </p>
	 * <code>
	 * response should be greater than or equals to 500 at 'student.totalmarks'
	 * </code>
	 * <p />
	 * 
	 * @param value
	 *            : expected value
	 * @param path
	 *            : jsonpath
	 */
	@QAFTestStep(description = "response should be greater than or equals to {expectedvalue} at {jsonpath}")
	public static void responseShouldGreaterThanOrEqualsTo(double expectedValue, String path) {
		Object actual = JsonPath.read(new RestTestBase().getResponse().getMessageBody(), getPath(path));
		assertThat(Double.parseDouble(String.valueOf(actual)), Matchers.greaterThanOrEqualTo(expectedValue));
	}

	/**
	 * This method validates value at jsonpath equals to expected value with
	 * ignoring case or not
	 * <p>
	 * Example:
	 * </p>
	 * <code>
	 * response should have value ignoring case 'admin' at 'user.username'
	 * </code>
	 * <p />
	 * 
	 * @param value
	 *            : expected value
	 * @param path
	 *            : jsonpath
	 */
	@QAFTestStep(description = "response should have value ignoring case {expectedvalue} at {jsonpath}")
	public static void responseShouldHaveValueIgnoringCase(String expectedValue, String path) {
		Object actual = JsonPath.read(new RestTestBase().getResponse().getMessageBody(), getPath(path));
		assertThat(String.valueOf(actual), Matchers.equalToIgnoringCase(expectedValue));
	}

	/**
	 * This method validates value at jsonpath contains expected value with
	 * ignoring case or not
	 * <p>
	 * Example:
	 * </p>
	 * <code>
	 * response should have value contains ignoring case 'admin' at 'user.username'
	 * </code>
	 * <p />
	 * 
	 * @param value
	 *            : expected value
	 * @param path
	 *            : jsonpath
	 */
	@QAFTestStep(description = "response should have value contains ignoring case {expectedvalue} at {jsonpath}")
	public static void responseShouldHaveValueContainsIgnoringCase(String expectedValue, String path) {
		Object actual = JsonPath.read(new RestTestBase().getResponse().getMessageBody(), getPath(path));
		assertThat(String.valueOf(actual).toUpperCase(), Matchers.containsString(expectedValue.toUpperCase()));
	}

	/**
	 * This method validates value of jsonpath matches with regEx value or not
	 * <p>
	 * Example:
	 * </p>
	 * <code>
	 * response should have value matches with '[a-z]*' at 'user.username'
	 * </code>
	 * <p />
	 * 
	 * @param value
	 *            : expected value
	 * @param path
	 *            : jsonpath
	 */
	@QAFTestStep(description = "response should have value matches with {regEx} at {jsonpath}")
	public static void responseShouldHaveValueMatchesWith(String regEx, String path) {
		Object actual = JsonPath.read(new RestTestBase().getResponse().getMessageBody(), getPath(path));
		assertThat(String.valueOf(actual).matches(regEx), Matchers.equalTo(true));
	}

	/**
	 * This method validates value of jsonpath is not equal to expected value
	 * <p>
	 * Example:
	 * </p>
	 * <code>
	 * response should not have value 'admin' at 'user.username'
	 * </code>
	 * <p />
	 * 
	 * @param value
	 *            : expected value
	 * @param path
	 *            : jsonpath
	 */
	@QAFTestStep(description = "response should not have value {expectedvalue} at {jsonpath}")
	public static void responseShouldNotHaveValue(Object expectedValue, String path) {
		Object actual = JsonPath.read(new RestTestBase().getResponse().getMessageBody(), getPath(path));
		assertThat(actual, Matchers.not(expectedValue));
	}

	@QAFTestStep(description = "verify response schema for {0}")
	public static boolean verifyResponseSchema(String requestKey) {
		ProcessingReport result = null;
		try {
			com.fasterxml.jackson.databind.JsonNode responseNode = JsonLoader
					.fromString(new RestTestBase().getResponse().getMessageBody());
			Map<String, Object> map = JSONUtil
					.toMap(ConfigurationManager.getBundle().getString(requestKey, requestKey));
			Object responseSchema = map.get(WSCRepositoryConstants.RESPONSE_SCHEMA);
			if (responseSchema instanceof Map) {
				responseSchema = new Gson().toJson(responseSchema);
			} else {
				File file = new File(responseSchema.toString());
				if (file.exists())
					responseSchema = FileUtils.readFileToString(file, "UTF-8");
			}
			com.fasterxml.jackson.databind.JsonNode schemaNode = JsonLoader.fromString(String.valueOf(responseSchema));
			JsonSchema schema = JsonSchemaFactory.byDefault().getJsonSchema(schemaNode);
			result = schema.validate(responseNode);
			if (!result.isSuccess()) {
				for (ProcessingMessage processingMessage : result) {
					Reporter.log(processingMessage.getMessage(), MessageTypes.Fail);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ProcessingException e) {
			e.printStackTrace();
		}
		return result != null && result.isSuccess();
	}

	/**
	 * This is verification method to check value at jsonpath in response status
	 * of web service. It will continue test case even if failure. It uses
	 * {@link StringMatcher} to match expected vs actual values. You can specify
	 * matcher by providing prefix separated by ':', For example,
	 * 'containsIgnoringCase:someValue' will use
	 * {@link StringMatcher#containsIgnoringCase(String)} Default is
	 * {@link StringMatcher#exact(String)}
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * response should have value '1.90' at jsonpath '$.Price'<br/>
	 * response should have value 'gte:1.90' at jsonpath '$.Price'<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param val
	 *            : {val} : value to be verified in response
	 * @param jsonpath
	 *            : {jsonpath} : jsonpath to look value in response
	 */
	@QAFTestStep(description = "response should have value {val} at jsonpath {path}")
	public static void responseShouldHaveValueAtJsonpath(Object val, String jsonpath) {
		Object actual = JsonPath.read(new RestTestBase().getResponse().getMessageBody(), getPath(jsonpath));
		StringMatcher matcher = getMatcher(val);
		boolean res = matcher.match(String.valueOf(actual));
		String message = "Expected value at jsonpath " + jsonpath + " [" + matcher + "] actual [" + actual + "]";
		verifyTrue(res, message, message);

	}

	/**
	 * This is verification method to check value is not at jsonpath in response
	 * status of web service. It will continue test case even if failure. It
	 * uses {@link StringMatcher} to match expected vs actual values. You can
	 * specify matcher by providing prefix separated by ':', For example,
	 * 'containsIgnoringCase:someValue' will use
	 * {@link StringMatcher#containsIgnoringCase(String)} Default is
	 * {@link StringMatcher#exact(String)}
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * response should not have value '1.90' at jsonpath '$.Price'<br/>
	 * response should not have value 'gte:1.90' at jsonpath '$.Price'<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param val
	 *            : {val} : value to be verified in response
	 * @param jsonpath
	 *            : {jsonpath} : jsonpath to look value in response
	 */
	@QAFTestStep(description = "response should not have value {val} at jsonpath {path}")
	public static void responseShouldNotHaveValueAtJsonpath(Object val, String jsonpath) {
		Object actual = JsonPath.read(new RestTestBase().getResponse().getMessageBody(), getPath(jsonpath));
		StringMatcher matcher = getMatcher(val);
		boolean res = matcher.match(String.valueOf(actual));
		String message = "Expected value at jsonpath " + jsonpath + " is not [" + matcher + "] actual [" + actual + "]";
		verifyFalse(res, message, message);

	}

	/**
	 * This is assertion method to check value at jsonpath in response status of
	 * web service. It will break test case on failure. It uses
	 * {@link StringMatcher} to match expected vs actual values. You can specify
	 * matcher by providing prefix separated by ':', For example,
	 * 'containsIgnoringCase:someValue' will use
	 * {@link StringMatcher#containsIgnoringCase(String)} Default is
	 * {@link StringMatcher#exact(String)}
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * response has value '1.90' at jsonpath '$.Price'<br/>
	 * response has value 'gte:1.90' at jsonpath '$.Price'<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param val
	 *            : {val} : value to be verified in response
	 * @param jsonpath
	 *            : {jsonpath} : jsonpath to look value in response
	 */
	@QAFTestStep(description = "response has value {val} at jsonpath {jsonpath}")
	public static void responseHasValueAtJsonpath(Object val, String jsonpath) {
		Object actual = JsonPath.read(new RestTestBase().getResponse().getMessageBody(), getPath(jsonpath));
		StringMatcher matcher = getMatcher(val);
		boolean res = matcher.match(String.valueOf(actual));
		String message = "Expected value at jsonpath " + jsonpath + " [" + matcher + "] actual [" + actual + "]";
		assertTrue(res, message, message);
	}

	/**
	 * This is assertion method to check value is not at jsonpath in response
	 * status of web service. It will break test case on failure. It uses
	 * {@link StringMatcher} to match expected vs actual values. You can specify
	 * matcher by providing prefix separated by ':', For example,
	 * 'containsIgnoringCase:someValue' will use
	 * {@link StringMatcher#containsIgnoringCase(String)} Default is
	 * {@link StringMatcher#exact(String)}
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * response has not value '1.90' at jsonpath '$.Price'<br/>
	 * response has not value 'gte:1.90' at jsonpath '$.Price'<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param val
	 *            : {val} : value to be verified in response
	 * @param jsonpath
	 *            : {jsonpath} : jsonpath to look value in response
	 */
	@QAFTestStep(description = "response has not value {val} at jsonpath {jsonpath}")
	public static void responseHasValueNotAtJsonpath(Object val, String jsonpath) {
		Object actual = JsonPath.read(new RestTestBase().getResponse().getMessageBody(), getPath(jsonpath));
		StringMatcher matcher = getMatcher(val);
		boolean res = matcher.match(String.valueOf(actual));
		String message = "Expected value at jsonpath " + jsonpath + " is not [" + matcher + "] actual [" + actual + "]";
		assertFalse(res, message, message);
	}
	
	@QAFTestStep(description = "response contains json {expectedJsonStr}")
	public static void assertResponseContains(String expectedJsonStr) {
		Validator.assertJsonContains(new RestTestBase().getResponse().getMessageBody(), expectedJsonStr);
	}

	@QAFTestStep(description = "response should contain json {expectedJsonStr}")
	public static void verifyResponseContains(String expectedJsonStr) {
		Validator.verifyJsonContains(new RestTestBase().getResponse().getMessageBody(), expectedJsonStr);
	}

	@QAFTestStep(description = "response matches json {expectedJsonStr}")
	public static void assertResponseMatches(String expectedJsonStr) {
		Validator.assertJsonMatches(new RestTestBase().getResponse().getMessageBody(), expectedJsonStr);
	}

	@QAFTestStep(description = "response should match json {expectedJsonStr}")
	public static void verifyResponseMatches(String expectedJsonStr) {
		Validator.verifyJsonMatches(new RestTestBase().getResponse().getMessageBody(), expectedJsonStr);
	}

	/**
	 * This method store value of given json path to
	 * {@link ConfigurationManager}
	 * <p>
	 * Example:
	 * </p>
	 * <code>
	 * say 'loginuser' is value at jsonpath 'user.username'
	 * </code>
	 * <p />
	 * 
	 *
	 * @param variable
	 *            variable that can be use later
	 * @param path
	 *            jsonpath
	 */
	@QAFTestStep(description = "say {var-name} is value at jsonpath {jsonpath}")
	public static void sayValueAtJsonPath(String variable, String path) {
		if (!path.startsWith("$"))
			path = "$." + path;
		Object value = JsonPath.read(new RestTestBase().getResponse().getMessageBody(), path);
		getBundle().setProperty(variable, value);
	}

	/**
	 * This is verification method to check given Xpath is there in response
	 * status of web service. It will continue test case even if failure.
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * response should have xpath 'Xpath String'<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param xpath
	 *            : {0} : xpath string to be verified in response
	 */
	@QAFTestStep(description = "response should have xpath {xpath}")
	public static void responseShouldHaveXpath(String xpath) {
		boolean hasXPath = !XPathUtils.read(new RestTestBase().getResponse().getMessageBody()).configurationsAt(xpath)
				.isEmpty();
		verifyThat("Expected xpath " + xpath, hasXPath, Matchers.is(true));
		// assertThat(the(new RestTestBase().getResponse().getMessageBody()),
		// hasXPath(xpath));
	}

	/**
	 * This is verification method to check given Xpath is not there in response
	 * status of web service. It will continue test case even if failure.
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * response should not have xpath 'Xpath String'<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param xpath
	 *            : {0} : xpath to be verified in response
	 */
	@QAFTestStep(description = "response should not have xpath {xpath}")
	public static void responseShouldNotHaveXpath(String xpath) {
		boolean hasNoXPath = XPathUtils.read(new RestTestBase().getResponse().getMessageBody()).configurationsAt(xpath)
				.isEmpty();
		verifyThat("Expected xpath " + xpath + "not present", hasNoXPath, Matchers.is(false));
	}

	/**
	 * This is verification method to check value at XPATH in response status of
	 * web service. It will continue test case even if failure. It uses
	 * {@link StringMatcher} to match expected vs actual values. You can specify
	 * matcher by providing prefix separated by ':', For example,
	 * 'containsIgnoringCase:someValue' will use
	 * {@link StringMatcher#containsIgnoringCase(String)} Default is
	 * {@link StringMatcher#exact(String)}
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * response should have value '1.90' at xpath '//Price'<br/>
	 * response should have value 'gte:1.90' at xpath '//Price'<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param val
	 *            : {val} : value to be verified in response
	 * @param xpath
	 *            : {xpath} : xpath to look value in response
	 */
	@QAFTestStep(description = "response should have value {val} at xpath {xpath}")
	public static void responseShouldHaveValueAtXpath(Object val, String xpath) {
		String actual = XPathUtils.read(new RestTestBase().getResponse().getMessageBody()).getString(xpath);
		StringMatcher matcher = getMatcher(val);
		boolean res = matcher.match(actual);
		String message = "Expected value at xpath " + xpath + " [" + matcher + "] actual [" + actual + "]";
		verifyTrue(res, message, message);
		// assertThat(the(new RestTestBase().getResponse().getMessageBody()),
		// hasXPath(xpath));
	}

	/**
	 * This is verification method to check value at XPATH in response status of
	 * web service. It will continue test case even if failure. It uses
	 * {@link StringMatcher} to match expected vs actual values. You can specify
	 * matcher by providing prefix separated by ':', For example,
	 * 'containsIgnoringCase:someValue' will use
	 * {@link StringMatcher#containsIgnoringCase(String)} Default is
	 * {@link StringMatcher#exact(String)}
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * response should have value '1.90' at xpath '//Price'<br/>
	 * response should have value 'gte:1.90' at xpath '//Price'<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param val
	 *            : {val} : value to be verified in response
	 * @param xpath
	 *            : {xpath} : xpath to look value in response
	 */
	@QAFTestStep(description = "response should not have value {val} at xpath {xpath}")
	public static void responseShouldNotHaveValueAtXpath(Object val, String xpath) {
		String actual = XPathUtils.read(new RestTestBase().getResponse().getMessageBody()).getString(xpath);
		StringMatcher matcher = getMatcher(val);
		boolean res = matcher.match(actual);
		String message = "Expected value at xpath " + xpath + " is not [" + matcher + "] actual [" + actual + "]";
		verifyFalse(res, message, message);
		// assertThat(the(new RestTestBase().getResponse().getMessageBody()),
		// hasXPath(xpath));
	}

	/**
	 * This assertion method check given Xpath is there in response status of
	 * web service. It will break test case on failure.
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * response should have xpath 'Xpath String'<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param xpath
	 *            : {0} : xpath string to be verified in response
	 */
	@QAFTestStep(description = "response has xpath {xpath}")
	public static void responseHasXpath(String xpath) {
		boolean hasXPath = !XPathUtils.read(new RestTestBase().getResponse().getMessageBody()).configurationsAt(xpath)
				.isEmpty();
		assertThat("Expected xpath " + xpath, hasXPath, Matchers.is(true));
	}

	/**
	 * This assertion method check given Xpath is not there in response status
	 * of web service. It will break test case on failure.
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * response has not xpath 'Xpath String'<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param xpath
	 *            : {0} : xpath to be verified in response
	 */
	@QAFTestStep(description = "response has not xpath {xpath}")
	public static void responseHasNotXpath(String xpath) {
		boolean hasNoXPath = XPathUtils.read(new RestTestBase().getResponse().getMessageBody()).configurationsAt(xpath)
				.isEmpty();
		assertThat("Expected xpath " + xpath + " not present", hasNoXPath, Matchers.is(true));
	}

	/**
	 * This is assertion method to check value at XPATH in response status of
	 * web service. It will break test case on failure. It uses
	 * {@link StringMatcher} to match expected vs actual values. You can specify
	 * matcher by providing prefix separated by ':', For example,
	 * 'containsIgnoringCase:someValue' will use
	 * {@link StringMatcher#containsIgnoringCase(String)} Default is
	 * {@link StringMatcher#exact(String)}
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * response has value '1.90' at xpath '//Price'<br/>
	 * response has value 'gte:1.90' at xpath '//Price'<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param val
	 *            : {val} : value to be verified in response
	 * @param xpath
	 *            : {xpath} : xpath to look value in response
	 */
	@QAFTestStep(description = "response has value {val} at xpath {xpath}")
	public static void responseHasValueAtXpath(Object val, String xpath) {
		String actual = XPathUtils.read(new RestTestBase().getResponse().getMessageBody()).getString(xpath);
		StringMatcher matcher = getMatcher(val);
		boolean res = matcher.match(actual);
		String message = "Expected value at xpath " + xpath + " [" + matcher + "] actual [" + actual + "]";
		assertTrue(res, message, message);
	}

	/**
	 * This is assertion method to check value is not at XPATH in response
	 * status of web service. It will break test case on failure. It uses
	 * {@link StringMatcher} to match expected vs actual values. You can specify
	 * matcher by providing prefix separated by ':', For example,
	 * 'containsIgnoringCase:someValue' will use
	 * {@link StringMatcher#containsIgnoringCase(String)} Default is
	 * {@link StringMatcher#exact(String)}
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * response has not value '1.90' at xpath '//Price'<br/>
	 * response has not value 'gte:1.90' at xpath '//Price'<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param val
	 *            : {val} : value to be verified in response
	 * @param xpath
	 *            : {xpath} : xpath to look value in response
	 */
	@QAFTestStep(description = "response has not value {val} at xpath {xpath}")
	public static void responseHasNotValueAtXpath(Object val, String xpath) {
		String actual = XPathUtils.read(new RestTestBase().getResponse().getMessageBody()).getString(xpath);
		StringMatcher matcher = getMatcher(val);
		boolean res = matcher.match(actual);
		String message = "Expected value at xpath " + xpath + " is not [" + matcher + "] actual [" + actual + "]";
		assertFalse(res, message, message);
	}

	/**
	 * This method store value of given xpath to {@link ConfigurationManager}
	 * <p>
	 * Example:
	 * </p>
	 * <code>
	 * say 'loginuser' is value at xpath '//user/username'
	 * </code>
	 * <p />
	 * 
	 *
	 * @param variable
	 *            variable that can be use later
	 * @param path
	 *            xpath
	 */
	@QAFTestStep(description = "say {var-name} is value at xpath {xpath}")
	public static void sayValueAtXPath(String variable, String path) {
		Object value = XPathUtils.read(new RestTestBase().getResponse().getMessageBody()).getProperty(path);
		getBundle().setProperty(variable, value);
	}
    @QAFTestStep(description = "resolve request call {0} with {data}")
    public WsRequestBean resolveWSCwithData(Object request, Map<String, Object> data) {
    	WsRequestBean bean = new WsRequestBean();
		bean.fillData(request);
		bean.resolveParameters(data);
		return bean;
    }
	// move to rest test-base
	public static ClientResponse request(WsRequestBean bean) {

		WebResource resource = new RestTestBase().getWebResource(bean.getBaseUrl(), bean.getEndPoint());

		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();

		for (Entry<String, Object> entry : bean.getQueryParameters().entrySet()) {
			queryParams.add(entry.getKey(), entry.getValue().toString());
		}
		resource = resource.queryParams(queryParams);

		Builder builder = resource.getRequestBuilder();

		for (Entry<String, Object> header : bean.getHeaders().entrySet()) {
			if (header.getKey().equalsIgnoreCase("Accept")) {
				builder.accept((String) header.getValue());
			}
			builder.header(header.getKey(), header.getValue());
		}

		String body = bean.getBody();
		if (StringUtil.isNotBlank(body)) {
			// if body then post only body

			// binary stream?
			if (StringMatcher.startsWithIgnoringCase("binary:").match(body)) {
				byte[] bytes = new byte[0];// empty content
				String file = body.split(":", 2)[1];
				if (StringUtil.isNotBlank(file)) {
					Path path = Paths.get(new File(file).getAbsolutePath());
					try {
						bytes = Files.readAllBytes(path);
					} catch (IOException e) {
						throw new AutomationError(e);
					}
				}
				return builder.method(bean.getMethod(), ClientResponse.class, bytes);
			}
			// raw body
			return builder.method(bean.getMethod(), ClientResponse.class, body);
		} else if (isFileUpload(bean.getFormParameters())) {
			String fileName = "";
			// if contains file then upload as multipart/octet-stream as per
			// Content-Type header
			try (FormDataMultiPart multiPart = new FormDataMultiPart()) {
				for (Entry<String, Object> entry : bean.getFormParameters().entrySet()) {
					String value = String.valueOf(entry.getValue());
					if (value.startsWith("file:")) {
						fileName = value.split("file:", 2)[1];
						multiPart.bodyPart(new FileDataBodyPart(entry.getKey(), new File(fileName)));
					} else {
						multiPart.field(entry.getKey(), value);
					}
				}
				if (bean.getHeaders().containsValue(MediaType.APPLICATION_OCTET_STREAM)) {
					Path path = Paths.get(new File(fileName).getAbsolutePath());
					return builder.type(MediaType.APPLICATION_OCTET_STREAM).method(bean.getMethod(),
							ClientResponse.class, Files.readAllBytes(path));
				} else {
					return builder.type(MediaType.MULTIPART_FORM_DATA).method(bean.getMethod(), ClientResponse.class,
							multiPart);
				}
			} catch (Exception e) {
				throw new AutomationError(e);
			}
		} else {
			// does not contain files
			MultivaluedMap<String, String> formParam = new MultivaluedMapImpl();
			for (Entry<String, Object> entry : bean.getFormParameters().entrySet()) {
				formParam.add(entry.getKey(), String.valueOf(entry.getValue()));
			}
			if (formParam.isEmpty()) {
				return builder.method(bean.getMethod(), ClientResponse.class);
			} else {
				return builder.method(bean.getMethod(), ClientResponse.class, formParam);
			}
		}
	}

	/**
	 * @param json
	 * @param path
	 * @return
	 */
	private static boolean hasJsonPath(String json, String path) {
		try {
			Object res = JsonPath.read(json, path);
			JSONObject resObject = new JSONObject(res);
			return !resObject.optBoolean("empty");
		} catch (Exception exception) {
			return false;
		}
	}

	private static boolean isFileUpload(Map<String, Object> formParameters) {
		for (Entry<String, Object> params : formParameters.entrySet()) {
			String value = String.valueOf(params.getValue()).trim();
			if (value.startsWith("file:"))
				return true;
		}
		return false;
	}

	/**
	 * @param jsonpath
	 * @return
	 */
	private static String getPath(String jsonpath) {
		if (!jsonpath.startsWith("$"))
			jsonpath = "$." + jsonpath;
		return jsonpath;
	}

	private static StringMatcher getMatcher(Object o) {
		if (o instanceof StringMatcher)
			return (StringMatcher) o;
		String s = o.toString();
		if (s.indexOf(':') > 0) {
			String[] parts = s.split(":", 2);
			StringMatcher m = StringMatcher.get(parts[0], parts[1]);
			if (null != m)
				return m;
		}
		return StringMatcher.exact(s);
	}
}
