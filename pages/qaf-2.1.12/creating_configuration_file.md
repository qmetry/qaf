---
title: Creating configuration file
sidebar: qaf_2_1_12-sidebar
permalink: qaf-2.1.12/creating_configuration_file.html
folder: qaf-2.1.12
---

To configure test run, you need to provide configuration file. As QAF is built upon TestNG you need to provide XML or YAML configuration file that is supported by TestNG. Please refer TestNG Documentation for syntax and sample of XML and YAML configuration file.
In addition QAF provides following features.

## Use of properties in parameter value

You can make use of any property for defining value of parameter.

```xml
<parameter name="env.resources" value="resources/mobile/${brand.name}" />
```


Note: While accessing such parameter using @parameter annotation of TestNG the property value will not get reflected in parameter value. In such case you need to take care in code.

## Configuring combinations

See the below example of configuration file. It will execute test for "Westin" brand for "mobile" platform of AUT on two different mobile platform android and IPhone in parallel.

```xml	
<suite name="AUT Test Automation" verbose="0" parallel="true">
      <parameter name="brand.name" value="westin" />
      <parameter name="target.platform" value="mobile" />
      <test name="Mobile Web Tests on IPhone">
            <parameter name="remote.server" value="10.12.49.180"/>
            <parameter name="remote.port" value="3001" />
            <parameter name="driver.name" value="iphoneRemoteDriver" />           
            ...
      </test>
      <test name="Mobile Web Tests on android">
            <parameter name="remote.server" value="10.12.48.87"/>
            <parameter name="remote.port" value="8080" />
            <parameter name="driver.name" value="androidRemoteDriver"/>                      
            ...
      </test>
 </suite>
```

## Accessing parameter in code

Any parameter provided through configuration file will be available through configuration manager in addition to @parameter annotation. So one can access parameter in code, which is not TestNG test or configuration method.

```xml	
<suite name="AUT Test Automation" verbose="0" parallel="false">
      <parameter name="brand.name" value="westin" />
      <test name="Mobile Web Tests">
            <parameter name="target.platform" value="mobile" />
            ...
      </test>
      <test name="Branded Web Tests">
            <parameter name="target.platform" value="web" />
            ...
      </test>
</suite>
```
 
In above configuration file "target.platform" is provided as parameter that can be accessed in code as given below:

```java
String platform = ConfigurationManager.getBundle().getString("target.platform");
```


Here during execution of test case for "Mobile Web Tests" the parameter value will be "mobile" and for "Branded Web Tests" it will be "web" as defined in configuration file.
