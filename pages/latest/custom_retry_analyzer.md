---
title: Retry Analyzer
sidebar: qaf_latest-sidebar
permalink: latest/custom_retry_analyzer.html
folder: latest
---

## Retrying failed tests

Set `retry.count` property to auto retry failed testcase. Default value is 0 ( retry.count=0). QAF's internal retry analyzer uses `retry.count` property to determine maximum number of time to retry the test. 

QAF internal retry analyzer will not retry if test failed because of any checkpoint failure.

## Custom Retry Analyzer

If you don't want to use default retry analyzer provided by QAF, you can have your custom retry analyzer.

To custom retry analyzer, implement `IRetryAnalyzer` and set qualified class name using `retry.analyzer` Property.

Once `retry.analyzer` has been specified `retry.count` property will be not considered.

**For Example :**


```java	
public class CustomRetryAnalyzer implements IRetryAnalyzer{
    @Override
    public boolean retry(ITestResult result) {
        // TODO Auto-generated method stub
        return false;
    }
}
```
