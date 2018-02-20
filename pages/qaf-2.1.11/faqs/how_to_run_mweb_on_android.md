---
title: How to run mobile web automation on Android
sidebar: faq_sidebar
permalink: qaf-2.1.11/how_to_run_mweb_on_android.html
folder: qaf-2.1.11
---

Following are possible ways for mobile web for android platform.

1.Using chromedriver for mobile chrome on emulator/device
2.Using appium for mobile chrome on  emulator/device


**Using chromedriver for  mobile chrome on  emulator/device**

1.Download chrome driver

2.set driver as chrome driver

```properties	
	driver.name=chromeDriver
```	
3.set property webdriver.chrome.driver property

```properties	
	webdriver.chrome.driver=d:/chromedriver.exe
```	

4.Set chrome.additional.capabilities to run test on mobile chrome.

5.Start emulator or connect device

6.Make sure chrome browser is installed in emulator/device

7.Run tests



**Using appium for  mobile chrome on  emulator/device**

1.Make sure Appium java client lib is in your ivy/class path

```xml	
    <dependency org="io.appium" name="java-client" rev="1.6.2"/>
```	
2.set driver as appium Driver

```properties
    driver.name=appiumDriver
```	
3.Set additional capabilities, you need to provide "app" value "Chrome". Here is the sample

```properties	
    appium.additional.capabilities={'deviceName':'<device_id>','platformName':'Android','automationName':'Appium','browserName': 'Chrome', 'driverClass' : 'io.appium.java_client.android.AndroidDriver'}
```	

4.Start emulator or connect device where appium server is running

5.Make sure chrome browser is installed in emulator/device

6.Run tests....

