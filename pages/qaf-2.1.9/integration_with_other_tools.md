---
title: Integration
sidebar: qaf_2_1_9-sidebar
permalink: qaf-2.1.9/integration_with_other_tools.html
folder: latest
---


Using QAF you can integrate any Test Management Tool to update Test-Result after test execution completed.

1. Create a class which implements `TestCaseResultUpdator` interface.
2. Specify qualified class name in `result.updator` property.
3. Overide methods to provide test management tool specific implementation/method calls


`TestCaseResultUpdator` interface defines following methods.

```java
boolean updateResult(Map<String, ? extends Object> params,TestCaseRunResult result, String details);
String getToolName();
```

Example:

```java
package com.qmetry.qaf.automation.integration.example
...

public class ExampleResultUpdator implements TestCaseResultUpdator{

	@Override
	public String getToolName() {
		return "Example";
	}

	/**
	 * This method will be called by result updator after completion of each testcase/scenario.
	 * @param params
	 *            tescase/scenario meta-data including method parameters if any
	 * @param result
	 *            test case result
	 * @param details
	 *            run details
	 * @return
	 */

	@Override
	public boolean updateResult(Map<String, ? extends Object> params,
			TestCaseRunResult result, String details) {

		// Provide test management tool specific implemeneation/method calls
		
		return true;
	}

}
```
Registering class for Result updator

To register result updator class set property **result.updator**

Property:

```properties
result.updator=com.qmetry.qaf.automation.integration.example.ExampleResultUpdator
```
