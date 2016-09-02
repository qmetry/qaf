---
title: Web Services Testing
sidebar: qaf_2_1_7b_sidebar
permalink: qaf-2.1.7b/web_services.html
folder: qaf-2.1.7b
tags: [java]
---

QAF provides support classes for Webservice Testing. It displays all details of web services request/response in QAF Report.

You can create a new test case or suit by extending `RestWSTestCase` class.

On extending `RestWSTestCase` following object will be available to use in your test.

1. **WebResource**

	[WebResource](https://jersey.java.net/apidocs/1.19/jersey/com/sun/jersey/api/client/WebResource.html) instance can be accessed using `getWebResource` method. WebResource is a useful class to specify request body, headers and invoking Http Requests.

2. **Client**

	[Client](https://jersey.java.net/apidocs/1.19/jersey/com/sun/jersey/api/client/Client.html) instance can be accessed using `getClient` method. Client instance is useful for configuring the properties of connections and requests.

3. **Response**
	
	Response instance can be accessed using `getResponse` method. Using response class user can access response status code, response body and response headers.

4. **Validation Methods**
	
	All the methods of [Validator](assertion_verification.html#validator-class) can be accessed directly in testcase or suite.

**Example**

Below is an sample example to invoke http request and verify response code using QAF.

```java
public class Wstest extends RestWSTestCase {

	@Test(testName = "create order")
	public void createOrder() {

		//request data
		Map<String, String> data = new HashMap<String, String>();
		data.put("clientName", "Amit");
		data.put("amount", "100");
		
		//create post request
		getWebResource("/orders.json").post(new Gson().toJson(data));
		
		//verify response
		verifyThat("Response Status", getResponse().getStatus(), Matchers.equalTo(Status.CREATED));
	}
}
```

For more reference visit [Jersey Client API Documentation](https://jersey.java.net/documentation/1.19/client-api.html).