---
title: QAF listeners
sidebar: qaf_2_1_9-sidebar
permalink: qaf-2.1.9/qaf_listeners.html
folder: qaf-2.1.9
tags: [webelement,webdriver,java]
---

QAF provides command listener support for both Selenium 1 and 2 API.

The general idea is that one or more objects (the listeners) register their interest in being notified of command execution on selenium/webdriver/webelement.

The listener can perform some actions or track details before/after command execution as well as on failure.
You can create listener by implementing appropriate listener interface of by extending appropriate adapter class. To register one or more listener you need to set appropriate property.

## Listeners API Table

| List Interface | Adapter Class | Property | Listener Methods |
|-------|--------|---------|---------|
| QAFTestStepListener | QAFTestStepAdapter  | teststep.listeners  | onFailure(StepExecutionTracker)
| | | | beforExecute(StepExecutionTracker)
| | | | afterExecute(StepExecutionTracker)
| QAFWebDriverCommandListener | QAFWebDriverCommandAdapter | wd.command.listeners |beforeInitialize(Capabilities)
| | | | onInitialize(QAFExtendedWebDriver)
| | | | beforeCommand(QAFExtendedWebDriver, CommandTracker)
| | | | afterCommand(QAFExtendedWebDriver, CommandTracker)
| | | | onFailure(QAFExtendedWebDriver, CommandTracker)
| QAFWebElementCommandListener | QAFWebElementCommandAdapter | we.command.listeners | beforeCommand(QAFExtendedWebElement, CommandTracker)
| | | | afterCommand(QAFExtendedWebElement, CommandTracker)
| | | | onFailure(QAFExtendedWebElement, CommandTracker)

## TestStep Listener

In order to create teststep listener, you need to implement listener interface ```com.qmetry.qaf.automation.step.QAFTestStepListener``` or extend adapter class ```QAFTestStepAdapter```.

```QAFTestStepListener``` defines following methods:

```java	
void beforExecute(StepExecutionTracker stepExecutionTracker);
void afterExecute(StepExecutionTracker stepExecutionTracker);
void onFailure(StepExecutionTracker stepExecutionTracker);
```
**Creating listener**

```java	
public class ThresholdListener extends QAFTestStepAdapter {
    @Override
    public void afterExecute(StepExecutionTracker stepExecutionTracker) {
        long duration = (stepExecutionTracker.getEndTime() - stepExecutionTracker.getStartTime()) / 1000;
        if (stepExecutionTracker.getStep().getThreshold() > duration)
            stepExecutionTracker.setVerificationError("Threshold value is exceed");
    }
}
```

**Registering listener**

To register listener set property **teststep.listeners**.

For example to register above created listener you need to set property as below:

```properties
teststep.listeners=com.qmetry.qaf.listeners.ThresholdListener
```

## WebDriver Listener

In order to create webdriver command listener, you need to implement listener interface ```QAFWebDriverCommandListener``` or extend adapter class ```QAFWebDriverCommandAdapter```.

```QAFWebDriverCommandListener``` defines following methods:

```java	
void beforeInitialize(Capabilities)
void beforeCommand(QAFExtendedWebDriver paramQAFExtendedWebDriver, CommandTracker paramCommandTracker);
void afterCommand(QAFExtendedWebDriver paramQAFExtendedWebDriver, CommandTracker paramCommandTracker);
void onFailure(QAFExtendedWebDriver paramQAFExtendedWebDriver, CommandTracker paramCommandTracker);
void onInitialize(QAFExtendedWebDriver driver);
```

**Creating listener** 

