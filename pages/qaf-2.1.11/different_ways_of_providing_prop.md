---
title: Different ways of providing properties
sidebar: qaf_2_1_11-sidebar
permalink: qaf-2.1.11/different_ways_of_providing_prop.html
folder: latest
---


QAF provide different way to specify properties and you need to know which value will take effect when you are providing same property with multiple ways.
Following are ways of providing property in the order of their priority.

1. System property
2. TestNG parameter
3. Property file (either xml or .properties)

Consider the following default env.properties file where properties are provided for execution environment.
#set one of the platform: mobile or web

```properties
target.platform=mobile
brand.name=westin
```

 Now consider the following configuration file.

```xml 
<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd">
<suite name="AUT Test Automation" verbose="0" parallel="false">
    <test name="whotels brand">
        <parameter name="brand.name" value="whotels" />
        <classes>
            <class name="com.aut.automation.tests.web.Demo" />
        </classes>
    </test>
</suite>
```

Here you can see that "brand.name" is provided at two places, one in property file and second in configuration file. In this case "brand.name" value will take effect from configuration file as it has high priority then property defined in property file.

 
