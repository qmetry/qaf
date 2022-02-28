---
title: How to run existing Appium testcases on Perfecto?
sidebar: faq_sidebar
permalink: qaf-3.0.1/how_to_run_appium_testcases_on_perfecto.html
folder: qaf-3.0.1
---

Follow the below steps to execute Appium scripts on Perfecto.

**Step 1:**

Below are the properties that we need to set in **application.properties** file

```properties
appium.additional.capabilities={

'user':'user@infostretch.com',

'password':'password',

'deviceName':'0123456789',

'automationName':'Appium',

'platformName':'Android',

'driverClass':'io.appium.java_client.android.AndroidDriver'

}
```

```properties
remote.server=https://cloud.perfectomobile.com/nexperience/perfectomobile/wd/hub

driver.name=appiumDriver

env.baseurl=https://cloud.perfectomobile.com/nexperience/perfectomobile/wd/hub

appName = Contact Manager
```

**Step 2:**

Create **Webdriver listener.** Create or update **onInitialize** method, to launch application when driver initialized.

...

```java
public class PerfectoDriverListener extends QAFWebDriverCommandAdapter {

    @Override
    public void onInitialize(QAFExtendedWebDriver driver) {
        String appName = ConfigurationManager.getBundle().getString("appName");
        
        //Close Application if already its opened
        String command = "mobile:application:close";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", appName);
        try {
            ((RemoteWebDriver) driver.getUnderLayingDriver())
                    .executeScript(command, params);
        } catch (Exception e) {
            System.err.println("Unable to close app: " + appName);
        }

        //Open Application
        command = "mobile:application:open";
        ((RemoteWebDriver) driver.getUnderLayingDriver()).executeScript(
                command, params);
        
        
        //Switch Context
        RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(
                (RemoteWebDriver) driver.getUnderLayingDriver());
        params.clear();
        params.put("name","NATIVE");
        executeMethod.execute(DriverCommand.SWITCH_TO_CONTEXT, params);
    }

}
```
 

**Step 4:**

**Registering listener**

To register listener set property **“wd.command.listeners”.**

For example to register above created listener you need to set property as below:

```properties
wd.command.listeners=com.ispl.automation.sample.support.webdriver.PerfectoDriverListener
```

 