```java
public class WDListener extends QAFWebDriverCommandAdapter {
    Log logger = LogFactory.getLog(getClass());
    @Override
    public void beforeCommand(QAFExtendedWebDriver driver, CommandTracker commandTracker) {
        super.beforeCommand(driver, commandTracker);
        String command = commandTracker.getCommand();
        Map<String, Object> params = commandTracker.getParameters();
        // support selenium 1 api for navigating from frame to main window
        // with selenium.selectFrame("");
        if (command.equalsIgnoreCase(DriverCommand.SWITCH_TO_FRAME) && StringUtil.isBlank((String) params.get("id"))) {
            String mainWindow = driver.getWindowHandle();
            params.put("id", mainWindow);
            driver.switchTo().window(mainWindow);
            // skip original command execution
            commandTracker.setResponce(new Response());
        }
        if (command.equalsIgnoreCase(DriverCommand.SWITCH_TO_WINDOW)) {
            String targetWindow = String.valueOf(commandTracker.getParameters().get("name"));
            String parentHandle = driver.getWindowHandle();
            if (!targetWindow.equalsIgnoreCase(parentHandle)) {
                // store the parent window handle
                ConfigurationManager.getBundle().setProperty("parentWindowHandel", parentHandle);
            }
        }
    }
    @Override
    public void onFailure(QAFExtendedWebDriver driver, CommandTracker commandTracker) {
        super.onFailure(driver, commandTracker);
        if (commandTracker.getCommand().equalsIgnoreCase(DriverCommand.SWITCH_TO_WINDOW)
                && commandTracker.hasException()) {
            Set<String> windows = driver.getWindowHandles();
            String parentWindowHandel = ConfigurationManager.getBundle().getString("parentWindowHandel");
            for (String window : windows) {
                if (!parentWindowHandel.equalsIgnoreCase(window)) {
                    driver.switchTo().window(window);
                    // now we can remove the exception, it is handled!
                    commandTracker.setException(null);
                    break;
                }
            }
        }
    }
}
```

**Registering listener**

To register listener set property **wd.command.listeners**. For example to register above created listener you need to set property as below:

```properties
wd.command.listeners= com.ispl.automation.sample.webdriver.WDListener
```

## WebElement Listener
A simple but very useful example of listener is "listener for sendkeys". As you know in webdriver send keys will append the text box, so each time we need to first call clear command and then use send keys command.

**Code Without listener:**

```java
firstName.clear();
firstName.sendKeys("fname");
lastName.clear();
firstName.sendKeys("lname");
```

**Code With use of listener:**

```java
firstName.sendKeys("fname");
firstName.sendKeys("lname");
```

**Creating listener**

In order to create webelement command listener, you need to implement listener interface ```QAFWebElementCommandListener``` or extend adapter class ```QAFWebElementCommandAdapter```.

```QAFWebElementCommandListener``` defines following methods:

```java
void beforeCommand(QAFExtendedWebElement paramQAFExtendedWebElement, CommandTracker paramCommandTracker);
void afterCommand(QAFExtendedWebElement paramQAFExtendedWebElement, CommandTracker paramCommandTracker);
void onFailure(QAFExtendedWebElement paramQAFExtendedWebElement, CommandTracker paramCommandTracker);
```

Below example illustrates how we can write a listener that clears element text before sending text.

```java	
public class SendKeysListener extends QAFWebElementCommandAdapter {
    @Override
    public void beforeCommand(QAFExtendedWebElement element, CommandTracker commandTracker) {
        if (commandTracker.getCommand().equalsIgnoreCase(DriverCommand.SEND_KEYS_TO_ELEMENT)) {
            element.clear();
            // you access/modify any parameter!
            String value = String.valueOf(commandTracker.getParameters().get("value"));
            if (StringUtil.isBlank(value)) {
                // No need to send key
                // ignore actual command
                commandTracker.setResponce(new Response());
            }
        }
    }
}
```

In this example we had extended adapter class for webelement command listener and provided implementation for ```beforeCommand```. Same way you can provide implementation for ```afterCommand```and ```onFailure``` method if required. You can see that one can access parameters in listener and also can modify its value if required.

**Registering listener** 

To register listener set property **we.command.listeners**. For example to register above created listener you need to set property as below:

```properties
we.command.listeners= com.ispl.automation.sample.webdriver.SendKeysListener
```



