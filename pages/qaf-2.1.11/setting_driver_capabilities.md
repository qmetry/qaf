---
title: Setting driver capabilities
sidebar: qaf_2_1_11-sidebar
permalink: qaf-2.1.11/setting_driver_capabilities.html
folder: qaf-2.1.11
---

## Setting driver capabilities	


Following properties are available to set driver capabilities in order of their priority 

* &lt;driverName&gt;.capabilities.&lt;capabilityName&gt; (**highest priority**)
* &lt;driverName&gt;.additional.capabilities
* driver.capabilities.&lt;capabilityName&gt;
* driver.additional.capabilities (**lowest priority**)


<br/>

**&lt;driverName&gt;.capabilities.&lt;capabilityName&gt;**

can be used to set individual capability for specific driver

**Examples:**

```properties
firefox.capabilities.version = 48
appium.capabilities.automationName = Appium
appium.capabilities.applicationName = Calculator

```

**&lt;driverName&gt;.additional.capabilities**

can be used to set all capabilities for specific driver. This need to be provided as JSON map of capability: value pair.

**Example**


```properties
appium.additional.capabilities = {'user':'user1@infostretch.com','password':'password','automationName':'Appium','applicationName':'Calculator'}
```

**driver.capabilities.&lt;capabilityName&gt;** 

Can be used to set individual capability for all driver.

**Example** 

```properties
driver.capabilities.user = myCloudeUser
driver.capabilities.password = myPwd
driver.capabilities.plateform = Windows
```
**driver.additional.capabilities**

can be used to set all capabilities for each driver. This need to be provided as JSON map of capability: value pair.


```properties
driver.additional.capabilities = {'user':'user1@infostretch.com','password':'password','automationName':'Appium','applicationName':'Calculator'}
```


{% include note.html content=" If same capability set through more than one of above properties then preference for capabilities to be loaded will be considered as per the priority of the property." %}
