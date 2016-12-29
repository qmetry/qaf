---
title: Custom Retry Analyzer
sidebar: qaf_2_1_9-sidebar
permalink: qaf-2.1.9/custom_retry_analyzer.html
folder: latest
---

## Custom Retry Analyzer



Provide this property to use your custom retry analyzer.

Provide own implementation for retry analyzer by specifying qualified class name in 'retry.analyzer' Property.

Once 'retry.analyzer' has been specified 'retry.count' property will be not considered.

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
