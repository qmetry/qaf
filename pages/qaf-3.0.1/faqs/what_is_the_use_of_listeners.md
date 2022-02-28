---
title: What is the use of listeners?
sidebar: faq_sidebar
permalink: qaf-3.0.1/what_is_the_use_of_listeners.html
folder: qaf-3.0.1
---

The general idea is that one or more objects (the listeners) register their interest in being notified of command execution on selenium/webdriver/webelement. The listener can perform some actions or track details before/after command execution as well as on failure. You can create listener by implementing appropriate listener interface of by extending adaptor class. To register listener set property “wd.command.listeners” for web driver listener and “we.command.listeners” for web element listener.

A simple but very useful example of listener is “listener for sendkeys”. As you know in webdriver send keys will append the text box, so each time we need to first call clear command and then use send keys command.

**Without listener**

* firstName.clear();
	
* firstName.sendKeys(“fname”);
	
* lastName.clear();
	
* lastName.sendKeys(“lname”);

**With listener**

* firstName.sendKeys(“fname”);

* lastName.sendKeys(“lname”);

 

Listener for above requirement can be implemented as below:

 
```java

public class SendKeysListener extends QAFWebElementCommandAdapter {

    @Override
    public void beforeCommand(QAFExtendedWebElement element,
            CommandTracker commandTracker) {

        if (commandTracker.getCommand().equalsIgnoreCase(
                DriverCommand.SEND_KEYS_TO_ELEMENT)) {
            element.clear();
        }

    }

}

```
