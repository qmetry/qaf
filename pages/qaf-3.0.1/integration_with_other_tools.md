---
title: Integration
sidebar: qaf_3_0_1-sidebar
permalink: qaf-3.0.1/integration_with_other_tools.html
folder: qaf-3.0.1
---


Using QAF you can integrate any Test Management Tool to update Test-Result after test execution completed.

1. Create a class which implements [TestCaseResultUpdator](javadoc/com/qmetry/qaf/automation/integration/TestCaseResultUpdator.html) interface.
2. Specify qualified class name in `result.updator` property.
3. Overide methods to provide test management tool specific implementation/method calls


`TestCaseResultUpdator` interface also has following default methods that can be implemented if required.


  * `default boolean 	allowConfigAndRetry()` - by default will return false, configuration and retry methods will not be sent to updateResult. You can Set weather configuration methods and retry also should be reported or not by overriding this method.

  * `default boolean 	allowParallel()` - By default result updator uses separate multi-threaded pool, if you want to run in single thread set it to false.

  * `default void beforeShutDown()` - Each Updator class can define implementation for this method to do clean up before shout down.

  * `default boolean 	enabled()` - Useful when registered with service, to enable disable based on some condition. Default implementation will return true, means always enabled.

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
	public boolean updateResult(TestCaseRunResult result) {

		// Provide test management tool specific implementation/method calls
		
		return true;
	}

}
```
Registering class for Result updater

To register result updater class set property **result.updator**. You can register more that one updater class. 

Property:

```properties
result.updator=com.qmetry.qaf.automation.integration.example.ExampleResultUpdator
```

QAF also supports to register result updater as service.
