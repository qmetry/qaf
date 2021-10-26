---
title: Different ways of providing properties
sidebar: qaf_3_0_0-sidebar
permalink: qaf-3.0.0/different_ways_of_providing_prop.html
folder: qaf-3.0.0
---


QAF provide different way to specify properties and you need to know which value will take effect when you are providing same property with multiple ways.
Following are ways of providing property in the order of their priority.

1. System property
2. TestNG parameter
3. Property file (either xml or .properties)

Consider the following default env.properties file where properties are provided for execution environment.

```properties
#set one of the platform: mobile or web
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

## Encryption support
With 2.1.13, QAF supports encryted values for any secure data, for instance password. When configuration manager found any key starts with `encrypted` prefix, for example `encripted.db.pwd`, then it will store decrypted value without prefix, `db.pwd` in this example. So you can reference decrypted value anywhere in the code with key without this prefix (`db.pwd` in this example). 

By default encryption is Base64 so value for the properties with prefix `encrypted` is expected Base64 encrypted. If you want to use custom encryption algorithm and provide other than Base64 encrypted value, you need to set PasswordDecryptor using property `password.decryptor.impl` with fully qualified name of the class that implements `PasswordDecryptor`. The implementation will be used to decrypt password, When configuration manager found any key starts with `encrypted`

**Example:**
Property file:

```properties
encrypted.user.pwd=Q2hpcmFnMTIzIw==
```

in code:

```java
String pwd = getBundle().getString("user.pwd");
element.sendKeys(pwd);
```

If you want sendkeys to log encrypted password instead of plain text not using property to hold value (for example data-driven test), set element meta-data `type` to `password` and use encrypted value. Make sure you haven't disabled element meta-data listener. For example:

Locator:

```properties
my.pwd.fld = {"locator":"xpath=//*[@name='password']","desc":"Password Input box", "type":'password'}
```


```java
String encryptedPwd = getBundle().getString("encrypted.user.pwd");
element.sendKeys(encryptedPwd);
element.sendKeys("Q2hpcmFnMTIzIw==");

```
